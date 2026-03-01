package com.ihu.scorm.engine.attempt.crud;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class AttemptStatusTransitionsTest {

  @Test
  void inProgressAllowsTerminalTransitions() {
    assertThat(AttemptStatus.IN_PROGRESS.canTransitionTo(AttemptStatus.COMPLETED)).isTrue();
    assertThat(AttemptStatus.IN_PROGRESS.canTransitionTo(AttemptStatus.TERMINATED)).isTrue();
    assertThat(AttemptStatus.IN_PROGRESS.canTransitionTo(AttemptStatus.ABANDONED)).isTrue();
  }

  @Test
  void terminalStatesAreIdempotentOnly() {
    assertThat(AttemptStatus.COMPLETED.canTransitionTo(AttemptStatus.COMPLETED)).isTrue();
    assertThat(AttemptStatus.COMPLETED.canTransitionTo(AttemptStatus.TERMINATED)).isFalse();
    assertThat(AttemptStatus.TERMINATED.canTransitionTo(AttemptStatus.ABANDONED)).isFalse();
    assertThat(AttemptStatus.ABANDONED.canTransitionTo(AttemptStatus.IN_PROGRESS)).isFalse();
  }
}
