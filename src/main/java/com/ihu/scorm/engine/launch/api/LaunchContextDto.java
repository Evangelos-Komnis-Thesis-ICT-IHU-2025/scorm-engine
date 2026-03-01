package com.ihu.scorm.engine.launch.api;

import com.ihu.scorm.engine.runtime.state.NormalizedProgress;
import com.ihu.scorm.engine.standard.LearningStandard;
import java.util.UUID;

public record LaunchContextDto(
    UUID launchId,
    UUID attemptId,
    UserView user,
    CourseView course,
    PlayerView player,
    RuntimeView runtime) {

  public record UserView(UUID id, String name, String email) {
  }

  public record CourseView(UUID id, String title, LearningStandard standard) {
  }

  public record PlayerView(String apiKind, ContentSourceView contentSource, String entrypointPath) {
  }

  public record ContentSourceView(String type, String url) {
  }

  public record RuntimeView(NormalizedProgress initialNormalizedState) {
  }
}
