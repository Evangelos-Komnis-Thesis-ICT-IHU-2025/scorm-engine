package com.ihu.scorm.engine.course.orchestrator;

public record ImportCourseCommand(
    byte[] fileBytes,
    String originalFilename,
    String contentType,
    String code,
    String versionLabel) {
}
