package com.ihu.scorm.engine.common.security;

import com.ihu.scorm.engine.common.constant.SecurityConstants;
import java.util.Locale;

public enum SecurityRole {
  ADMIN,
  INSTRUCTOR,
  LAUNCH,
  LEARNER;

  public String roleName() {
    return name();
  }

  public String authority() {
    return SecurityConstants.ROLE_PREFIX + name();
  }

  public static String normalizeAuthority(String role) {
    if (role == null) {
      return SecurityConstants.ROLE_PREFIX;
    }
    String normalizedRole = role.trim().toUpperCase(Locale.ROOT);
    if (normalizedRole.startsWith(SecurityConstants.ROLE_PREFIX)) {
      return normalizedRole;
    }
    return SecurityConstants.ROLE_PREFIX + normalizedRole;
  }
}
