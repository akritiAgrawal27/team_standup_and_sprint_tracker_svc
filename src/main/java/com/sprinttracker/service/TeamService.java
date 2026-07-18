package com.sprinttracker.service;

import com.sprinttracker.common.AccessDeniedApplicationException;
import com.sprinttracker.common.ResourceNotFoundException;
import com.sprinttracker.dto.AddMemberRequest;
import com.sprinttracker.dto.CreateTeamRequest;
import com.sprinttracker.dto.TeamMemberResponse;
import com.sprinttracker.dto.TeamResponse;
import com.sprinttracker.entity.Team;
import com.sprinttracker.entity.TeamMember;
import com.sprinttracker.entity.TeamMemberId;
import com.sprinttracker.entity.User;
import com.sprinttracker.repository.TeamMemberRepository;
import com.sprinttracker.repository.TeamRepository;
import com.sprinttracker.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final UserRepository userRepository;

    public TeamService(TeamRepository teamRepository, TeamMemberRepository teamMemberRepository, UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.teamMemberRepository = teamMemberRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public TeamResponse createTeam(Long currentUserId, CreateTeamRequest request) {
        Team team = Team.builder().name(request.name()).createdBy(currentUserId).build();
        team = teamRepository.save(team);

        TeamMember member = TeamMember.builder()
                .id(new TeamMemberId(team.getId(), currentUserId))
                .role("ADMIN")
                .build();
        teamMemberRepository.save(member);

        return TeamResponse.from(team, "ADMIN");
    }

    public List<TeamResponse> myTeams(Long currentUserId) {
        return teamMemberRepository.findById_UserId(currentUserId).stream()
                .map(tm -> teamRepository.findById(tm.getId().getTeamId())
                        .map(team -> TeamResponse.from(team, tm.getRole()))
                        .orElse(null))
                .filter(t -> t != null)
                .toList();
    }

    public List<TeamMemberResponse> listMembers(Long currentUserId, Long teamId) {
        requireMember(teamId, currentUserId);
        return teamMemberRepository.findById_TeamId(teamId).stream()
                .map(tm -> {
                    User u = userRepository.findById(tm.getId().getUserId())
                            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
                    return new TeamMemberResponse(u.getId(), u.getName(), u.getEmail(), tm.getRole());
                })
                .toList();
    }

    @Transactional
    public TeamMemberResponse addMember(Long currentUserId, Long teamId, AddMemberRequest request) {
        requireAdmin(teamId, currentUserId);

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("No user found with email " + request.email()));

        if (teamMemberRepository.existsById_TeamIdAndId_UserId(teamId, user.getId())) {
            throw new IllegalArgumentException("User is already a member of this team");
        }

        TeamMember member = TeamMember.builder()
                .id(new TeamMemberId(teamId, user.getId()))
                .role("MEMBER")
                .build();
        teamMemberRepository.save(member);

        return new TeamMemberResponse(user.getId(), user.getName(), user.getEmail(), "MEMBER");
    }

    public void requireMember(Long teamId, Long userId) {
        if (!teamMemberRepository.existsById_TeamIdAndId_UserId(teamId, userId)) {
            throw new AccessDeniedApplicationException("You are not a member of this team");
        }
    }

    private void requireAdmin(Long teamId, Long userId) {
        TeamMember member = teamMemberRepository.findById_TeamIdAndId_UserId(teamId, userId)
                .orElseThrow(() -> new AccessDeniedApplicationException("You are not a member of this team"));
        if (!"ADMIN".equals(member.getRole())) {
            throw new AccessDeniedApplicationException("Only team admins can do this");
        }
    }
}
