package org.example.managementplatformrasirom.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.example.managementplatformrasirom.model.ProjectStatus;

@Data
public class ProjectRequest {
    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    private ProjectStatus status;
}
