package com.ihu.scorm.engine.reporting.orchestrator;

import com.ihu.scorm.engine.reporting.api.CourseStatsDto;
import com.ihu.scorm.engine.reporting.persist.GetCourseStatsQuery;
import com.ihu.scorm.engine.reporting.persist.ReportingReadService;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ReportingOrchestrator {

  private final ReportingReadService reportingReadService;

  public ReportingOrchestrator(ReportingReadService reportingReadService) {
    this.reportingReadService = reportingReadService;
  }

  public CourseStatsDto getCourseStats(UUID courseId) {
    return reportingReadService.getCourseStats(new GetCourseStatsQuery(courseId));
  }
}
