package com.ihu.scorm.engine.launch.crud;

import com.ihu.scorm.engine.common.persistence.BaseEntity;
import com.ihu.scorm.engine.launch.LaunchDatabaseFieldConstants;
import com.ihu.scorm.engine.standard.LearningStandard;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = LaunchDatabaseFieldConstants.TABLE_NAME)
public class LaunchEntity extends BaseEntity {

  @Column(nullable = false)
  private UUID attemptId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private LearningStandard standard;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private LaunchStatus status;

  @Column(nullable = false)
  private String launchTokenHash;

  @Column(nullable = false)
  private Instant lastSeenAt;

  @Column(nullable = false)
  private Instant expiresAt;

  public UUID getAttemptId() {
    return attemptId;
  }

  public void setAttemptId(UUID attemptId) {
    this.attemptId = attemptId;
  }

  public LearningStandard getStandard() {
    return standard;
  }

  public void setStandard(LearningStandard standard) {
    this.standard = standard;
  }

  public LaunchStatus getStatus() {
    return status;
  }

  public void setStatus(LaunchStatus status) {
    this.status = status;
  }

  public String getLaunchTokenHash() {
    return launchTokenHash;
  }

  public void setLaunchTokenHash(String launchTokenHash) {
    this.launchTokenHash = launchTokenHash;
  }

  public Instant getLastSeenAt() {
    return lastSeenAt;
  }

  public void setLastSeenAt(Instant lastSeenAt) {
    this.lastSeenAt = lastSeenAt;
  }

  public Instant getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(Instant expiresAt) {
    this.expiresAt = expiresAt;
  }
}
