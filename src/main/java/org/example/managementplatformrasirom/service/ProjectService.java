package org.example.managementplatformrasirom.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.managementplatformrasirom.dto.request.ProjectRequest;
import org.example.managementplatformrasirom.dto.response.ProjectResponse;
import org.example.managementplatformrasirom.dto.response.UserResponse;
import org.example.managementplatformrasirom.exception.BusinessException;
import org.example.managementplatformrasirom.model.Project;
import org.example.managementplatformrasirom.model.ProjectStatus;
import org.example.managementplatformrasirom.model.User;
import org.example.managementplatformrasirom.repository.ProjectRepository;
import org.example.managementplatformrasirom.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(ProjectService.class);

    public ProjectResponse createProject(ProjectRequest request, String ownerEmail) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new BusinessException("User not found", HttpStatus.NOT_FOUND));

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
                .orElseThrow(() -> new BusinessException("User not found", HttpStatus.NOT_FOUND));
        return projectRepository.findByMembersIdAndDeletedFalse(user.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ProjectResponse updateProject(Long id, ProjectRequest request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Project not found", HttpStatus.NOT_FOUND));

        project.setName(request.getName());
        project.setDescription(request.getDescription());

        if (request.getStatus() != null) {
            project.setStatus(request.getStatus());
        }

        projectRepository.save(project);

        log.info("UPDATED PROJECT: project '{}' with id '{}'", project.getName(), id);

        return mapToResponse(project);
    }

    public ProjectResponse updateStatus(Long id, ProjectStatus status) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Project not found", HttpStatus.NOT_FOUND));
        project.setStatus(status);
        projectRepository.save(project);

        log.info("UPDATED STATUS PROJECT: project '{}' with id '{}'", project.getName(), id);

        return mapToResponse(project);
    }

    @Transactional
    public void addMember(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BusinessException("Project not found", HttpStatus.NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("User not found", HttpStatus.NOT_FOUND));

        boolean alreadyMember = project.getMembers().stream()
                .anyMatch(member -> member.getId().equals(userId));

        if (alreadyMember) {
            throw new BusinessException("User is already a member of this project", HttpStatus.CONFLICT);
        }

        project.getMembers().add(user);
        projectRepository.save(project);
    }

    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Project not found", HttpStatus.NOT_FOUND));
        project.setDeleted(true);

        log.info("DELETE PROJECT: project '{}' with id '{}'", project.getName(), id);

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
