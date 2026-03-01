package com.ihu.scorm.engine.common.token;

import com.ihu.scorm.engine.common.config.JwtProperties;
import com.ihu.scorm.engine.common.constant.SystemMessages;
import com.ihu.scorm.engine.common.constant.TokenClaims;
import com.ihu.scorm.engine.common.infra.ClockProvider;
import com.ihu.scorm.engine.common.infra.IdGenerator;
import com.ihu.scorm.engine.standard.LearningStandard;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenService {

  private final JwtProperties jwtProperties;
  private final ClockProvider clockProvider;
  private final IdGenerator idGenerator;

  public JwtTokenService(JwtProperties jwtProperties, ClockProvider clockProvider, IdGenerator idGenerator) {
    this.jwtProperties = jwtProperties;
    this.clockProvider = clockProvider;
    this.idGenerator = idGenerator;
  }

  public String createLaunchToken(UUID launchId, UUID attemptId, UUID userId, UUID courseId, LearningStandard standard) {
    Instant now = clockProvider.now();
    Instant expiresAt = now.plus(jwtProperties.launchTtlMinutesOrDefault(), ChronoUnit.MINUTES);
    return Jwts.builder()
        .subject(userId.toString())
        .id(idGenerator.newUuid().toString())
        .claim(TokenClaims.LAUNCH_ID, launchId.toString())
        .claim(TokenClaims.ATTEMPT_ID, attemptId.toString())
        .claim(TokenClaims.COURSE_ID, courseId.toString())
        .claim(TokenClaims.STANDARD, standard.name())
        .claim(TokenClaims.TOKEN_TYPE, TokenClaims.TOKEN_TYPE_LAUNCH)
        .issuedAt(Date.from(now))
        .expiration(Date.from(expiresAt))
        .signWith(launchSigningKey())
        .compact();
  }

  public LaunchTokenPayload parseLaunchToken(String token) {
    Claims claims = parseClaims(token, launchSigningKey());
    return new LaunchTokenPayload(
        UUID.fromString((String) claims.get(TokenClaims.LAUNCH_ID)),
        UUID.fromString((String) claims.get(TokenClaims.ATTEMPT_ID)),
        UUID.fromString(claims.getSubject()),
        UUID.fromString((String) claims.get(TokenClaims.COURSE_ID)),
        LearningStandard.valueOf((String) claims.get(TokenClaims.STANDARD)),
        claims.getExpiration().toInstant());
  }

  public EngineTokenPayload parseEngineToken(String token) {
    Claims claims = parseClaims(token, engineSigningKey());
    Object rawRoles = claims.get(TokenClaims.ROLES);
    List<String> roles;
    if (rawRoles instanceof List<?> list) {
      roles = list.stream().map(String::valueOf).toList();
    } else if (rawRoles instanceof String roleString) {
      roles = List.of(roleString);
    } else {
      roles = List.of();
    }
    return new EngineTokenPayload(UUID.fromString(claims.getSubject()), roles);
  }

  public String createEngineToken(UUID userId, List<String> roles) {
    Instant now = clockProvider.now();
    Instant expiresAt = now.plus(jwtProperties.engineTtlMinutesOrDefault(), ChronoUnit.MINUTES);
    return Jwts.builder()
        .subject(userId.toString())
        .id(idGenerator.newUuid().toString())
        .claim(TokenClaims.ROLES, roles)
        .claim(TokenClaims.TOKEN_TYPE, TokenClaims.TOKEN_TYPE_ENGINE)
        .issuedAt(Date.from(now))
        .expiration(Date.from(expiresAt))
        .signWith(engineSigningKey())
        .compact();
  }

  private Claims parseClaims(String token, SecretKey key) {
    try {
      return Jwts.parser()
          .verifyWith(key)
          .build()
          .parseSignedClaims(token)
          .getPayload();
    } catch (JwtException exception) {
      throw new IllegalArgumentException(SystemMessages.INVALID_TOKEN, exception);
    }
  }

  private SecretKey launchSigningKey() {
    return Keys.hmacShaKeyFor(jwtProperties.launchSecret().getBytes(StandardCharsets.UTF_8));
  }

  private SecretKey engineSigningKey() {
    return Keys.hmacShaKeyFor(jwtProperties.engineSecret().getBytes(StandardCharsets.UTF_8));
  }
}
