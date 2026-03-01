package com.ihu.scorm.engine.attempt.crud;

import com.ihu.scorm.engine.attempt.AttemptDatabaseFieldConstants;
import com.ihu.scorm.engine.common.persistence.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = AttemptDatabaseFieldConstants.RUNTIME_SNAPSHOT_TABLE_NAME)
public class AttemptRuntimeSnapshotEntity extends BaseEntity {

  @Column(nullable = false)
  private UUID attemptId;

  @Column(nullable = false)
  private UUID launchId;

  @Column(nullable = false)
  private Instant capturedAt;

  @Column(nullable = false, columnDefinition = "jsonb")
  private String rawRuntimeJson;

  @Column(nullable = false, columnDefinition = "jsonb")
  private String normalizedProgressJson;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private SnapshotReason reason;

  public UUID getAttemptId() {
    return attemptId;
  }

  public void setAttemptId(UUID attemptId) {
    this.attemptId = attemptId;
  }

  public UUID getLaunchId() {
    return launchId;
  }

  public void setLaunchId(UUID launchId) {
    this.launchId = launchId;
  }

  public Instant getCapturedAt() {
    return capturedAt;
  }

  public void setCapturedAt(Instant capturedAt) {
    this.capturedAt = capturedAt;
  }

  public String getRawRuntimeJson() {
    return rawRuntimeJson;
  }

  public void setRawRuntimeJson(String rawRuntimeJson) {
    this.rawRuntimeJson = rawRuntimeJson;
  }

  public String getNormalizedProgressJson() {
    return normalizedProgressJson;
  }

  public void setNormalizedProgressJson(String normalizedProgressJson) {
    this.normalizedProgressJson = normalizedProgressJson;
  }

  public SnapshotReason getReason() {
    return reason;
  }

  public void setReason(SnapshotReason reason) {
    this.reason = reason;
  }
}
