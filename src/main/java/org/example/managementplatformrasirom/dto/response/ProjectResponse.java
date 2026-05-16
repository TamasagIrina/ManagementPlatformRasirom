package org.example.managementplatformrasirom.dto.response;

import lombok.Data;
import org.example.managementplatformrasirom.model.ProjectStatus;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class ProjectResponse {
    private Long id;
    private String name;
    private String description;
    private String ownerName;
    private ProjectStatus status;
    private LocalDateTime createdAt;
    private Set<UserResponse> members;
}
