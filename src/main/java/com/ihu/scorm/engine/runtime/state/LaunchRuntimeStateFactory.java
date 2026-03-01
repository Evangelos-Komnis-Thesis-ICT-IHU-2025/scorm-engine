package com.ihu.scorm.engine.runtime.state;

import com.ihu.scorm.engine.attempt.crud.AttemptEntity;
import com.ihu.scorm.engine.launch.crud.LaunchEntity;
import java.time.Instant;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class LaunchRuntimeStateFactory {

  public LaunchRuntimeState seedForLaunch(LaunchEntity launch, AttemptEntity attempt, Instant now) {
    return LaunchRuntimeStateBuilder.create()
        .launchId(launch.getId())
        .attemptId(attempt.getId())
        .userId(attempt.getUserId())
        .courseId(attempt.getCourseId())
        .standard(launch.getStandard())
        .lastSequence(0)
        .lastCommitAt(now)
        .lastSeenAt(now)
        .dirty(false)
        .build();
  }

  public LaunchRuntimeState initializeForCommit(LaunchEntity launch, AttemptEntity attempt, Instant now) {
    return LaunchRuntimeStateBuilder.create()
        .launchId(launch.getId())
        .attemptId(attempt.getId())
        .userId(attempt.getUserId())
        .courseId(attempt.getCourseId())
        .standard(launch.getStandard())
        .lastSequence(0)
        .lastSeenAt(now)
        .dirty(false)
        .build();
  }
}
