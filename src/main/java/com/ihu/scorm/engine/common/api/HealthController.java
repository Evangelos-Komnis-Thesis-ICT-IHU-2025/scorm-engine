package com.ihu.scorm.engine.common.api;

import com.ihu.scorm.engine.common.constant.DetailKey;
import java.util.Map;
import org.springframework.boot.actuate.health.Status;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/health")
public class HealthController {

  @GetMapping
  public Map<String, Object> health() {
    return Map.of(DetailKey.STATUS, Status.UP.getCode());
  }
}
