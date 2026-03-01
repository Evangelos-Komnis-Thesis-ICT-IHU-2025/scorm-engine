package com.ihu.scorm.engine.launch.orchestrator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ihu.scorm.engine.attempt.persist.AttemptReadService;
import com.ihu.scorm.engine.common.config.StorageProperties;
import com.ihu.scorm.engine.common.infra.ClockProvider;
import com.ihu.scorm.engine.common.storage.ObjectStorage;
import com.ihu.scorm.engine.common.token.LaunchTokenPayload;
import com.ihu.scorm.engine.course.persist.CourseReadService;
import com.ihu.scorm.engine.launch.api.LaunchCreatedDto;
import com.ihu.scorm.engine.launch.persist.LaunchReadService;
import com.ihu.scorm.engine.runtime.state.RuntimeStateStore;
import com.ihu.scorm.engine.standard.LearningStandard;
import com.ihu.scorm.engine.user.persist.UserReadService;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class LaunchOrchestratorDelegationTest {

  @Test
  void createLaunchDelegatesToCreateHandler() {
    CreateLaunchCommandHandler createHandler = mock(CreateLaunchCommandHandler.class);
    TerminateLaunchCommandHandler terminateHandler = mock(TerminateLaunchCommandHandler.class);
    LaunchOrchestrator orchestrator = newOrchestrator(createHandler, terminateHandler);

    CreateLaunchCommand command = new CreateLaunchCommand(UUID.randomUUID(), UUID.randomUUID(), false);
    LaunchCreatedDto expected = new LaunchCreatedDto(
        UUID.randomUUID(),
        UUID.randomUUID(),
        LearningStandard.SCORM_12,
        "http://localhost/launch",
        "token",
        Instant.now().plusSeconds(3600));
    when(createHandler.handle(command)).thenReturn(expected);

    LaunchCreatedDto result = orchestrator.createLaunch(command);

    assertThat(result).isSameAs(expected);
    verify(createHandler).handle(command);
  }

  @Test
  void terminateDelegatesToTerminateHandler() {
    CreateLaunchCommandHandler createHandler = mock(CreateLaunchCommandHandler.class);
    TerminateLaunchCommandHandler terminateHandler = mock(TerminateLaunchCommandHandler.class);
    LaunchOrchestrator orchestrator = newOrchestrator(createHandler, terminateHandler);

    UUID launchId = UUID.randomUUID();
    LaunchTokenPayload payload = new LaunchTokenPayload(
        launchId,
        UUID.randomUUID(),
        UUID.randomUUID(),
        UUID.randomUUID(),
        LearningStandard.SCORM_12,
        Instant.now().plusSeconds(3600));

    orchestrator.terminate(launchId, payload);

    verify(terminateHandler).handle(new TerminateLaunchCommand(launchId, payload));
  }

  private static LaunchOrchestrator newOrchestrator(
      CreateLaunchCommandHandler createHandler,
      TerminateLaunchCommandHandler terminateHandler) {
    return new LaunchOrchestrator(
        mock(UserReadService.class),
        mock(CourseReadService.class),
        mock(AttemptReadService.class),
        mock(LaunchReadService.class),
        mock(ClockProvider.class),
        new StorageProperties("http://localhost:9000", "key", "secret", "packages", "extracted", 10),
        mock(ObjectStorage.class),
        mock(RuntimeStateStore.class),
        createHandler,
        terminateHandler);
  }
}
