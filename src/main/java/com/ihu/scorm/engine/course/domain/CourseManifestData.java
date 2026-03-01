package com.ihu.scorm.engine.course.domain;

import com.ihu.scorm.engine.standard.LearningStandard;
import java.util.Map;

public record CourseManifestData(
    String title,
    String description,
    String entrypointPath,
    LearningStandard standard,
    Map<String, Object> metadata) {
}
