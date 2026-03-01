package com.ihu.scorm.engine.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ihu.scorm.engine.common.api.ApiError;
import com.ihu.scorm.engine.common.api.ApiErrorResponse;
import com.ihu.scorm.engine.common.error.ErrorCode;
import com.ihu.scorm.engine.common.error.ErrorMessages;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

  private final BearerTokenAuthenticationFilter bearerTokenAuthenticationFilter;
  private final ObjectMapper objectMapper;

  public SecurityConfig(BearerTokenAuthenticationFilter bearerTokenAuthenticationFilter, ObjectMapper objectMapper) {
    this.bearerTokenAuthenticationFilter = bearerTokenAuthenticationFilter;
    this.objectMapper = objectMapper;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .httpBasic(httpBasic -> httpBasic.disable())
        .formLogin(form -> form.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .exceptionHandling(ex -> ex
            .authenticationEntryPoint((request, response, authException) ->
                writeError(
                    response,
                    HttpServletResponse.SC_UNAUTHORIZED,
                    ErrorCode.UNAUTHENTICATED.value(),
                    ErrorMessages.AUTHENTICATION_REQUIRED))
            .accessDeniedHandler((request, response, accessDeniedException) ->
                writeError(
                    response,
                    HttpServletResponse.SC_FORBIDDEN,
                    ErrorCode.FORBIDDEN.value(),
                    ErrorMessages.ACCESS_DENIED)))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/v1/health", "/actuator/health", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
            .requestMatchers("/api/v1/auth/dev-token").permitAll()
            .requestMatchers("/api/v1/runtime/**").hasRole(SecurityRole.LAUNCH.roleName())
            .requestMatchers("/api/v1/launches/*/terminate").hasRole(SecurityRole.LAUNCH.roleName())
            .requestMatchers("/api/v1/launches/*").hasRole(SecurityRole.LAUNCH.roleName())
            .requestMatchers("/api/v1/courses/import").hasRole(SecurityRole.ADMIN.roleName())
            .requestMatchers("/api/v1/users/**").hasRole(SecurityRole.ADMIN.roleName())
            .requestMatchers("/api/v1/enrollments/**")
            .hasAnyRole(SecurityRole.ADMIN.roleName(), SecurityRole.INSTRUCTOR.roleName())
            .requestMatchers("/api/v1/courses/*/stats")
            .hasAnyRole(SecurityRole.ADMIN.roleName(), SecurityRole.INSTRUCTOR.roleName())
            .anyRequest().authenticated())
        .addFilterBefore(bearerTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  private void writeError(HttpServletResponse response, int status, String code, String message) throws IOException {
    response.setStatus(status);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    objectMapper.writeValue(response.getWriter(), new ApiErrorResponse(new ApiError(code, message, java.util.Map.of())));
  }
}
