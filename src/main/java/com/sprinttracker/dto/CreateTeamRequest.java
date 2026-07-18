package com.sprinttracker.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateTeamRequest(@NotBlank(message = "team name is required") String name) {
}
