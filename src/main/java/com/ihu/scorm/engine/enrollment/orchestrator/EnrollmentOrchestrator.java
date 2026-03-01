package com.ihu.scorm.engine.enrollment.orchestrator;

import com.ihu.scorm.engine.common.query.PageResult;
import com.ihu.scorm.engine.course.persist.CourseReadService;
import com.ihu.scorm.engine.enrollment.crud.EnrollmentEntity;
import com.ihu.scorm.engine.enrollment.crud.EnrollmentStatus;
import com.ihu.scorm.engine.enrollment.persist.EnrollmentPersistService;
import com.ihu.scorm.engine.enrollment.persist.EnrollmentReadService;
import com.ihu.scorm.engine.enrollment.persist.GetEnrollmentsQuery;
import com.ihu.scorm.engine.user.persist.UserReadService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentOrchestrator {

  private final EnrollmentReadService enrollmentReadService;
  private final EnrollmentPersistService enrollmentPersistService;
  private final UserReadService userReadService;
  private final CourseReadService courseReadService;

  public EnrollmentOrchestrator(
      EnrollmentReadService enrollmentReadService,
      EnrollmentPersistService enrollmentPersistService,
      UserReadService userReadService,
      CourseReadService courseReadService) {
    this.enrollmentReadService = enrollmentReadService;
    this.enrollmentPersistService = enrollmentPersistService;
    this.userReadService = userReadService;
    this.courseReadService = courseReadService;
  }

  public EnrollmentEntity create(CreateEnrollmentCommand command) {
    userReadService.getRequired(command.userId());
    courseReadService.getRequired(command.courseId());

    EnrollmentEntity enrollment = new EnrollmentEntity();
    enrollment.setUserId(command.userId());
    enrollment.setCourseId(command.courseId());
    enrollment.setStatus(EnrollmentStatus.ACTIVE);
    return enrollmentPersistService.create(enrollment);
  }

  public PageResult<EnrollmentEntity> list(GetEnrollmentsQuery query) {
    Page<EnrollmentEntity> page = enrollmentReadService.list(query);
    return new PageResult<>(
        page.getContent(),
        page.getNumber(),
        page.getSize(),
        page.getTotalElements(),
        page.getTotalPages());
  }
}
