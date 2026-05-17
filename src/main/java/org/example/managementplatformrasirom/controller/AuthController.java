package org.example.managementplatformrasirom.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.managementplatformrasirom.dto.request.LoginRequest;
import org.example.managementplatformrasirom.dto.request.RegisterRequest;
import org.example.managementplatformrasirom.dto.response.ApiResponse;
import org.example.managementplatformrasirom.dto.response.AuthResponse;
import org.example.managementplatformrasirom.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(authService.register(request)));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(ApiResponse.success(authService.login(request)));
    }
}
