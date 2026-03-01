package com.ihu.scorm.engine.course.api;

import com.ihu.scorm.engine.standard.LearningStandard;
import java.time.Instant;
import java.util.UUID;

public record CourseDto(
    UUID id,
    String code,
    String title,
    String description,
    LearningStandard standard,
    String versionLabel,
    String entrypointPath,
    String manifestHash,
    String metadataJson,
    String storageBucket,
    String storageObjectKeyZip,
    Instant createdAt,
    Instant updatedAt) {
}
