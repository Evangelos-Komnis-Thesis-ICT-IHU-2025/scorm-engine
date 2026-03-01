package com.ihu.scorm.engine.runtime.api;

import com.ihu.scorm.engine.common.error.ErrorCode;
import com.ihu.scorm.engine.common.error.ErrorMessages;
import com.ihu.scorm.engine.common.error.UnauthorizedException;
import com.ihu.scorm.engine.common.security.BearerTokenAuthenticationFilter;
import com.ihu.scorm.engine.common.token.LaunchTokenPayload;
import com.ihu.scorm.engine.runtime.orchestrator.RuntimeOrchestrator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/runtime")
public class RuntimeController {

  private final RuntimeOrchestrator runtimeOrchestrator;

  public RuntimeController(RuntimeOrchestrator runtimeOrchestrator) {
    this.runtimeOrchestrator = runtimeOrchestrator;
  }

  @PostMapping("/launches/{launchId}/commit")
  public ResponseEntity<RuntimeCommitResponse> commit(
      @PathVariable UUID launchId,
      @Valid @RequestBody RuntimeCommitForm form,
      HttpServletRequest request) {
    LaunchTokenPayload payload = requiredLaunchToken(request);
    return ResponseEntity.ok(runtimeOrchestrator.commit(launchId, payload, form));
  }

  private LaunchTokenPayload requiredLaunchToken(HttpServletRequest request) {
    Object attr = request.getAttribute(BearerTokenAuthenticationFilter.LAUNCH_PAYLOAD_ATTR);
    if (attr instanceof LaunchTokenPayload payload) {
      return payload;
    }
    throw new UnauthorizedException(
        ErrorCode.INVALID_LAUNCH_TOKEN,
        ErrorMessages.LAUNCH_TOKEN_IS_REQUIRED,
        Map.of());
  }
}
