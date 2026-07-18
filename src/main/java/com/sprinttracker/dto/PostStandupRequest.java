package com.sprinttracker.dto;

import jakarta.validation.constraints.NotNull;

public record PostStandupRequest(
        @NotNull Long teamId,
        @NotNull Long sprintId,
        String yesterday,
        String today,
        String blockersText
) {
}
