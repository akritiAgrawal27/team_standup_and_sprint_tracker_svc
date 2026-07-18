package com.sprinttracker.controller;

import com.sprinttracker.security.CurrentUser;
import com.sprinttracker.dto.PostStandupRequest;
import com.sprinttracker.dto.StandupResponse;
import com.sprinttracker.service.StandupService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/standups")
public class StandupController {

    private final StandupService standupService;
    private final CurrentUser currentUser;

    public StandupController(StandupService standupService, CurrentUser currentUser) {
        this.standupService = standupService;
        this.currentUser = currentUser;
    }

    @PostMapping
    public ResponseEntity<StandupResponse> postToday(@Valid @RequestBody PostStandupRequest request) {
        return ResponseEntity.ok(standupService.postToday(currentUser.id(), request));
    }

    @GetMapping
    public ResponseEntity<List<StandupResponse>> board(
            @RequestParam Long teamId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok(standupService.boardForDate(currentUser.id(), teamId, date));
    }

    @GetMapping("/mine")
    public ResponseEntity<List<StandupResponse>> mine(@RequestParam Long sprintId) {
        return ResponseEntity.ok(standupService.myHistory(currentUser.id(), sprintId));
    }
}
