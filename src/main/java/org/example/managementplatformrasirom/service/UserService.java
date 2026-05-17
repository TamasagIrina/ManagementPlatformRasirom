package org.example.managementplatformrasirom.service;

import lombok.RequiredArgsConstructor;
import org.example.managementplatformrasirom.dto.request.RegisterRequest;
import org.example.managementplatformrasirom.dto.response.UserResponse;
import org.example.managementplatformrasirom.exception.BusinessException;
import org.example.managementplatformrasirom.model.User;
import org.example.managementplatformrasirom.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.example.managementplatformrasirom.model.Role;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final AuditService auditService;

    public UserResponse getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("User not found", HttpStatus.NOT_FOUND));
        return mapToResponse(user);
    }

    public UserResponse updateProfile(String email, RegisterRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("User not found", HttpStatus.NOT_FOUND));

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        userRepository.save(user);

        return mapToResponse(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public UserResponse updateRoles(Long userId, Set<Role> roles) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("User not found", HttpStatus.NOT_FOUND));
        user.setRoles(roles);
        userRepository.save(user);

        log.info("ROLE CHANGE: user id '{}' roles changed to '{}'", userId, roles);
        auditService.log("UPDATE_ROLES", "ADMIN", "USER", userId, "Roles changed to: " + roles);

        return mapToResponse(user);
    }

    public void deactivateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("User not found", HttpStatus.NOT_FOUND));
        user.setActive(false);

        log.info("USER DEACTIVATED: user id '{}'", userId);
        auditService.log("DEACTIVATE_USER", "ADMIN", "USER", userId, "User deactivated");

        userRepository.save(user);
    }

    public void activateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("User not found", HttpStatus.NOT_FOUND));
        user.setActive(true);

        log.info("USER ACTIVATED: user id '{}'", userId);
        auditService.log("ACTIVATE_USER", "ADMIN", "USER", userId, "User activated");

        userRepository.save(user);
    }

    public UserResponse mapToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setRoles(user.getRoles());
        response.setActive(user.isActive());
        response.setCreatedAt(user.getCreatedAt());
        return response;
    }
}
