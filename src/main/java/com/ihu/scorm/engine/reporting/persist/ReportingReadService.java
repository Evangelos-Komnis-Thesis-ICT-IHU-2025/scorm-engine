package com.ihu.scorm.engine.reporting.persist;

import com.ihu.scorm.engine.attempt.crud.AttemptCrudService;
import com.ihu.scorm.engine.course.persist.CourseReadService;
import com.ihu.scorm.engine.enrollment.crud.EnrollmentCrudService;
import com.ihu.scorm.engine.reporting.api.CourseStatsDto;
import org.springframework.stereotype.Service;

@Service
public class ReportingReadService {

  private final CourseReadService courseReadService;
  private final EnrollmentCrudService enrollmentCrudService;
  private final AttemptCrudService attemptCrudService;

  public ReportingReadService(
      CourseReadService courseReadService,
      EnrollmentCrudService enrollmentCrudService,
      AttemptCrudService attemptCrudService) {
    this.courseReadService = courseReadService;
    this.enrollmentCrudService = enrollmentCrudService;
    this.attemptCrudService = attemptCrudService;
  }

  public CourseStatsDto getCourseStats(GetCourseStatsQuery query) {
    courseReadService.getRequired(query.courseId());

    long enrolled = enrollmentCrudService.countByCourseId(query.courseId());
    long started = attemptCrudService.countByCourseId(query.courseId());
    long completed = attemptCrudService.countCompletedByCourseId(query.courseId());
    long active = attemptCrudService.countInProgressByCourseId(query.courseId());
    double avgTime = attemptCrudService.averageTotalTimeSeconds(query.courseId()) == null
        ? 0.0
        : attemptCrudService.averageTotalTimeSeconds(query.courseId());
    double avgScore = attemptCrudService.averageScoreRaw(query.courseId()) == null
        ? 0.0
        : attemptCrudService.averageScoreRaw(query.courseId());

    return new CourseStatsDto(query.courseId(), enrolled, started, completed, active, avgTime, avgScore);
  }
}
