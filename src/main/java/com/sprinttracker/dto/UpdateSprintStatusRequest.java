package com.sprinttracker.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateSprintStatusRequest(@NotBlank String status) {
}
