package com.ihu.scorm.engine.attempt.api;

import com.ihu.scorm.engine.attempt.mapper.AttemptApiMapper;
import com.ihu.scorm.engine.attempt.orchestrator.AttemptOrchestrator;
import com.ihu.scorm.engine.attempt.persist.AttemptEmbed;
import com.ihu.scorm.engine.attempt.persist.GetAttemptsQuery;
import com.ihu.scorm.engine.common.query.BasePaginationQuery;
import com.ihu.scorm.engine.common.query.PageResult;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.Instant;
import java.util.EnumSet;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/attempts")
@Validated
public class AttemptController {

  private final AttemptOrchestrator attemptOrchestrator;
  private final AttemptApiMapper mapper;

  public AttemptController(AttemptOrchestrator attemptOrchestrator, AttemptApiMapper mapper) {
    this.attemptOrchestrator = attemptOrchestrator;
    this.mapper = mapper;
  }

  @GetMapping
  public ResponseEntity<PageResult<AttemptDto>> listAttempts(
      @RequestParam(defaultValue = "0") @Min(0) int page,
      @RequestParam(defaultValue = "20") @Min(1) @Max(200) int size,
      @RequestParam(required = false) UUID userId,
      @RequestParam(required = false) UUID courseId) {

    GetAttemptsQuery query = new GetAttemptsQuery(
        new BasePaginationQuery(page, size, "createdAt,desc"),
        userId,
        courseId,
        EnumSet.noneOf(AttemptEmbed.class),
        Instant.now());

    return ResponseEntity.ok(mapper.toPageResult(attemptOrchestrator.list(query)));
  }

  @GetMapping("/{attemptId}/progress")
  public ResponseEntity<AttemptProgressDto> getProgress(@PathVariable UUID attemptId) {
    return ResponseEntity.ok(attemptOrchestrator.getProgress(attemptId));
  }
}
