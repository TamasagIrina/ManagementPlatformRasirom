package org.example.managementplatformrasirom.service;

import lombok.RequiredArgsConstructor;
import org.example.managementplatformrasirom.dto.request.TaskRequest;
import org.example.managementplatformrasirom.dto.response.TaskResponse;
import org.example.managementplatformrasirom.model.*;
import org.example.managementplatformrasirom.repository.ProjectRepository;
import org.example.managementplatformrasirom.repository.TaskRepository;
import org.example.managementplatformrasirom.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {


    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public TaskResponse createTask(TaskRequest request, String creatorEmail) {
        User creator = userRepository.findByEmail(creatorEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority());
        task.setStatus(TaskStatus.TODO);
        task.setDeadline(request.getDeadline());
        task.setCreatedBy(creator);
        task.setProject(project);

        if (request.getAssignedToId() != null) {
            User assignedTo = userRepository.findById(request.getAssignedToId())
                    .orElseThrow(() -> new RuntimeException("Assigned user not found"));

            boolean assignedIsOwner = project.getOwner().getId().equals(assignedTo.getId());
            boolean assignedIsMember = project.getMembers().contains(assignedTo);

            if (!assignedIsOwner && !assignedIsMember) {
                throw new RuntimeException("Assigned user is not a member of this project");
            }

            task.setAssignedTo(assignedTo);
        }

        taskRepository.save(task);
        return mapToResponse(task);
    }

    public TaskResponse assignTask(Long id, Long userId) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Project project = task.getProject();

        boolean isOwner = project.getOwner().getId().equals(user.getId());
        boolean isMember = project.getMembers().contains(user);
        if (!isOwner && !isMember) {
            throw new RuntimeException("User is not a member of this project");
        }

        task.setAssignedTo(user);
        taskRepository.save(task);
        return mapToResponse(task);
    }

    public List<TaskResponse> getAllTasks() {
        return taskRepository.findByDeletedFalse()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<TaskResponse> getTasksByStatus(TaskStatus status) {
        return taskRepository.findByStatusAndDeletedFalse(status)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<TaskResponse> getTasksByPriority(TaskPriority priority) {
        return taskRepository.findByPriorityAndDeletedFalse(priority)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<TaskResponse> getMyTasks(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return taskRepository.findByAssignedToIdAndDeletedFalse(user.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public TaskResponse updateStatus(Long id, TaskStatus status) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setStatus(status);
        taskRepository.save(task);
        return mapToResponse(task);
    }



    public TaskResponse updateTask(Long id, TaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority());
        task.setDeadline(request.getDeadline());

        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }

        if (request.getAssignedToId() != null) {
            User assignedTo = userRepository.findById(request.getAssignedToId())
                    .orElseThrow(() -> new RuntimeException("Assigned user not found"));
            task.setAssignedTo(assignedTo);
        }

        taskRepository.save(task);
        return mapToResponse(task);
    }

    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setDeleted(true);
        taskRepository.save(task);
    }

    public TaskResponse mapToResponse(Task task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setPriority(task.getPriority());
        response.setStatus(task.getStatus());
        response.setDeadline(task.getDeadline());
        response.setCreatedAt(task.getCreatedAt());
        response.setUpdatedAt(task.getUpdatedAt());
        response.setProjectName(task.getProject().getName());

        if (task.getAssignedTo() != null) {
            response.setAssignedToName(task.getAssignedTo().getFirstName() + " "
                    + task.getAssignedTo().getLastName());
        }

        response.setCreatedByName(task.getCreatedBy().getFirstName() + " "
                + task.getCreatedBy().getLastName());

        return response;
    }
}
