package org.example.managementplatformrasirom.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.managementplatformrasirom.dto.request.ProjectRequest;
import org.example.managementplatformrasirom.dto.response.ApiResponse;
import org.example.managementplatformrasirom.dto.response.ProjectResponse;
import org.example.managementplatformrasirom.model.ProjectStatus;
import org.example.managementplatformrasirom.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ProjectResponse>> createProject(
            @Valid @RequestBody ProjectRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(projectService.createProject(request, userDetails.getUsername())));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> getAllProjects() {
        return ResponseEntity.ok(ApiResponse.success(projectService.getAllProjects()));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> getMyProjects(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success(projectService.getMyProjects(userDetails.getUsername())));
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<ApiResponse<ProjectResponse>> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectRequest request) {
        return ResponseEntity.ok(ApiResponse.success(projectService.updateProject(id, request)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<ProjectResponse>> updateStatus(
            @PathVariable Long id,
            @RequestParam ProjectStatus status) {
        return ResponseEntity.ok(ApiResponse.success(projectService.updateStatus(id, status)));
    }

    @PostMapping("/{id}/members/{userId}/add")
    public ResponseEntity<ApiResponse<Void>> addMember(
            @PathVariable Long id,
            @PathVariable Long userId) {
        projectService.addMember(id, userId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ApiResponse<Void>> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
