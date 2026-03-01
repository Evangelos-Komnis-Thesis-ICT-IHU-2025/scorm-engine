package com.ihu.scorm.engine.user.api;

import java.time.Instant;
import java.util.UUID;

public record UserDto(
    UUID id,
    String externalRef,
    String username,
    String email,
    String firstName,
    String lastName,
    String locale,
    Instant createdAt,
    Instant updatedAt) {
}
