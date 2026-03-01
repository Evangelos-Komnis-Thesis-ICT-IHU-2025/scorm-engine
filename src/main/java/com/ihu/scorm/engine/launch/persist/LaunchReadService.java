package com.ihu.scorm.engine.launch.persist;

import com.ihu.scorm.engine.common.constant.DetailKey;
import com.ihu.scorm.engine.common.error.ErrorCode;
import com.ihu.scorm.engine.common.error.ErrorMessages;
import com.ihu.scorm.engine.common.error.NotFoundException;
import com.ihu.scorm.engine.launch.crud.LaunchCrudService;
import com.ihu.scorm.engine.launch.crud.LaunchEntity;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class LaunchReadService {

  private final LaunchCrudService launchCrudService;

  public LaunchReadService(LaunchCrudService launchCrudService) {
    this.launchCrudService = launchCrudService;
  }

  public LaunchEntity getRequired(UUID launchId) {
    LaunchEntity entity = launchCrudService.findById(launchId);
    if (entity == null) {
      throw new NotFoundException(
          ErrorCode.LAUNCH_NOT_FOUND,
          ErrorMessages.LAUNCH_NOT_FOUND,
          Map.of(DetailKey.LAUNCH_ID, launchId));
    }
    return entity;
  }

  public List<LaunchEntity> staleActive(Instant threshold) {
    return launchCrudService.findStaleActive(threshold);
  }

  public LaunchEntity findLatestByAttemptId(UUID attemptId) {
    return launchCrudService.findLatestByAttemptId(attemptId);
  }
}
