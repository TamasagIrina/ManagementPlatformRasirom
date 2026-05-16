package org.example.managementplatformrasirom.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.managementplatformrasirom.dto.request.TaskRequest;
import org.example.managementplatformrasirom.dto.response.TaskResponse;
import org.example.managementplatformrasirom.model.TaskPriority;
import org.example.managementplatformrasirom.model.TaskStatus;
import org.example.managementplatformrasirom.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/create")
    public ResponseEntity<TaskResponse> createTask(
            @Valid @RequestBody TaskRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskService.createTask(request, userDetails.getUsername()));
    }

    @GetMapping("/all")
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/my")
    public ResponseEntity<List<TaskResponse>> getMyTasks(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(taskService.getMyTasks(userDetails.getUsername()));
    }

    @GetMapping("/filter/status")
    public ResponseEntity<List<TaskResponse>> getByStatus(
            @RequestParam TaskStatus status) {
        return ResponseEntity.ok(taskService.getTasksByStatus(status));
    }

    @GetMapping("/filter/priority")
    public ResponseEntity<List<TaskResponse>> getByPriority(
            @RequestParam TaskPriority priority) {
        return ResponseEntity.ok(taskService.getTasksByPriority(priority));
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest request) {
        return ResponseEntity.ok(taskService.updateTask(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam TaskStatus status) {
        return ResponseEntity.ok(taskService.updateStatus(id, status));
    }

    @PatchMapping("/{id}/assign/{userId}")
    public ResponseEntity<TaskResponse> assignTask(
            @PathVariable Long id,
            @PathVariable Long userId) {
        return ResponseEntity.ok(taskService.assignTask(id, userId));
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
