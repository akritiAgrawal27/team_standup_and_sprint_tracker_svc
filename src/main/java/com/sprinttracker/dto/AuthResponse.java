package com.sprinttracker.dto;

public record AuthResponse(String token, UserSummary user) {
}
