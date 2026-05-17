package org.example.managementplatformrasirom.service;


import lombok.RequiredArgsConstructor;
import org.example.managementplatformrasirom.dto.request.LoginRequest;
import org.example.managementplatformrasirom.dto.request.RegisterRequest;
import org.example.managementplatformrasirom.dto.response.AuthResponse;
import org.example.managementplatformrasirom.exception.BusinessException;
import org.example.managementplatformrasirom.model.Role;
import org.example.managementplatformrasirom.model.User;
import org.example.managementplatformrasirom.repository.UserRepository;
import org.example.managementplatformrasirom.security.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    private final AuditService auditService;

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email already in use", HttpStatus.CONFLICT);
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRoles(Set.of(Role.USER));
        user.setActive(true);

        userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtService.generateToken(userDetails);

        log.info("REGISTER SUCCESS: user '{}'", request.getEmail());
        auditService.log("REGISTER", request.getEmail(), "USER", null, "Register successful");

        return new AuthResponse(token, user.getEmail(), user.getFirstName());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtService.generateToken(userDetails);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->  new BusinessException("User not found", HttpStatus.NOT_FOUND));

        log.info("LOGIN SUCCESS: user '{}'", request.getEmail());
        auditService.log("LOGIN", request.getEmail(), "USER", null, "Login successful");

        return new AuthResponse(token, user.getEmail(), user.getFirstName());
    }
}
