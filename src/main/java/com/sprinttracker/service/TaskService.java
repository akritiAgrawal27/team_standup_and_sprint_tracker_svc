package com.sprinttracker.service;

import com.sprinttracker.common.ResourceNotFoundException;
import com.sprinttracker.entity.Sprint;
import com.sprinttracker.entity.Task;
import com.sprinttracker.dto.CreateTaskRequest;
import com.sprinttracker.dto.TaskResponse;
import com.sprinttracker.dto.UpdateTaskStatusRequest;
import com.sprinttracker.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final SprintService sprintService;
    private final TeamService teamService;

    public TaskService(TaskRepository taskRepository, SprintService sprintService, TeamService teamService) {
        this.taskRepository = taskRepository;
        this.sprintService = sprintService;
        this.teamService = teamService;
    }

    @Transactional
    public TaskResponse createTask(Long currentUserId, Long sprintId, CreateTaskRequest request) {
        Sprint sprint = sprintService.getSprintOrThrow(sprintId);
        teamService.requireMember(sprint.getTeamId(), currentUserId);

        Task task = Task.builder()
                .sprintId(sprintId)
                .title(request.title())
                .storyPoints(request.storyPoints() == null ? 0 : request.storyPoints())
                .assigneeId(request.assigneeId())
                .build();
        task = taskRepository.save(task);
        sprintService.evictVelocity(sprint.getTeamId());
        return TaskResponse.from(task);
    }

    public List<TaskResponse> listTasks(Long currentUserId, Long sprintId) {
        Sprint sprint = sprintService.getSprintOrThrow(sprintId);
        teamService.requireMember(sprint.getTeamId(), currentUserId);
        return taskRepository.findBySprintIdOrderByCreatedAtAsc(sprintId).stream()
                .map(TaskResponse::from)
                .toList();
    }

    @Transactional
    public TaskResponse updateStatus(Long currentUserId, Long taskId, UpdateTaskStatusRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + taskId));
        Sprint sprint = sprintService.getSprintOrThrow(task.getSprintId());
        teamService.requireMember(sprint.getTeamId(), currentUserId);

        task.setStatus(request.status());
        task.setCompletedAt("DONE".equals(request.status()) ? Instant.now() : null);
        task = taskRepository.save(task);

        sprintService.evictVelocity(sprint.getTeamId());
        return TaskResponse.from(task);
    }
}
