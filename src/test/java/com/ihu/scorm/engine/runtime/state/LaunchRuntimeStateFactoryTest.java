package com.ihu.scorm.engine.runtime.state;

import static org.assertj.core.api.Assertions.assertThat;

import com.ihu.scorm.engine.attempt.crud.AttemptEntity;
import com.ihu.scorm.engine.common.persistence.BaseEntity;
import com.ihu.scorm.engine.launch.crud.LaunchEntity;
import com.ihu.scorm.engine.standard.LearningStandard;
import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class LaunchRuntimeStateFactoryTest {

  private final LaunchRuntimeStateFactory factory = new LaunchRuntimeStateFactory();

  @Test
  void seedForLaunchSetsExpectedDefaults() {
    LaunchEntity launch = new LaunchEntity();
    setEntityId(launch);
    launch.setStandard(LearningStandard.SCORM_12);

    AttemptEntity attempt = new AttemptEntity();
    setEntityId(attempt);
    attempt.setUserId(UUID.randomUUID());
    attempt.setCourseId(UUID.randomUUID());

    Instant now = Instant.parse("2026-03-01T12:00:00Z");

    LaunchRuntimeState state = factory.seedForLaunch(launch, attempt, now);

    assertThat(state.getLaunchId()).isEqualTo(launch.getId());
    assertThat(state.getAttemptId()).isEqualTo(attempt.getId());
    assertThat(state.getUserId()).isEqualTo(attempt.getUserId());
    assertThat(state.getCourseId()).isEqualTo(attempt.getCourseId());
    assertThat(state.getStandard()).isEqualTo(LearningStandard.SCORM_12);
    assertThat(state.getLastSequence()).isEqualTo(0);
    assertThat(state.getLastCommitAt()).isEqualTo(now);
    assertThat(state.getLastSeenAt()).isEqualTo(now);
    assertThat(state.isDirty()).isFalse();
    assertThat(state.getRawRuntimeState()).isEmpty();
    assertThat(state.getNormalizedProgress()).isNotNull();
  }

  @Test
  void builderCreatesIndependentRawStateMap() {
    LaunchRuntimeState state = LaunchRuntimeStateBuilder.create()
        .launchId(UUID.randomUUID())
        .lastSequence(1)
        .rawRuntimeState(Map.of("key", "value"))
        .build();

    assertThat(state.getRawRuntimeState()).containsEntry("key", "value");
    assertThat(state.getNormalizedProgress()).isNotNull();
  }

  private static void setEntityId(BaseEntity entity) {
    try {
      Field idField = BaseEntity.class.getDeclaredField("id");
      idField.setAccessible(true);
      idField.set(entity, UUID.randomUUID());
    } catch (Exception exception) {
      throw new RuntimeException(exception);
    }
  }
}
