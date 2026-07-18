package com.sprinttracker.service;

import com.sprinttracker.common.ResourceNotFoundException;
import com.sprinttracker.dto.PostStandupRequest;
import com.sprinttracker.dto.StandupResponse;
import com.sprinttracker.entity.StandupEntry;
import com.sprinttracker.entity.User;
import com.sprinttracker.repository.StandupEntryRepository;
import com.sprinttracker.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class StandupService {

    private final StandupEntryRepository standupEntryRepository;
    private final BlockerDetectionService blockerDetectionService;
    private final TeamService teamService;
    private final UserRepository userRepository;

    public StandupService(
            StandupEntryRepository standupEntryRepository,
            BlockerDetectionService blockerDetectionService,
            TeamService teamService,
            UserRepository userRepository
    ) {
        this.standupEntryRepository = standupEntryRepository;
        this.blockerDetectionService = blockerDetectionService;
        this.teamService = teamService;
        this.userRepository = userRepository;
    }

    @Transactional
    public StandupResponse postToday(Long currentUserId, PostStandupRequest request) {
        teamService.requireMember(request.teamId(), currentUserId);

        LocalDate today = LocalDate.now();
        StandupEntry entry = standupEntryRepository
                .findByUserIdAndSprintIdAndEntryDate(currentUserId, request.sprintId(), today)
                .orElseGet(() -> StandupEntry.builder()
                        .teamId(request.teamId())
                        .sprintId(request.sprintId())
                        .userId(currentUserId)
                        .entryDate(today)
                        .build());

        entry.setYesterday(request.yesterday());
        entry.setToday(request.today());
        entry.setBlockersText(request.blockersText());
        entry.setBlocked(blockerDetectionService.isBlocked(request.yesterday(), request.today(), request.blockersText()));
        entry.setUpdatedAt(java.time.Instant.now());

        entry = standupEntryRepository.save(entry);
        return toResponse(entry);
    }

    public List<StandupResponse> boardForDate(Long currentUserId, Long teamId, LocalDate date) {
        teamService.requireMember(teamId, currentUserId);
        LocalDate targetDate = date != null ? date : LocalDate.now();
        return standupEntryRepository.findByTeamIdAndEntryDateOrderByCreatedAtAsc(teamId, targetDate).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<StandupResponse> myHistory(Long currentUserId, Long sprintId) {
        return standupEntryRepository.findByUserIdAndSprintIdOrderByEntryDateDesc(currentUserId, sprintId).stream()
                .map(this::toResponse)
                .toList();
    }

    private StandupResponse toResponse(StandupEntry entry) {
        User user = userRepository.findById(entry.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return StandupResponse.from(entry, user.getName());
    }
}
