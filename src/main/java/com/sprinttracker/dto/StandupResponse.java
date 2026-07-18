package com.sprinttracker.dto;

import com.sprinttracker.entity.StandupEntry;

import java.time.Instant;
import java.time.LocalDate;

public record StandupResponse(
        Long id, Long teamId, Long sprintId, Long userId, String userName,
        LocalDate entryDate, String yesterday, String today, String blockersText,
        boolean blocked, Instant updatedAt
) {
    public static StandupResponse from(StandupEntry entry, String userName) {
        return new StandupResponse(
                entry.getId(), entry.getTeamId(), entry.getSprintId(), entry.getUserId(), userName,
                entry.getEntryDate(), entry.getYesterday(), entry.getToday(), entry.getBlockersText(),
                entry.isBlocked(), entry.getUpdatedAt()
        );
    }
}
