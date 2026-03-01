package com.ihu.scorm.engine.common.error;

public final class ErrorMessages {

  public static final String ACCESS_DENIED = "Access denied";
  public static final String ACTIVE_ENROLLMENT_NOT_FOUND = "Active enrollment not found";
  public static final String ACTIVE_ATTEMPT_ALREADY_EXISTS = "Active attempt already exists";
  public static final String ATTEMPT_NOT_FOUND = "Attempt not found";
  public static final String AUTHENTICATION_REQUIRED = "Authentication required";
  public static final String COMMIT_SEQUENCE_IS_OLDER_THAN_LAST_ACCEPTED =
      "Commit sequence is older than last accepted";
  public static final String COURSE_FILE_IS_EMPTY = "Course file is empty";
  public static final String COURSE_NOT_FOUND = "Course not found";
  public static final String COULD_NOT_RESOLVE_COURSE_ENTRYPOINT = "Could not resolve course entrypoint";
  public static final String EMAIL_ALREADY_EXISTS = "Email already exists";
  public static final String ENROLLMENT_ALREADY_EXISTS = "Enrollment already exists";
  public static final String FAILED_TO_PARSE_IMSMANIFEST_XML = "Failed to parse imsmanifest.xml";
  public static final String FAILED_TO_PARSE_RESOURCES = "Failed to parse resources";
  public static final String INVALID_SCORM_ZIP_FILE = "Invalid SCORM zip file";
  public static final String LAUNCH_IS_ALREADY_CLOSED = "Launch is already closed";
  public static final String LAUNCH_IS_CLOSED = "Launch is closed";
  public static final String LAUNCH_NOT_FOUND = "Launch not found";
  public static final String LAUNCH_TOKEN_DOES_NOT_MATCH_LAUNCH = "Launch token does not match launch";
  public static final String LAUNCH_TOKEN_DOES_NOT_MATCH_LAUNCH_ID = "Launch token does not match launch id";
  public static final String LAUNCH_TOKEN_EXPIRED = "Launch token expired";
  public static final String LAUNCH_TOKEN_IS_REQUIRED = "Launch token is required";
  public static final String MANIFEST_RESOURCES_ARE_EMPTY = "No resources with href found in manifest";
  public static final String NO_ADAPTER_REGISTERED_FOR_STANDARD = "No adapter registered for standard ";
  public static final String ONLY_ZIP_FILES_ARE_SUPPORTED = "Only .zip files are supported";
  public static final String PAGE_MUST_BE_GREATER_OR_EQUAL_TO_ZERO = "page must be >= 0";
  public static final String RUNTIME_COMMITS_RATE_LIMITED = "Too many runtime commits for launch";
  public static final String SCORM_MANIFEST_NOT_FOUND = "imsmanifest.xml not found";
  public static final String SIZE_MUST_BE_BETWEEN_1_AND_200 = "size must be between 1 and 200";
  public static final String UNABLE_TO_DETECT_LEARNING_STANDARD = "Unable to detect learning standard";
  public static final String UNEXPECTED_SERVER_ERROR = "Unexpected server error";
  public static final String USERNAME_ALREADY_EXISTS = "Username already exists";
  public static final String USER_NOT_FOUND = "User not found";
  public static final String VALIDATION_FAILED = "Validation failed";

  private ErrorMessages() {
  }
}
