package com.ihu.scorm.engine.attempt.persist;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ihu.scorm.engine.attempt.crud.AttemptCrudService;
import com.ihu.scorm.engine.attempt.crud.AttemptEntity;
import com.ihu.scorm.engine.attempt.crud.AttemptRuntimeSnapshotEntity;
import com.ihu.scorm.engine.attempt.crud.AttemptStatus;
import com.ihu.scorm.engine.attempt.crud.CompletionStatus;
import com.ihu.scorm.engine.attempt.crud.SnapshotReason;
import com.ihu.scorm.engine.attempt.crud.SuccessStatus;
import com.ihu.scorm.engine.common.constant.SystemMessages;
import com.ihu.scorm.engine.runtime.state.NormalizedProgress;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class AttemptPersistService {

  private final AttemptCrudService attemptCrudService;
  private final ObjectMapper objectMapper;

  public AttemptPersistService(AttemptCrudService attemptCrudService, ObjectMapper objectMapper) {
    this.attemptCrudService = attemptCrudService;
    this.objectMapper = objectMapper;
  }

  public AttemptEntity create(UUID userId, UUID courseId, UUID enrollmentId, int attemptNo, Instant startedAt) {
    AttemptEntity entity = new AttemptEntity();
    entity.setUserId(userId);
    entity.setCourseId(courseId);
    entity.setEnrollmentId(enrollmentId);
    entity.setAttemptNo(attemptNo);
    entity.setStatus(AttemptStatus.IN_PROGRESS);
    entity.setStartedAt(startedAt);
    entity.setCompletionStatus(CompletionStatus.NOT_ATTEMPTED);
    entity.setSuccessStatus(SuccessStatus.UNKNOWN);
    entity.setTotalTimeSeconds(0L);
    return attemptCrudService.save(entity);
  }

  public AttemptEntity save(AttemptEntity attemptEntity) {
    return attemptCrudService.save(attemptEntity);
  }

  public AttemptEntity applyNormalizedProgress(AttemptEntity attempt, NormalizedProgress progress) {
    if (progress.getCompletionStatus() != null) {
      attempt.setCompletionStatus(progress.getCompletionStatus());
    }
    if (progress.getSuccessStatus() != null) {
      attempt.setSuccessStatus(progress.getSuccessStatus());
    }
    if (progress.getScore() != null) {
      attempt.setScoreRaw(progress.getScore().getRaw());
      attempt.setScoreScaled(progress.getScore().getScaled());
    }
    if (progress.getTime() != null && progress.getTime().getTotalSeconds() != null) {
      attempt.setTotalTimeSeconds(progress.getTime().getTotalSeconds());
    }
    attempt.setLastLocation(progress.getLocation());
    attempt.setLastSuspendData(progress.getSuspendData());
    attempt.setLastCommittedAt(progress.getLastCommitAt());
    return attemptCrudService.save(attempt);
  }

  public void closeAttempt(AttemptEntity attempt, AttemptStatus status, Instant endedAt) {
    AttemptStatus currentStatus = attempt.getStatus();
    if (currentStatus != null && !currentStatus.canTransitionTo(status)) {
      throw new IllegalStateException(
          SystemMessages.INVALID_ATTEMPT_STATUS_TRANSITION + " from " + currentStatus + " to " + status);
    }
    attempt.setStatus(status);
    attempt.setEndedAt(endedAt);
    attemptCrudService.save(attempt);
  }

  public void saveSnapshot(UUID attemptId, UUID launchId, Map<String, Object> rawRuntime,
      NormalizedProgress progress, SnapshotReason reason, Instant capturedAt) {
    AttemptRuntimeSnapshotEntity snapshot = new AttemptRuntimeSnapshotEntity();
    snapshot.setAttemptId(attemptId);
    snapshot.setLaunchId(launchId);
    snapshot.setCapturedAt(capturedAt);
    snapshot.setRawRuntimeJson(toJson(rawRuntime));
    snapshot.setNormalizedProgressJson(toJson(progress));
    snapshot.setReason(reason);
    attemptCrudService.saveSnapshot(snapshot);
  }

  private String toJson(Object value) {
    try {
      return objectMapper.writeValueAsString(value);
    } catch (JsonProcessingException exception) {
      throw new IllegalStateException(SystemMessages.FAILED_TO_SERIALIZE_SNAPSHOT_PAYLOAD, exception);
    }
  }
}
