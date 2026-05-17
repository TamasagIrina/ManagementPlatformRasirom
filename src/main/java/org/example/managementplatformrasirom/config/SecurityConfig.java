package org.example.managementplatformrasirom.config;


import lombok.RequiredArgsConstructor;
import org.example.managementplatformrasirom.security.JwtAuthFilter;
import org.example.managementplatformrasirom.security.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()

                        // USER endpoints
                        .requestMatchers(HttpMethod.GET, "/api/users/me").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/users/me").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/users/get/all").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/*/roles").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/*/deactivate").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/*/activate").hasRole("ADMIN")

                        // PROJECT endpoints
                        .requestMatchers(HttpMethod.GET, "/api/projects/my").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/projects/create").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/projects/all").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/projects/*/update").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/projects/*/status").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/projects/*/delete").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/projects/*/members/*/add").hasRole("ADMIN")

                        // TASK endpoints
                        .requestMatchers(HttpMethod.POST, "/api/tasks/create").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/tasks/all").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/tasks/my").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/tasks/filter/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/tasks/*/update").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/tasks/*/status").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/tasks/*/assign/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/tasks/*/delete").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
