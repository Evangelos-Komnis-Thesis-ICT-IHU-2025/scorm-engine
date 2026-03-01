package com.ihu.scorm.engine.enrollment.persist;

import com.ihu.scorm.engine.common.constant.DetailKey;
import com.ihu.scorm.engine.common.error.ErrorCode;
import com.ihu.scorm.engine.common.error.ErrorMessages;
import com.ihu.scorm.engine.common.error.NotFoundException;
import com.ihu.scorm.engine.enrollment.crud.EnrollmentCrudService;
import com.ihu.scorm.engine.enrollment.crud.EnrollmentEntity;
import com.ihu.scorm.engine.enrollment.crud.EnrollmentStatus;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentReadService {

  private final EnrollmentCrudService enrollmentCrudService;

  public EnrollmentReadService(EnrollmentCrudService enrollmentCrudService) {
    this.enrollmentCrudService = enrollmentCrudService;
  }

  public EnrollmentEntity getActiveRequired(UUID userId, UUID courseId) {
    EnrollmentEntity enrollment = enrollmentCrudService.findByUserCourse(userId, courseId);
    if (enrollment == null || enrollment.getStatus() != EnrollmentStatus.ACTIVE) {
      throw new NotFoundException(
          ErrorCode.ENROLLMENT_NOT_FOUND,
          ErrorMessages.ACTIVE_ENROLLMENT_NOT_FOUND,
          Map.of(DetailKey.USER_ID, userId, DetailKey.COURSE_ID, courseId));
    }
    return enrollment;
  }

  public EnrollmentEntity findByUserCourse(UUID userId, UUID courseId) {
    return enrollmentCrudService.findByUserCourse(userId, courseId);
  }

  public Page<EnrollmentEntity> list(GetEnrollmentsQuery query) {
    return enrollmentCrudService.findPage(query);
  }
}
