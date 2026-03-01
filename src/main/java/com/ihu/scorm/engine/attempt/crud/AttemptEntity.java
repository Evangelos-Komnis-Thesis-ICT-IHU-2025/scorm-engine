package com.ihu.scorm.engine.attempt.crud;

import com.ihu.scorm.engine.attempt.AttemptDatabaseFieldConstants;
import com.ihu.scorm.engine.common.persistence.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = AttemptDatabaseFieldConstants.TABLE_NAME)
public class AttemptEntity extends BaseEntity {

  @Column(nullable = false)
  private UUID userId;

  @Column(nullable = false)
  private UUID courseId;

  @Column(nullable = false)
  private UUID enrollmentId;

  @Column(nullable = false)
  private Integer attemptNo;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private AttemptStatus status;

  @Column(nullable = false)
  private Instant startedAt;

  @Column
  private Instant endedAt;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private CompletionStatus completionStatus;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private SuccessStatus successStatus;

  @Column(precision = 10, scale = 4)
  private BigDecimal scoreRaw;

  @Column(precision = 10, scale = 4)
  private BigDecimal scoreScaled;

  @Column(nullable = false)
  private Long totalTimeSeconds;

  @Column
  private String lastLocation;

  @Column(columnDefinition = "text")
  private String lastSuspendData;

  @Column
  private Instant lastCommittedAt;

  public UUID getUserId() {
    return userId;
  }

  public void setUserId(UUID userId) {
    this.userId = userId;
  }

  public UUID getCourseId() {
    return courseId;
  }

  public void setCourseId(UUID courseId) {
    this.courseId = courseId;
  }

  public UUID getEnrollmentId() {
    return enrollmentId;
  }

  public void setEnrollmentId(UUID enrollmentId) {
    this.enrollmentId = enrollmentId;
  }

  public Integer getAttemptNo() {
    return attemptNo;
  }

  public void setAttemptNo(Integer attemptNo) {
    this.attemptNo = attemptNo;
  }

  public AttemptStatus getStatus() {
    return status;
  }

  public void setStatus(AttemptStatus status) {
    this.status = status;
  }

  public Instant getStartedAt() {
    return startedAt;
  }

  public void setStartedAt(Instant startedAt) {
    this.startedAt = startedAt;
  }

  public Instant getEndedAt() {
    return endedAt;
  }

  public void setEndedAt(Instant endedAt) {
    this.endedAt = endedAt;
  }

  public CompletionStatus getCompletionStatus() {
    return completionStatus;
  }

  public void setCompletionStatus(CompletionStatus completionStatus) {
    this.completionStatus = completionStatus;
  }

  public SuccessStatus getSuccessStatus() {
    return successStatus;
  }

  public void setSuccessStatus(SuccessStatus successStatus) {
    this.successStatus = successStatus;
  }

  public BigDecimal getScoreRaw() {
    return scoreRaw;
  }

  public void setScoreRaw(BigDecimal scoreRaw) {
    this.scoreRaw = scoreRaw;
  }

  public BigDecimal getScoreScaled() {
    return scoreScaled;
  }

  public void setScoreScaled(BigDecimal scoreScaled) {
    this.scoreScaled = scoreScaled;
  }

  public Long getTotalTimeSeconds() {
    return totalTimeSeconds;
  }

  public void setTotalTimeSeconds(Long totalTimeSeconds) {
    this.totalTimeSeconds = totalTimeSeconds;
  }

  public String getLastLocation() {
    return lastLocation;
  }

  public void setLastLocation(String lastLocation) {
    this.lastLocation = lastLocation;
  }

  public String getLastSuspendData() {
    return lastSuspendData;
  }

  public void setLastSuspendData(String lastSuspendData) {
    this.lastSuspendData = lastSuspendData;
  }

  public Instant getLastCommittedAt() {
    return lastCommittedAt;
  }

  public void setLastCommittedAt(Instant lastCommittedAt) {
    this.lastCommittedAt = lastCommittedAt;
  }
}
