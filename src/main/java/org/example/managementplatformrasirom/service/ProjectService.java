package org.example.managementplatformrasirom.service;

import lombok.RequiredArgsConstructor;
import org.example.managementplatformrasirom.dto.request.ProjectRequest;
import org.example.managementplatformrasirom.dto.response.ProjectResponse;
import org.example.managementplatformrasirom.dto.response.UserResponse;
import org.example.managementplatformrasirom.model.Project;
import org.example.managementplatformrasirom.model.ProjectStatus;
import org.example.managementplatformrasirom.model.User;
import org.example.managementplatformrasirom.repository.ProjectRepository;
import org.example.managementplatformrasirom.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectResponse createProject(ProjectRequest request, String ownerEmail) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setOwner(owner);
        project.setStatus(ProjectStatus.ACTIVE);

        projectRepository.save(project);
        return mapToResponse(project);
    }

    public List<ProjectResponse> getAllProjects() {
        return projectRepository.findByDeletedFalse()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ProjectResponse> getMyProjects(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return projectRepository.findByMembersIdAndDeletedFalse(user.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ProjectResponse updateProject(Long id, ProjectRequest request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        project.setName(request.getName());
        project.setDescription(request.getDescription());

        if (request.getStatus() != null) {
            project.setStatus(request.getStatus());
        }

        projectRepository.save(project);
        return mapToResponse(project);
    }

    public ProjectResponse updateStatus(Long id, ProjectStatus status) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        project.setStatus(status);
        projectRepository.save(project);
        return mapToResponse(project);
    }

    public void addMember(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        project.getMembers().add(user);
        projectRepository.save(project);
    }

    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        project.setDeleted(true);
        projectRepository.save(project);
    }

    public ProjectResponse mapToResponse(Project project) {
        ProjectResponse response = new ProjectResponse();
        response.setId(project.getId());
        response.setName(project.getName());
        response.setDescription(project.getDescription());
        response.setOwnerName(project.getOwner().getFirstName() + " " + project.getOwner().getLastName());
        response.setStatus(project.getStatus());
        response.setCreatedAt(project.getCreatedAt());
        response.setMembers(project.getMembers()
                .stream()
                .map(this::mapUserToResponse)
                .collect(Collectors.toSet()));
        return response;
    }

    private UserResponse mapUserToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setRoles(user.getRoles());
        response.setActive(user.isActive());
        return response;
    }
}
