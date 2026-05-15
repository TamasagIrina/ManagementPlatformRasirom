package org.example.managementplatformrasirom.dto.response;

import lombok.Data;
import org.example.managementplatformrasirom.model.Role;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class UserResponse {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Set<Role> roles;
    private boolean active;
    private LocalDateTime createdAt;
}
