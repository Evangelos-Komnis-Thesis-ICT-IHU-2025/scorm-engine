package com.ihu.scorm.engine.runtime.adapter;

import com.ihu.scorm.engine.runtime.state.LaunchRuntimeState;
import com.ihu.scorm.engine.runtime.state.NormalizedProgress;
import com.ihu.scorm.engine.runtime.state.ScoreInfo;
import com.ihu.scorm.engine.runtime.state.TimeInfo;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

abstract class AbstractStandardAdapter implements StandardAdapter {

  protected Map<String, Object> mergeRaw(Map<String, Object> newRaw, LaunchRuntimeState currentState) {
    Map<String, Object> merged = new LinkedHashMap<>(currentState.getRawRuntimeState());
    merged.putAll(newRaw);
    return merged;
  }

  protected NormalizedProgress copyProgress(LaunchRuntimeState state) {
    NormalizedProgress source = state.getNormalizedProgress();
    NormalizedProgress copy = new NormalizedProgress();
    copy.setCompletionStatus(source.getCompletionStatus());
    copy.setSuccessStatus(source.getSuccessStatus());

    ScoreInfo sourceScore = source.getScore() == null ? new ScoreInfo() : source.getScore();
    ScoreInfo score = new ScoreInfo();
    score.setRaw(sourceScore.getRaw());
    score.setScaled(sourceScore.getScaled());
    score.setMin(sourceScore.getMin());
    score.setMax(sourceScore.getMax());
    copy.setScore(score);

    TimeInfo sourceTime = source.getTime() == null ? new TimeInfo() : source.getTime();
    TimeInfo time = new TimeInfo();
    time.setSessionSeconds(sourceTime.getSessionSeconds());
    time.setTotalSeconds(sourceTime.getTotalSeconds());
    copy.setTime(time);

    copy.setLocation(source.getLocation());
    copy.setSuspendData(source.getSuspendData());
    copy.setLastCommitAt(source.getLastCommitAt());
    return copy;
  }

  protected BigDecimal parseBigDecimal(Object value) {
    if (value == null) {
      return null;
    }
    try {
      return new BigDecimal(String.valueOf(value));
    } catch (NumberFormatException exception) {
      return null;
    }
  }

  protected Long parseScorm12Time(Object value) {
    if (value == null) {
      return null;
    }
    String text = String.valueOf(value).trim();
    String[] parts = text.split(":");
    if (parts.length != 3) {
      return null;
    }
    try {
      long hours = Long.parseLong(parts[0]);
      long minutes = Long.parseLong(parts[1]);
      double seconds = Double.parseDouble(parts[2]);
      return hours * 3600 + minutes * 60 + (long) seconds;
    } catch (NumberFormatException exception) {
      return null;
    }
  }

  protected Long parseScorm2004Duration(Object value) {
    if (value == null) {
      return null;
    }
    try {
      return Duration.parse(String.valueOf(value)).getSeconds();
    } catch (Exception exception) {
      return null;
    }
  }
}
