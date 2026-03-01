package com.ihu.scorm.engine.launch.crud;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class LaunchStatusTransitionsTest {

  @Test
  void activeCanClose() {
    assertThat(LaunchStatus.ACTIVE.canTransitionTo(LaunchStatus.CLOSED)).isTrue();
  }

  @Test
  void closedCanOnlyStayClosed() {
    assertThat(LaunchStatus.CLOSED.canTransitionTo(LaunchStatus.CLOSED)).isTrue();
    assertThat(LaunchStatus.CLOSED.canTransitionTo(LaunchStatus.ACTIVE)).isFalse();
  }
}
