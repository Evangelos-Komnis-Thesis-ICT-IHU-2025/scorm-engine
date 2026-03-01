package com.ihu.scorm.engine.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.jwt")
public record JwtProperties(
    String engineSecret,
    String launchSecret,
    Integer launchTtlMinutes,
    Integer engineTtlMinutes) {

  public int launchTtlMinutesOrDefault() {
    return launchTtlMinutes == null ? 120 : launchTtlMinutes;
  }

  public int engineTtlMinutesOrDefault() {
    return engineTtlMinutes == null ? 120 : engineTtlMinutes;
  }
}
