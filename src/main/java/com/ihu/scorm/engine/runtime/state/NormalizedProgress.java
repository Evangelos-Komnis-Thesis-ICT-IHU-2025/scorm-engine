package com.ihu.scorm.engine.runtime.state;

import com.ihu.scorm.engine.attempt.crud.CompletionStatus;
import com.ihu.scorm.engine.attempt.crud.SuccessStatus;
import java.time.Instant;

public class NormalizedProgress {

  private CompletionStatus completionStatus = CompletionStatus.NOT_ATTEMPTED;
  private SuccessStatus successStatus = SuccessStatus.UNKNOWN;
  private ScoreInfo score = new ScoreInfo();
  private TimeInfo time = new TimeInfo();
  private String location;
  private String suspendData;
  private Instant lastCommitAt;

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

  public ScoreInfo getScore() {
    return score;
  }

  public void setScore(ScoreInfo score) {
    this.score = score;
  }

  public TimeInfo getTime() {
    return time;
  }

  public void setTime(TimeInfo time) {
    this.time = time;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getSuspendData() {
    return suspendData;
  }

  public void setSuspendData(String suspendData) {
    this.suspendData = suspendData;
  }

  public Instant getLastCommitAt() {
    return lastCommitAt;
  }

  public void setLastCommitAt(Instant lastCommitAt) {
    this.lastCommitAt = lastCommitAt;
  }
}
