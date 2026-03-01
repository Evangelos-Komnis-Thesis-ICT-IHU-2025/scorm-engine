package com.ihu.scorm.engine.launch.orchestrator;

import com.ihu.scorm.engine.attempt.crud.AttemptStatus;
import com.ihu.scorm.engine.attempt.crud.SnapshotReason;
import com.ihu.scorm.engine.common.command.CommandHandler;
import com.ihu.scorm.engine.common.constant.DetailKey;
import com.ihu.scorm.engine.common.error.ErrorCode;
import com.ihu.scorm.engine.common.error.ErrorMessages;
import com.ihu.scorm.engine.common.error.ValidationException;
import com.ihu.scorm.engine.launch.crud.LaunchEntity;
import com.ihu.scorm.engine.launch.crud.LaunchStatus;
import com.ihu.scorm.engine.launch.persist.LaunchReadService;
import com.ihu.scorm.engine.runtime.orchestrator.RuntimeFlushService;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TerminateLaunchCommandHandler implements CommandHandler<TerminateLaunchCommand, Void> {

  private final LaunchReadService launchReadService;
  private final RuntimeFlushService runtimeFlushService;

  public TerminateLaunchCommandHandler(
      LaunchReadService launchReadService,
      RuntimeFlushService runtimeFlushService) {
    this.launchReadService = launchReadService;
    this.runtimeFlushService = runtimeFlushService;
  }

  @Override
  @Transactional
  public Void handle(TerminateLaunchCommand command) {
    LaunchEntity launch = launchReadService.getRequired(command.launchId());
    if (!launch.getId().equals(command.launchTokenPayload().launchId())) {
      throw new ValidationException(
          ErrorCode.TOKEN_LAUNCH_MISMATCH,
          ErrorMessages.LAUNCH_TOKEN_DOES_NOT_MATCH_LAUNCH_ID,
          Map.of(DetailKey.LAUNCH_ID, command.launchId()));
    }
    if (launch.getStatus() == LaunchStatus.CLOSED) {
      return null;
    }

    runtimeFlushService.flushLaunch(launch, SnapshotReason.TERMINATE, AttemptStatus.TERMINATED);
    return null;
  }
}
