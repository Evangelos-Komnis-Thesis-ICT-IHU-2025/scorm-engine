package com.ihu.scorm.engine.common.constant;

public final class SystemMessages {

  public static final String FAILED_TO_COMPUTE_MANIFEST_HASH = "Failed to compute manifest hash";
  public static final String FAILED_TO_CREATE_PRESIGNED_URL = "Failed to create presigned URL";
  public static final String FAILED_TO_DESERIALIZE_RUNTIME_STATE = "Failed to deserialize runtime state";
  public static final String FAILED_TO_HASH_LAUNCH_TOKEN = "Failed to hash launch token";
  public static final String FAILED_TO_INITIALIZE_MINIO_BUCKET = "Failed to initialize MinIO bucket ";
  public static final String FAILED_TO_PUT_OBJECT_TO_MINIO = "Failed to put object to MinIO";
  public static final String FAILED_TO_SERIALIZE_METADATA = "Failed to serialize metadata";
  public static final String FAILED_TO_SERIALIZE_RUNTIME_STATE = "Failed to serialize runtime state";
  public static final String FAILED_TO_SERIALIZE_SNAPSHOT_PAYLOAD = "Failed to serialize snapshot payload";
  public static final String INVALID_ATTEMPT_STATUS_TRANSITION = "Invalid attempt status transition";
  public static final String INVALID_LAUNCH_STATUS_TRANSITION = "Invalid launch status transition";
  public static final String INVALID_PRESIGNED_URL = "Invalid presigned URL";
  public static final String INVALID_TOKEN = "Invalid token";

  private SystemMessages() {
  }
}
