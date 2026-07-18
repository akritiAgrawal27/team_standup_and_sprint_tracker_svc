package com.sprinttracker.dto;

public record TeamMemberResponse(Long userId, String name, String email, String role) {
}
