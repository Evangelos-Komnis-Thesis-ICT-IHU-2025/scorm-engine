package com.ihu.scorm.engine.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.storage")
public record StorageProperties(
    String endpoint,
    String accessKey,
    String secretKey,
    String bucketPackages,
    String bucketExtracted,
    Integer presignedTtlMinutes) {

  public int presignedTtlMinutesOrDefault() {
    return presignedTtlMinutes == null ? 10 : presignedTtlMinutes;
  }
}
