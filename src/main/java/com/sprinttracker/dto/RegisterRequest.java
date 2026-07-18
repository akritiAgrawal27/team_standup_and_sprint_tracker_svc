package com.sprinttracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "name is required") String name,
        @NotBlank @Email(message = "must be a valid email") String email,
        @NotBlank @Size(min = 8, message = "password must be at least 8 characters") String password
) {
}
