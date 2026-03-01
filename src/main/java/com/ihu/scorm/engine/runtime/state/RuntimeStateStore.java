package com.ihu.scorm.engine.runtime.state;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface RuntimeStateStore {

  LaunchRuntimeState get(UUID launchId);

  void save(LaunchRuntimeState state, int ttlSeconds);

  void delete(UUID launchId);

  List<LaunchRuntimeState> findDirtyBefore(Instant lastSeenBefore);
}
