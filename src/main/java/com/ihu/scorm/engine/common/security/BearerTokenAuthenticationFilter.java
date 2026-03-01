package com.ihu.scorm.engine.common.security;

import com.ihu.scorm.engine.common.constant.SecurityConstants;
import com.ihu.scorm.engine.common.token.EngineTokenPayload;
import com.ihu.scorm.engine.common.token.JwtTokenService;
import com.ihu.scorm.engine.common.token.LaunchTokenPayload;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class BearerTokenAuthenticationFilter extends OncePerRequestFilter {

  public static final String LAUNCH_PAYLOAD_ATTR = "launchTokenPayload";
  public static final String ENGINE_PAYLOAD_ATTR = "engineTokenPayload";

  private final JwtTokenService jwtTokenService;

  public BearerTokenAuthenticationFilter(JwtTokenService jwtTokenService) {
    this.jwtTokenService = jwtTokenService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String authHeader = request.getHeader(SecurityConstants.AUTHORIZATION_HEADER);
    if (authHeader != null && authHeader.startsWith(SecurityConstants.BEARER_PREFIX)) {
      String token = authHeader.substring(SecurityConstants.BEARER_PREFIX.length()).trim();
      if (!token.isBlank()) {
        authenticateLaunchTokenIfPossible(request, token)
            .or(() -> authenticateEngineTokenIfPossible(request, token));
      }
    }
    filterChain.doFilter(request, response);
  }

  private java.util.Optional<Boolean> authenticateLaunchTokenIfPossible(HttpServletRequest request, String token) {
    try {
      LaunchTokenPayload payload = jwtTokenService.parseLaunchToken(token);
      request.setAttribute(LAUNCH_PAYLOAD_ATTR, payload);
      UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
          payload.userId().toString(),
          null,
          List.of(
              new SimpleGrantedAuthority(SecurityRole.LAUNCH.authority()),
              new SimpleGrantedAuthority(SecurityRole.LEARNER.authority())));
      SecurityContextHolder.getContext().setAuthentication(authentication);
      return java.util.Optional.of(Boolean.TRUE);
    } catch (Exception ignored) {
      return java.util.Optional.empty();
    }
  }

  private java.util.Optional<Boolean> authenticateEngineTokenIfPossible(HttpServletRequest request, String token) {
    try {
      EngineTokenPayload payload = jwtTokenService.parseEngineToken(token);
      request.setAttribute(ENGINE_PAYLOAD_ATTR, payload);
      Collection<GrantedAuthority> authorities = new ArrayList<>();
      for (String role : payload.roles()) {
        if (role == null || role.isBlank()) {
          continue;
        }
        String normalized = SecurityRole.normalizeAuthority(role);
        authorities.add(new SimpleGrantedAuthority(normalized));
      }
      UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
          payload.userId().toString(),
          null,
          authorities);
      SecurityContextHolder.getContext().setAuthentication(authentication);
      return java.util.Optional.of(Boolean.TRUE);
    } catch (Exception ignored) {
      return java.util.Optional.empty();
    }
  }
}
