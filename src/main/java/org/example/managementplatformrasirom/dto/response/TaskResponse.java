package org.example.managementplatformrasirom.dto.response;

import lombok.Data;
import org.example.managementplatformrasirom.model.TaskPriority;
import org.example.managementplatformrasirom.model.TaskStatus;

import java.time.LocalDateTime;

@Data
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private TaskPriority priority;
    private TaskStatus status;
    private LocalDateTime deadline;
    private String assignedToName;
    private String createdByName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String projectName;
}
