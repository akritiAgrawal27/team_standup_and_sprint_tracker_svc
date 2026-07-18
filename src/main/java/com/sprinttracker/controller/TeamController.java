package com.sprinttracker.controller;

import com.sprinttracker.security.CurrentUser;
import com.sprinttracker.dto.AddMemberRequest;
import com.sprinttracker.dto.CreateTeamRequest;
import com.sprinttracker.dto.TeamMemberResponse;
import com.sprinttracker.dto.TeamResponse;
import com.sprinttracker.service.TeamService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;
    private final CurrentUser currentUser;

    public TeamController(TeamService teamService, CurrentUser currentUser) {
        this.teamService = teamService;
        this.currentUser = currentUser;
    }

    @PostMapping
    public ResponseEntity<TeamResponse> create(@Valid @RequestBody CreateTeamRequest request) {
        return ResponseEntity.ok(teamService.createTeam(currentUser.id(), request));
    }

    @GetMapping
    public ResponseEntity<List<TeamResponse>> myTeams() {
        return ResponseEntity.ok(teamService.myTeams(currentUser.id()));
    }

    @GetMapping("/{teamId}/members")
    public ResponseEntity<List<TeamMemberResponse>> members(@PathVariable Long teamId) {
        return ResponseEntity.ok(teamService.listMembers(currentUser.id(), teamId));
    }

    @PostMapping("/{teamId}/members")
    public ResponseEntity<TeamMemberResponse> addMember(@PathVariable Long teamId, @Valid @RequestBody AddMemberRequest request) {
        return ResponseEntity.ok(teamService.addMember(currentUser.id(), teamId, request));
    }
}
