package org.example.managementplatformrasirom.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.managementplatformrasirom.model.TaskPriority;
import org.example.managementplatformrasirom.model.TaskStatus;

import java.time.LocalDateTime;

@Data
public class TaskRequest {
    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Priority is required")
    private TaskPriority priority;

    private TaskStatus status;

    private LocalDateTime deadline;

    private Long assignedToId;

    @NotNull(message = "Project is required")
    private Long projectId;
}
