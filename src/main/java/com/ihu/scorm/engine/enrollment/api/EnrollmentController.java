package com.ihu.scorm.engine.enrollment.api;

import com.ihu.scorm.engine.common.query.BasePaginationQuery;
import com.ihu.scorm.engine.common.query.PageResult;
import com.ihu.scorm.engine.enrollment.mapper.EnrollmentApiMapper;
import com.ihu.scorm.engine.enrollment.orchestrator.EnrollmentOrchestrator;
import com.ihu.scorm.engine.enrollment.persist.EnrollmentEmbed;
import com.ihu.scorm.engine.enrollment.persist.GetEnrollmentsQuery;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.Instant;
import java.util.EnumSet;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/enrollments")
@Validated
public class EnrollmentController {

  private final EnrollmentOrchestrator enrollmentOrchestrator;
  private final EnrollmentApiMapper mapper;

  public EnrollmentController(EnrollmentOrchestrator enrollmentOrchestrator, EnrollmentApiMapper mapper) {
    this.enrollmentOrchestrator = enrollmentOrchestrator;
    this.mapper = mapper;
  }

  @PostMapping
  public ResponseEntity<EnrollmentDto> createEnrollment(@Valid @RequestBody CreateEnrollmentForm form) {
    EnrollmentDto dto = mapper.toDto(enrollmentOrchestrator.create(mapper.toCommand(form)));
    return ResponseEntity.status(HttpStatus.CREATED).body(dto);
  }

  @GetMapping
  public ResponseEntity<PageResult<EnrollmentDto>> listEnrollments(
      @RequestParam(defaultValue = "0") @Min(0) int page,
      @RequestParam(defaultValue = "20") @Min(1) @Max(200) int size,
      @RequestParam(required = false) UUID userId,
      @RequestParam(required = false) UUID courseId) {

    GetEnrollmentsQuery query = new GetEnrollmentsQuery(
        new BasePaginationQuery(page, size, "createdAt,desc"),
        userId,
        courseId,
        EnumSet.noneOf(EnrollmentEmbed.class),
        Instant.now());

    return ResponseEntity.ok(mapper.toPageResult(enrollmentOrchestrator.list(query)));
  }
}
