package com.sprinttracker.service;

import com.sprinttracker.common.ResourceNotFoundException;
import com.sprinttracker.config.CacheNames;
import com.sprinttracker.dto.CreateSprintRequest;
import com.sprinttracker.dto.SprintResponse;
import com.sprinttracker.dto.UpdateSprintStatusRequest;
import com.sprinttracker.dto.VelocityPoint;
import com.sprinttracker.entity.Sprint;
import com.sprinttracker.repository.SprintRepository;
import com.sprinttracker.repository.TaskRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SprintService {

    private final SprintRepository sprintRepository;
    private final TaskRepository taskRepository;
    private final TeamService teamService;

    public SprintService(SprintRepository sprintRepository, TaskRepository taskRepository, TeamService teamService) {
        this.sprintRepository = sprintRepository;
        this.taskRepository = taskRepository;
        this.teamService = teamService;
    }

    @Transactional
    @CacheEvict(value = CacheNames.VELOCITY, key = "#teamId")
    public SprintResponse createSprint(Long currentUserId, Long teamId, CreateSprintRequest request) {
        teamService.requireMember(teamId, currentUserId);

        Sprint sprint = Sprint.builder()
                .teamId(teamId)
                .name(request.name())
                .goal(request.goal())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .build();
        sprint = sprintRepository.save(sprint);
        return SprintResponse.from(sprint);
    }

    public List<SprintResponse> listSprints(Long currentUserId, Long teamId) {
        teamService.requireMember(teamId, currentUserId);
        return sprintRepository.findByTeamIdOrderByStartDateDesc(teamId).stream()
                .map(SprintResponse::from)
                .toList();
    }

    public Sprint getSprintOrThrow(Long sprintId) {
        return sprintRepository.findById(sprintId)
                .orElseThrow(() -> new ResourceNotFoundException("Sprint not found: " + sprintId));
    }

    @Transactional
    @CacheEvict(value = CacheNames.VELOCITY, key = "#result.teamId()")
    public SprintResponse updateStatus(Long currentUserId, Long sprintId, UpdateSprintStatusRequest request) {
        Sprint sprint = getSprintOrThrow(sprintId);
        teamService.requireMember(sprint.getTeamId(), currentUserId);
        sprint.setStatus(request.status());
        sprint = sprintRepository.save(sprint);
        return SprintResponse.from(sprint);
    }

    @Cacheable(value = CacheNames.VELOCITY, key = "#teamId")
    public List<VelocityPoint> getVelocity(Long currentUserId, Long teamId) {
        teamService.requireMember(teamId, currentUserId);
        return sprintRepository.findByTeamIdOrderByStartDateDesc(teamId).stream()
                .map(sprint -> new VelocityPoint(
                        sprint.getId(),
                        sprint.getName(),
                        sprint.getStartDate(),
                        sprint.getEndDate(),
                        taskRepository.sumPlannedPoints(sprint.getId()),
                        taskRepository.sumCompletedPoints(sprint.getId())
                ))
                .sorted((a, b) -> a.startDate().compareTo(b.startDate()))
                .toList();
    }

    @CacheEvict(value = CacheNames.VELOCITY, key = "#teamId")
    public void evictVelocity(Long teamId) {
        // no-op body; annotation handles eviction
    }
}
