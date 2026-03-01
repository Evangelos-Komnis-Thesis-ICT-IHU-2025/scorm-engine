package com.ihu.scorm.engine.launch.orchestrator;

import com.ihu.scorm.engine.common.token.LaunchTokenPayload;
import java.util.UUID;

public record TerminateLaunchCommand(
    UUID launchId,
    LaunchTokenPayload launchTokenPayload) {
}
