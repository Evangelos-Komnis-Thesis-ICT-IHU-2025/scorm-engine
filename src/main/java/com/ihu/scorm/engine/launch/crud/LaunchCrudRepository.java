package com.ihu.scorm.engine.launch.crud;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LaunchCrudRepository extends JpaRepository<LaunchEntity, UUID> {

  List<LaunchEntity> findByStatusAndLastSeenAtBefore(LaunchStatus status, Instant lastSeenThreshold);

  Optional<LaunchEntity> findTopByAttemptIdOrderByCreatedAtDesc(UUID attemptId);
}
