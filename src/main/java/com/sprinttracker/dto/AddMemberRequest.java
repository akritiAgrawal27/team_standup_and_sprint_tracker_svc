package com.sprinttracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AddMemberRequest(@NotBlank @Email(message = "must be a valid email") String email) {
}
