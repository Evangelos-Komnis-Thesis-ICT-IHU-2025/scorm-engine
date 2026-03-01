package com.ihu.scorm.engine.launch.persist;

import com.ihu.scorm.engine.common.constant.SystemMessages;
import com.ihu.scorm.engine.launch.crud.LaunchCrudService;
import com.ihu.scorm.engine.launch.crud.LaunchEntity;
import com.ihu.scorm.engine.launch.crud.LaunchStatus;
import java.time.Instant;
import org.springframework.stereotype.Service;

@Service
public class LaunchPersistService {

  private final LaunchCrudService launchCrudService;

  public LaunchPersistService(LaunchCrudService launchCrudService) {
    this.launchCrudService = launchCrudService;
  }

  public LaunchEntity save(LaunchEntity launchEntity) {
    return launchCrudService.save(launchEntity);
  }

  public LaunchEntity create(LaunchEntity launchEntity) {
    LaunchStatus currentStatus = launchEntity.getStatus();
    if (currentStatus != null && !currentStatus.canTransitionTo(LaunchStatus.ACTIVE)) {
      throw new IllegalStateException(
          SystemMessages.INVALID_LAUNCH_STATUS_TRANSITION + " from " + currentStatus + " to " + LaunchStatus.ACTIVE);
    }
    launchEntity.setStatus(LaunchStatus.ACTIVE);
    return launchCrudService.save(launchEntity);
  }

  public void close(LaunchEntity launchEntity, Instant now) {
    LaunchStatus currentStatus = launchEntity.getStatus();
    if (currentStatus != null && !currentStatus.canTransitionTo(LaunchStatus.CLOSED)) {
      throw new IllegalStateException(
          SystemMessages.INVALID_LAUNCH_STATUS_TRANSITION + " from " + currentStatus + " to " + LaunchStatus.CLOSED);
    }
    launchEntity.setStatus(LaunchStatus.CLOSED);
    launchEntity.setLastSeenAt(now);
    launchCrudService.save(launchEntity);
  }
}
