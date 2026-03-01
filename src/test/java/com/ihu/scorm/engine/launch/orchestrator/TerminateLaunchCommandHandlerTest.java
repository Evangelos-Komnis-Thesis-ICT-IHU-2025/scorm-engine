package com.ihu.scorm.engine.launch.orchestrator;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ihu.scorm.engine.attempt.crud.AttemptStatus;
import com.ihu.scorm.engine.attempt.crud.SnapshotReason;
import com.ihu.scorm.engine.common.error.ValidationException;
import com.ihu.scorm.engine.common.token.LaunchTokenPayload;
import com.ihu.scorm.engine.launch.crud.LaunchEntity;
import com.ihu.scorm.engine.launch.crud.LaunchStatus;
import com.ihu.scorm.engine.launch.persist.LaunchReadService;
import com.ihu.scorm.engine.runtime.orchestrator.RuntimeFlushService;
import com.ihu.scorm.engine.standard.LearningStandard;
import java.lang.reflect.Field;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class TerminateLaunchCommandHandlerTest {

  @Test
  void doesNothingWhenLaunchAlreadyClosed() {
    LaunchReadService launchReadService = mock(LaunchReadService.class);
    RuntimeFlushService runtimeFlushService = mock(RuntimeFlushService.class);
    TerminateLaunchCommandHandler handler = new TerminateLaunchCommandHandler(launchReadService, runtimeFlushService);

    LaunchEntity launch = launchWithStatus(LaunchStatus.CLOSED);
    when(launchReadService.getRequired(launch.getId())).thenReturn(launch);

    LaunchTokenPayload payload = payloadForLaunch(launch.getId());
    handler.handle(new TerminateLaunchCommand(launch.getId(), payload));

    verify(runtimeFlushService, never()).flushLaunch(any(), any(), any());
  }

  @Test
  void throwsWhenTokenLaunchIdMismatch() {
    LaunchReadService launchReadService = mock(LaunchReadService.class);
    RuntimeFlushService runtimeFlushService = mock(RuntimeFlushService.class);
    TerminateLaunchCommandHandler handler = new TerminateLaunchCommandHandler(launchReadService, runtimeFlushService);

    LaunchEntity launch = launchWithStatus(LaunchStatus.ACTIVE);
    when(launchReadService.getRequired(launch.getId())).thenReturn(launch);

    LaunchTokenPayload wrongPayload = payloadForLaunch(UUID.randomUUID());

    assertThatThrownBy(() -> handler.handle(new TerminateLaunchCommand(launch.getId(), wrongPayload)))
        .isInstanceOf(ValidationException.class);
    verify(runtimeFlushService, never()).flushLaunch(any(), any(), any());
  }

  @Test
  void flushesWhenLaunchActiveAndTokenMatches() {
    LaunchReadService launchReadService = mock(LaunchReadService.class);
    RuntimeFlushService runtimeFlushService = mock(RuntimeFlushService.class);
    TerminateLaunchCommandHandler handler = new TerminateLaunchCommandHandler(launchReadService, runtimeFlushService);

    LaunchEntity launch = launchWithStatus(LaunchStatus.ACTIVE);
    when(launchReadService.getRequired(launch.getId())).thenReturn(launch);

    LaunchTokenPayload payload = payloadForLaunch(launch.getId());
    handler.handle(new TerminateLaunchCommand(launch.getId(), payload));

    verify(runtimeFlushService).flushLaunch(eq(launch), eq(SnapshotReason.TERMINATE), eq(AttemptStatus.TERMINATED));
  }

  private static LaunchEntity launchWithStatus(LaunchStatus status) {
    LaunchEntity launch = new LaunchEntity();
    setEntityId(launch);
    launch.setStatus(status);
    return launch;
  }

  private static LaunchTokenPayload payloadForLaunch(UUID launchId) {
    return new LaunchTokenPayload(
        launchId,
        UUID.randomUUID(),
        UUID.randomUUID(),
        UUID.randomUUID(),
        LearningStandard.SCORM_12,
        Instant.now().plusSeconds(3600));
  }

  private static void setEntityId(com.ihu.scorm.engine.common.persistence.BaseEntity entity) {
    try {
      Field idField = com.ihu.scorm.engine.common.persistence.BaseEntity.class.getDeclaredField("id");
      idField.setAccessible(true);
      idField.set(entity, UUID.randomUUID());
    } catch (Exception exception) {
      throw new RuntimeException(exception);
    }
  }
}
