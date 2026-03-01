package com.ihu.scorm.engine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ConfigurationPropertiesScan
public class ScormEngineApplication {

  public static void main(String[] args) {
    SpringApplication.run(ScormEngineApplication.class, args);
  }
}
