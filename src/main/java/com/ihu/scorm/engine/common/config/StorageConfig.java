package com.ihu.scorm.engine.common.config;

import com.ihu.scorm.engine.common.constant.SystemMessages;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {

  @Bean
  public MinioClient minioClient(StorageProperties storageProperties) {
    MinioClient client = MinioClient.builder()
        .endpoint(storageProperties.endpoint())
        .credentials(storageProperties.accessKey(), storageProperties.secretKey())
        .build();
    ensureBucket(client, storageProperties.bucketPackages());
    ensureBucket(client, storageProperties.bucketExtracted());
    return client;
  }

  private void ensureBucket(MinioClient client, String bucketName) {
    if (bucketName == null || bucketName.isBlank()) {
      return;
    }
    try {
      boolean exists = client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
      if (!exists) {
        client.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
      }
    } catch (Exception exception) {
      throw new IllegalStateException(SystemMessages.FAILED_TO_INITIALIZE_MINIO_BUCKET + bucketName, exception);
    }
  }
}
