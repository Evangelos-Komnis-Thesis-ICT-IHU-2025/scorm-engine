package com.ihu.scorm.engine.runtime.state;

import com.ihu.scorm.engine.standard.LearningStandard;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public final class LaunchRuntimeStateBuilder {

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

  private LaunchRuntimeStateBuilder() {
  }

  public static LaunchRuntimeStateBuilder create() {
    return new LaunchRuntimeStateBuilder();
  }

  public LaunchRuntimeStateBuilder launchId(UUID launchId) {
    this.launchId = launchId;
    return this;
  }

  public LaunchRuntimeStateBuilder attemptId(UUID attemptId) {
    this.attemptId = attemptId;
    return this;
  }

  public LaunchRuntimeStateBuilder userId(UUID userId) {
    this.userId = userId;
    return this;
  }

  public LaunchRuntimeStateBuilder courseId(UUID courseId) {
    this.courseId = courseId;
    return this;
  }

  public LaunchRuntimeStateBuilder standard(LearningStandard standard) {
    this.standard = standard;
    return this;
  }

  public LaunchRuntimeStateBuilder lastSequence(Integer lastSequence) {
    this.lastSequence = lastSequence;
    return this;
  }

  public LaunchRuntimeStateBuilder lastCommitAt(Instant lastCommitAt) {
    this.lastCommitAt = lastCommitAt;
    return this;
  }

  public LaunchRuntimeStateBuilder lastSeenAt(Instant lastSeenAt) {
    this.lastSeenAt = lastSeenAt;
    return this;
  }

  public LaunchRuntimeStateBuilder dirty(boolean dirty) {
    this.dirty = dirty;
    return this;
  }

  public LaunchRuntimeStateBuilder rawRuntimeState(Map<String, Object> rawRuntimeState) {
    this.rawRuntimeState = rawRuntimeState == null ? new LinkedHashMap<>() : new LinkedHashMap<>(rawRuntimeState);
    return this;
  }

  public LaunchRuntimeStateBuilder normalizedProgress(NormalizedProgress normalizedProgress) {
    this.normalizedProgress = normalizedProgress == null ? new NormalizedProgress() : normalizedProgress;
    return this;
  }

  public LaunchRuntimeState build() {
    LaunchRuntimeState state = new LaunchRuntimeState();
    state.setLaunchId(launchId);
    state.setAttemptId(attemptId);
    state.setUserId(userId);
    state.setCourseId(courseId);
    state.setStandard(standard);
    state.setLastSequence(lastSequence);
    state.setLastCommitAt(lastCommitAt);
    state.setLastSeenAt(lastSeenAt);
    state.setDirty(dirty);
    state.setRawRuntimeState(rawRuntimeState);
    state.setNormalizedProgress(normalizedProgress);
    return state;
  }
}
