package com.sprinttracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateSprintRequest(
        @NotBlank(message = "sprint name is required") String name,
        String goal,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate
) {
}
