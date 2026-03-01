package com.ihu.scorm.engine.launch.crud;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class LaunchCrudService {

  private final LaunchCrudRepository repository;

  public LaunchCrudService(LaunchCrudRepository repository) {
    this.repository = repository;
  }

  public LaunchEntity save(LaunchEntity launch) {
    return repository.save(launch);
  }

  public LaunchEntity findById(UUID launchId) {
    return repository.findById(launchId).orElse(null);
  }

  public List<LaunchEntity> findStaleActive(Instant threshold) {
    return repository.findByStatusAndLastSeenAtBefore(LaunchStatus.ACTIVE, threshold);
  }

  public LaunchEntity findLatestByAttemptId(UUID attemptId) {
    return repository.findTopByAttemptIdOrderByCreatedAtDesc(attemptId).orElse(null);
  }
}
