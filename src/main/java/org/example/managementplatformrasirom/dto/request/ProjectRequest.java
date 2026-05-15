package org.example.managementplatformrasirom.dto.request;

import jakarta.validation.constraints.NotBlank;

public class ProjectRequest {
    @NotBlank(message = "Name is required")
    private String name;

    private String description;
}
