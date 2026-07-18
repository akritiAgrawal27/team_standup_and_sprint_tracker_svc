package com.sprinttracker.controller;

import com.sprinttracker.security.CurrentUser;
import com.sprinttracker.dto.CreateTaskRequest;
import com.sprinttracker.dto.TaskResponse;
import com.sprinttracker.dto.UpdateTaskStatusRequest;
import com.sprinttracker.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TaskController {

    private final TaskService taskService;
    private final CurrentUser currentUser;

    public TaskController(TaskService taskService, CurrentUser currentUser) {
        this.taskService = taskService;
        this.currentUser = currentUser;
    }

    @PostMapping("/api/sprints/{sprintId}/tasks")
    public ResponseEntity<TaskResponse> create(@PathVariable Long sprintId, @Valid @RequestBody CreateTaskRequest request) {
        return ResponseEntity.ok(taskService.createTask(currentUser.id(), sprintId, request));
    }

    @GetMapping("/api/sprints/{sprintId}/tasks")
    public ResponseEntity<List<TaskResponse>> list(@PathVariable Long sprintId) {
        return ResponseEntity.ok(taskService.listTasks(currentUser.id(), sprintId));
    }

    @PatchMapping("/api/tasks/{taskId}/status")
    public ResponseEntity<TaskResponse> updateStatus(@PathVariable Long taskId, @Valid @RequestBody UpdateTaskStatusRequest request) {
        return ResponseEntity.ok(taskService.updateStatus(currentUser.id(), taskId, request));
    }
}
