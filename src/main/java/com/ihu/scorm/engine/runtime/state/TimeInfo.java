package com.ihu.scorm.engine.runtime.state;

public class TimeInfo {

  private Long sessionSeconds;
  private Long totalSeconds;

  public Long getSessionSeconds() {
    return sessionSeconds;
  }

  public void setSessionSeconds(Long sessionSeconds) {
    this.sessionSeconds = sessionSeconds;
  }

  public Long getTotalSeconds() {
    return totalSeconds;
  }

  public void setTotalSeconds(Long totalSeconds) {
    this.totalSeconds = totalSeconds;
  }
}
