package com.ihu.scorm.engine.attempt.crud;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttemptRuntimeSnapshotCrudRepository extends JpaRepository<AttemptRuntimeSnapshotEntity, UUID> {
}
