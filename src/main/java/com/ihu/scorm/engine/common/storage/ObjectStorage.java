package com.ihu.scorm.engine.common.storage;

import java.io.InputStream;
import java.net.URL;

public interface ObjectStorage {

  void putObject(String bucket, String objectKey, InputStream data, long size, String contentType);

  URL getPresignedGetUrl(String bucket, String objectKey, int ttlMinutes);
}
