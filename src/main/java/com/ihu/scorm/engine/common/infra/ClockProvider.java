package com.ihu.scorm.engine.common.infra;

import java.time.Instant;

public interface ClockProvider {

  Instant now();
}
