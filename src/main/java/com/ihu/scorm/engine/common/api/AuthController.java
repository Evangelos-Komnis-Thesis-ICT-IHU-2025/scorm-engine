package com.ihu.scorm.engine.common.api;

import com.ihu.scorm.engine.common.token.JwtTokenService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

  private final JwtTokenService jwtTokenService;

  public AuthController(JwtTokenService jwtTokenService) {
    this.jwtTokenService = jwtTokenService;
  }

  @PostMapping("/dev-token")
  public DevTokenResponse createDevToken(@Valid @RequestBody DevTokenRequest request) {
    String token = jwtTokenService.createEngineToken(request.userId(), request.roles());
    return new DevTokenResponse(token);
  }

  public record DevTokenRequest(@NotNull UUID userId, @NotEmpty List<String> roles) {
  }

  public record DevTokenResponse(String token) {
  }
}
