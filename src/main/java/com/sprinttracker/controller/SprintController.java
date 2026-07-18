package com.sprinttracker.controller;

import com.sprinttracker.security.CurrentUser;
import com.sprinttracker.dto.CreateSprintRequest;
import com.sprinttracker.dto.SprintResponse;
import com.sprinttracker.dto.UpdateSprintStatusRequest;
import com.sprinttracker.dto.VelocityPoint;
import com.sprinttracker.service.SprintService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SprintController {

    private final SprintService sprintService;
    private final CurrentUser currentUser;

    public SprintController(SprintService sprintService, CurrentUser currentUser) {
        this.sprintService = sprintService;
        this.currentUser = currentUser;
    }

    @PostMapping("/api/teams/{teamId}/sprints")
    public ResponseEntity<SprintResponse> create(@PathVariable Long teamId, @Valid @RequestBody CreateSprintRequest request) {
        return ResponseEntity.ok(sprintService.createSprint(currentUser.id(), teamId, request));
    }

    @GetMapping("/api/teams/{teamId}/sprints")
    public ResponseEntity<List<SprintResponse>> list(@PathVariable Long teamId) {
        return ResponseEntity.ok(sprintService.listSprints(currentUser.id(), teamId));
    }

    @PatchMapping("/api/sprints/{sprintId}/status")
    public ResponseEntity<SprintResponse> updateStatus(@PathVariable Long sprintId, @Valid @RequestBody UpdateSprintStatusRequest request) {
        return ResponseEntity.ok(sprintService.updateStatus(currentUser.id(), sprintId, request));
    }

    @GetMapping("/api/teams/{teamId}/velocity")
    public ResponseEntity<List<VelocityPoint>> velocity(@PathVariable Long teamId) {
        return ResponseEntity.ok(sprintService.getVelocity(currentUser.id(), teamId));
    }
}
