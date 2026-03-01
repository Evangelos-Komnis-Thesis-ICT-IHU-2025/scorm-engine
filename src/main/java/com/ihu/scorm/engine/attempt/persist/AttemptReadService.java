package com.ihu.scorm.engine.attempt.persist;

import com.ihu.scorm.engine.attempt.crud.AttemptCrudService;
import com.ihu.scorm.engine.attempt.crud.AttemptEntity;
import com.ihu.scorm.engine.common.constant.DetailKey;
import com.ihu.scorm.engine.common.error.ErrorCode;
import com.ihu.scorm.engine.common.error.ErrorMessages;
import com.ihu.scorm.engine.common.error.NotFoundException;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class AttemptReadService {

  private final AttemptCrudService attemptCrudService;

  public AttemptReadService(AttemptCrudService attemptCrudService) {
    this.attemptCrudService = attemptCrudService;
  }

  public AttemptEntity getRequired(UUID attemptId) {
    AttemptEntity entity = attemptCrudService.findById(attemptId);
    if (entity == null) {
      throw new NotFoundException(
          ErrorCode.ATTEMPT_NOT_FOUND,
          ErrorMessages.ATTEMPT_NOT_FOUND,
          Map.of(DetailKey.ATTEMPT_ID, attemptId));
    }
    return entity;
  }

  public AttemptEntity findActive(UUID enrollmentId) {
    return attemptCrudService.findActiveByEnrollmentId(enrollmentId);
  }

  public int nextAttemptNo(UUID enrollmentId) {
    return attemptCrudService.findLastAttemptNo(enrollmentId) + 1;
  }

  public Page<AttemptEntity> list(GetAttemptsQuery query) {
    return attemptCrudService.list(query);
  }
}
