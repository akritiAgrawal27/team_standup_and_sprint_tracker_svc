package com.sprinttracker.dto;

import com.sprinttracker.entity.Task;

import java.time.Instant;

public record TaskResponse(
        Long id, Long sprintId, String title, int storyPoints,
        String status, Long assigneeId, Instant completedAt
) {
    public static TaskResponse from(Task task) {
        return new TaskResponse(
                task.getId(), task.getSprintId(), task.getTitle(), task.getStoryPoints(),
                task.getStatus(), task.getAssigneeId(), task.getCompletedAt()
        );
    }
}
