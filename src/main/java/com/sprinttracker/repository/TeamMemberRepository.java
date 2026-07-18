package com.sprinttracker.repository;

import com.sprinttracker.entity.TeamMember;
import com.sprinttracker.entity.TeamMemberId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamMemberRepository extends JpaRepository<TeamMember, TeamMemberId> {

    List<TeamMember> findById_UserId(Long userId);

    List<TeamMember> findById_TeamId(Long teamId);

    Optional<TeamMember> findById_TeamIdAndId_UserId(Long teamId, Long userId);

    boolean existsById_TeamIdAndId_UserId(Long teamId, Long userId);
}
