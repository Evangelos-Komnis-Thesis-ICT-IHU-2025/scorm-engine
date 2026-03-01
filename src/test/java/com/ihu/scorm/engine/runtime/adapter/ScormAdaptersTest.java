package com.ihu.scorm.engine.runtime.adapter;

import static org.assertj.core.api.Assertions.assertThat;

import com.ihu.scorm.engine.runtime.state.LaunchRuntimeState;
import com.ihu.scorm.engine.standard.LearningStandard;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ScormAdaptersTest {

  @Test
  void scorm12MapsProgress() {
    Scorm12Adapter adapter = new Scorm12Adapter();
    LaunchRuntimeState state = new LaunchRuntimeState();

    StandardCommitPayload payload = new StandardCommitPayload(
        UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), LearningStandard.SCORM_12,
        1, Instant.parse("2026-03-01T10:00:00Z"),
        Map.of(
            "cmi.core.lesson_status", "completed",
            "cmi.core.score.raw", "85",
            "cmi.core.session_time", "00:02:00",
            "cmi.core.lesson_location", "page3",
            "cmi.suspend_data", "bookmark"));

    NormalizedUpdateResult result = adapter.applyCommit(payload, state);

    assertThat(result.normalizedProgress().getCompletionStatus().name()).isEqualTo("COMPLETE");
    assertThat(result.normalizedProgress().getScore().getRaw()).hasToString("85");
    assertThat(result.normalizedProgress().getTime().getSessionSeconds()).isEqualTo(120L);
    assertThat(result.normalizedProgress().getLocation()).isEqualTo("page3");
  }

  @Test
  void scorm2004MapsProgress() {
    Scorm2004Adapter adapter = new Scorm2004Adapter();
    LaunchRuntimeState state = new LaunchRuntimeState();

    StandardCommitPayload payload = new StandardCommitPayload(
        UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), LearningStandard.SCORM_2004,
        2, Instant.parse("2026-03-01T10:01:00Z"),
        Map.of(
            "cmi.completion_status", "incomplete",
            "cmi.success_status", "passed",
            "cmi.score.scaled", "0.92",
            "cmi.session_time", "PT2M",
            "cmi.location", "screen2"));

    NormalizedUpdateResult result = adapter.applyCommit(payload, state);

    assertThat(result.normalizedProgress().getCompletionStatus().name()).isEqualTo("INCOMPLETE");
    assertThat(result.normalizedProgress().getSuccessStatus().name()).isEqualTo("PASSED");
    assertThat(result.normalizedProgress().getScore().getScaled()).hasToString("0.92");
    assertThat(result.normalizedProgress().getTime().getSessionSeconds()).isEqualTo(120L);
  }
}
