package com.ihu.scorm.engine.runtime.api;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Map;

public record RuntimeCommitForm(
    @NotNull Integer sequence,
    @NotNull Instant clientTime,
    String apiKind,
    @NotNull Map<String, Object> payload) {
}
