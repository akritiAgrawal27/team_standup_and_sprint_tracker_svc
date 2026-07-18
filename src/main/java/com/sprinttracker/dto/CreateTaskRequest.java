package com.sprinttracker.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateTaskRequest(
        @NotBlank(message = "task title is required") String title,
        @Min(0) Integer storyPoints,
        Long assigneeId
) {
}
