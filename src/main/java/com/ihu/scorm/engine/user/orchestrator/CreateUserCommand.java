package com.ihu.scorm.engine.user.orchestrator;

public record CreateUserCommand(
    String externalRef,
    String username,
    String email,
    String firstName,
    String lastName,
    String locale) {
}
