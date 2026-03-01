package com.ihu.scorm.engine.common.storage;

import com.ihu.scorm.engine.common.constant.SystemMessages;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import org.springframework.stereotype.Component;

@Component
public class MinioObjectStorage implements ObjectStorage {

  private final MinioClient minioClient;

  public MinioObjectStorage(MinioClient minioClient) {
    this.minioClient = minioClient;
  }

  @Override
  public void putObject(String bucket, String objectKey, InputStream data, long size, String contentType) {
    try {
      minioClient.putObject(
          PutObjectArgs.builder()
              .bucket(bucket)
              .object(objectKey)
              .stream(data, size, -1)
              .contentType(contentType)
              .build());
    } catch (Exception exception) {
      throw new IllegalStateException(SystemMessages.FAILED_TO_PUT_OBJECT_TO_MINIO, exception);
    }
  }

  @Override
  public URL getPresignedGetUrl(String bucket, String objectKey, int ttlMinutes) {
    try {
      String url = minioClient.getPresignedObjectUrl(
          GetPresignedObjectUrlArgs.builder()
              .bucket(bucket)
              .object(objectKey)
              .expiry(ttlMinutes * 60)
              .method(Method.GET)
              .build());
      return new URL(url);
    } catch (MalformedURLException exception) {
      throw new IllegalStateException(SystemMessages.INVALID_PRESIGNED_URL, exception);
    } catch (Exception exception) {
      throw new IllegalStateException(SystemMessages.FAILED_TO_CREATE_PRESIGNED_URL, exception);
    }
  }
}
