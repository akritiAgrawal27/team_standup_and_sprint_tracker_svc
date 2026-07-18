package com.sprinttracker.dto;

import java.time.LocalDate;

public record VelocityPoint(
        Long sprintId, String sprintName, LocalDate startDate, LocalDate endDate,
        int plannedPoints, int completedPoints
) {
}
