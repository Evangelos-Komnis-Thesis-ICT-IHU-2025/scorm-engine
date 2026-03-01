package com.ihu.scorm.engine.enrollment.persist;

import com.ihu.scorm.engine.common.constant.DetailKey;
import com.ihu.scorm.engine.common.error.ConflictException;
import com.ihu.scorm.engine.common.error.ErrorCode;
import com.ihu.scorm.engine.common.error.ErrorMessages;
import com.ihu.scorm.engine.enrollment.crud.EnrollmentCrudService;
import com.ihu.scorm.engine.enrollment.crud.EnrollmentEntity;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentPersistService {

  private final EnrollmentCrudService enrollmentCrudService;

  public EnrollmentPersistService(EnrollmentCrudService enrollmentCrudService) {
    this.enrollmentCrudService = enrollmentCrudService;
  }

  public EnrollmentEntity create(EnrollmentEntity enrollmentEntity) {
    if (enrollmentCrudService.exists(enrollmentEntity.getUserId(), enrollmentEntity.getCourseId())) {
      throw new ConflictException(
          ErrorCode.ENROLLMENT_EXISTS,
          ErrorMessages.ENROLLMENT_ALREADY_EXISTS,
          Map.of(
              DetailKey.USER_ID,
              enrollmentEntity.getUserId(),
              DetailKey.COURSE_ID,
              enrollmentEntity.getCourseId()));
    }
    return enrollmentCrudService.save(enrollmentEntity);
  }
}
