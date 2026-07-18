package com.sprinttracker.dto;

import com.sprinttracker.entity.Sprint;

import java.time.LocalDate;

public record SprintResponse(
        Long id, Long teamId, String name, String goal,
        LocalDate startDate, LocalDate endDate, String status
) {
    public static SprintResponse from(Sprint sprint) {
        return new SprintResponse(
                sprint.getId(), sprint.getTeamId(), sprint.getName(), sprint.getGoal(),
                sprint.getStartDate(), sprint.getEndDate(), sprint.getStatus()
        );
    }
}
