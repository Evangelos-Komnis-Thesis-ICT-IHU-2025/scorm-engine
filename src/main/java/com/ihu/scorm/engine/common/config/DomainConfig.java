package com.ihu.scorm.engine.common.config;

import com.ihu.scorm.engine.course.domain.ManifestParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfig {

  @Bean
  public ManifestParser manifestParser() {
    return new ManifestParser();
  }
}
