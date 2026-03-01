package com.ihu.scorm.engine.launch.api;

import com.ihu.scorm.engine.common.constant.DetailKey;
import com.ihu.scorm.engine.common.error.ErrorCode;
import com.ihu.scorm.engine.common.error.ErrorMessages;
import com.ihu.scorm.engine.common.error.UnauthorizedException;
import com.ihu.scorm.engine.common.security.BearerTokenAuthenticationFilter;
import com.ihu.scorm.engine.common.token.LaunchTokenPayload;
import com.ihu.scorm.engine.launch.LaunchConstants;
import com.ihu.scorm.engine.launch.mapper.LaunchApiMapper;
import com.ihu.scorm.engine.launch.orchestrator.CreateLaunchCommand;
import com.ihu.scorm.engine.launch.orchestrator.LaunchOrchestrator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/launches")
public class LaunchController {

  private final LaunchOrchestrator launchOrchestrator;
  private final LaunchApiMapper mapper;

  public LaunchController(LaunchOrchestrator launchOrchestrator, LaunchApiMapper mapper) {
    this.launchOrchestrator = launchOrchestrator;
    this.mapper = mapper;
  }

  @PostMapping
  public ResponseEntity<LaunchCreatedDto> createLaunch(@Valid @RequestBody CreateLaunchForm form) {
    CreateLaunchCommand command = mapper.toCommand(form);
    return ResponseEntity.status(HttpStatus.CREATED).body(launchOrchestrator.createLaunch(command));
  }

  @GetMapping("/{launchId}")
  public ResponseEntity<LaunchContextDto> getLaunchContext(@PathVariable UUID launchId, HttpServletRequest request) {
    LaunchTokenPayload payload = requiredLaunchToken(request);
    return ResponseEntity.ok(launchOrchestrator.getLaunchContext(launchId, payload));
  }

  @PostMapping("/{launchId}/terminate")
  public ResponseEntity<Map<String, Object>> terminate(@PathVariable UUID launchId, HttpServletRequest request) {
    LaunchTokenPayload payload = requiredLaunchToken(request);
    launchOrchestrator.terminate(launchId, payload);
    return ResponseEntity.ok(Map.of(DetailKey.LAUNCH_ID, launchId, DetailKey.STATUS, LaunchConstants.TERMINATED_STATUS));
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
