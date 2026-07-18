package com.sprinttracker.dto;

import com.sprinttracker.entity.Team;

import java.time.Instant;

public record TeamResponse(Long id, String name, Long createdBy, Instant createdAt, String myRole) {
    public static TeamResponse from(Team team, String myRole) {
        return new TeamResponse(team.getId(), team.getName(), team.getCreatedBy(), team.getCreatedAt(), myRole);
    }
}
