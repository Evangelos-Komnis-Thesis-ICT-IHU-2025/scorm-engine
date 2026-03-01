package com.ihu.scorm.engine.reporting.api;

import com.ihu.scorm.engine.reporting.orchestrator.ReportingOrchestrator;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/courses")
public class ReportingController {

  private final ReportingOrchestrator reportingOrchestrator;

  public ReportingController(ReportingOrchestrator reportingOrchestrator) {
    this.reportingOrchestrator = reportingOrchestrator;
  }

  @GetMapping("/{courseId}/stats")
  public ResponseEntity<CourseStatsDto> getCourseStats(@PathVariable UUID courseId) {
    return ResponseEntity.ok(reportingOrchestrator.getCourseStats(courseId));
  }
}
