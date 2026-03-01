package com.ihu.scorm.engine.runtime.state;

import com.ihu.scorm.engine.standard.LearningStandard;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class LaunchRuntimeState {

  private UUID launchId;
  private UUID attemptId;
  private UUID userId;
  private UUID courseId;
  private LearningStandard standard;
  private Integer lastSequence;
  private Instant lastCommitAt;
  private Instant lastSeenAt;
  private boolean dirty;
  private Map<String, Object> rawRuntimeState = new LinkedHashMap<>();
  private NormalizedProgress normalizedProgress = new NormalizedProgress();

  public UUID getLaunchId() {
    return launchId;
  }

  public void setLaunchId(UUID launchId) {
    this.launchId = launchId;
  }

  public UUID getAttemptId() {
    return attemptId;
  }

  public void setAttemptId(UUID attemptId) {
    this.attemptId = attemptId;
  }

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

  public LearningStandard getStandard() {
    return standard;
  }

  public void setStandard(LearningStandard standard) {
    this.standard = standard;
  }

  public Integer getLastSequence() {
    return lastSequence;
  }

  public void setLastSequence(Integer lastSequence) {
    this.lastSequence = lastSequence;
  }

  public Instant getLastCommitAt() {
    return lastCommitAt;
  }

  public void setLastCommitAt(Instant lastCommitAt) {
    this.lastCommitAt = lastCommitAt;
  }

  public Instant getLastSeenAt() {
    return lastSeenAt;
  }

  public void setLastSeenAt(Instant lastSeenAt) {
    this.lastSeenAt = lastSeenAt;
  }

  public boolean isDirty() {
    return dirty;
  }

  public void setDirty(boolean dirty) {
    this.dirty = dirty;
  }

  public Map<String, Object> getRawRuntimeState() {
    return rawRuntimeState;
  }

  public void setRawRuntimeState(Map<String, Object> rawRuntimeState) {
    this.rawRuntimeState = rawRuntimeState;
  }

  public NormalizedProgress getNormalizedProgress() {
    return normalizedProgress;
  }

  public void setNormalizedProgress(NormalizedProgress normalizedProgress) {
    this.normalizedProgress = normalizedProgress;
  }
}
