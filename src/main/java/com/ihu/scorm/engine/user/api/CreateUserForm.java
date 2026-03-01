package com.ihu.scorm.engine.user.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserForm(
    String externalRef,
    @NotBlank String username,
    @NotBlank @Email String email,
    String firstName,
    String lastName,
    String locale) {
}
