package com.ihu.scorm.engine.common.infra;

import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class UuidGenerator implements IdGenerator {

  @Override
  public UUID newUuid() {
    return UUID.randomUUID();
  }
}
