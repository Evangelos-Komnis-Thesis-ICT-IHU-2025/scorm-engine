package com.ihu.scorm.engine.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.runtime")
public record RuntimeProperties(
    Integer launchStateTtlSeconds,
    Integer staleFlushMinutes,
    Integer flushJobIntervalMs,
    Boolean strictCommitSequence) {

  public int launchStateTtlSecondsOrDefault() {
    return launchStateTtlSeconds == null ? 7200 : launchStateTtlSeconds;
  }

  public int staleFlushMinutesOrDefault() {
    return staleFlushMinutes == null ? 10 : staleFlushMinutes;
  }

  public int flushJobIntervalMsOrDefault() {
    return flushJobIntervalMs == null ? 60000 : flushJobIntervalMs;
  }

  public boolean strictCommitSequenceOrDefault() {
    return strictCommitSequence != null && strictCommitSequence;
  }
}
