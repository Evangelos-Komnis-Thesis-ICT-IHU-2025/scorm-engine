package com.ihu.scorm.engine.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.player")
public record PlayerProperties(String baseUrl) {
}
