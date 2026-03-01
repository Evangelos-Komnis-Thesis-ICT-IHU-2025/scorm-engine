package com.ihu.scorm.engine.runtime.adapter;

import com.ihu.scorm.engine.runtime.state.NormalizedProgress;
import java.util.Map;

public record NormalizedUpdateResult(NormalizedProgress normalizedProgress, Map<String, Object> mergedRawState) {
}
