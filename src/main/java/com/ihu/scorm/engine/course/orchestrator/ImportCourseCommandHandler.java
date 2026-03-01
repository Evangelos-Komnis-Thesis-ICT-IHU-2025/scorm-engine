package com.ihu.scorm.engine.course.orchestrator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ihu.scorm.engine.common.command.CommandHandler;
import com.ihu.scorm.engine.common.config.StorageProperties;
import com.ihu.scorm.engine.common.constant.SystemMessages;
import com.ihu.scorm.engine.common.error.ErrorCode;
import com.ihu.scorm.engine.common.error.ErrorMessages;
import com.ihu.scorm.engine.common.error.ValidationException;
import com.ihu.scorm.engine.common.storage.ObjectStorage;
import com.ihu.scorm.engine.course.crud.CourseEntity;
import com.ihu.scorm.engine.course.domain.CourseManifestData;
import com.ihu.scorm.engine.course.domain.ManifestParser;
import com.ihu.scorm.engine.course.persist.CoursePersistService;
import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ImportCourseCommandHandler implements CommandHandler<ImportCourseCommand, CourseEntity> {

  private static final Logger LOGGER = LoggerFactory.getLogger(ImportCourseCommandHandler.class);

  private final ManifestParser manifestParser;
  private final CoursePersistService coursePersistService;
  private final ObjectStorage objectStorage;
  private final ObjectMapper objectMapper;
  private final StorageProperties storageProperties;

  public ImportCourseCommandHandler(
      ManifestParser manifestParser,
      CoursePersistService coursePersistService,
      ObjectStorage objectStorage,
      ObjectMapper objectMapper,
      StorageProperties storageProperties) {
    this.manifestParser = manifestParser;
    this.coursePersistService = coursePersistService;
    this.objectStorage = objectStorage;
    this.objectMapper = objectMapper;
    this.storageProperties = storageProperties;
  }

  @Override
  public CourseEntity handle(ImportCourseCommand command) {
    validateImport(command);
    CourseManifestData manifestData = manifestParser.parse(command.fileBytes());
    String manifestHash = sha256(command.fileBytes());

    LOGGER.info("course_import_start filename={} size={} manifestHash={} standard={}",
        command.originalFilename(), command.fileBytes().length, manifestHash, manifestData.standard());

    CourseEntity entity = new CourseEntity();
    entity.setCode(command.code() == null || command.code().isBlank()
        ? "CRS-" + manifestHash.substring(0, 8)
        : command.code().trim());
    entity.setTitle(manifestData.title());
    entity.setDescription(manifestData.description());
    entity.setStandard(manifestData.standard());
    entity.setVersionLabel(command.versionLabel());
    entity.setEntrypointPath(manifestData.entrypointPath());
    entity.setManifestHash(manifestHash);
    entity.setMetadataJson(toJson(manifestData.metadata()));
    entity.setStorageBucket(storageProperties.bucketPackages());
    entity.setStorageObjectKeyZip("pending");

    CourseEntity created = coursePersistService.save(entity);

    String objectKey = "packages/" + created.getId() + "/" + manifestHash + ".zip";
    objectStorage.putObject(
        storageProperties.bucketPackages(),
        objectKey,
        new ByteArrayInputStream(command.fileBytes()),
        command.fileBytes().length,
        command.contentType() == null ? "application/zip" : command.contentType());

    created.setStorageObjectKeyZip(objectKey);
    CourseEntity saved = coursePersistService.save(created);

    LOGGER.info("course_import_completed courseId={} filename={} objectKey={}",
        saved.getId(), command.originalFilename(), objectKey);
    return saved;
  }

  private void validateImport(ImportCourseCommand command) {
    if (command.fileBytes() == null || command.fileBytes().length == 0) {
      throw new ValidationException(ErrorCode.INVALID_FILE, ErrorMessages.COURSE_FILE_IS_EMPTY, Map.of());
    }
    String lowerName = command.originalFilename() == null ? "" : command.originalFilename().toLowerCase();
    if (!lowerName.endsWith(".zip")) {
      throw new ValidationException(
          ErrorCode.UNSUPPORTED_MEDIA_TYPE,
          ErrorMessages.ONLY_ZIP_FILES_ARE_SUPPORTED,
          Map.of());
    }
  }

  private String sha256(byte[] bytes) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      return HexFormat.of().formatHex(digest.digest(bytes));
    } catch (Exception exception) {
      throw new IllegalStateException(SystemMessages.FAILED_TO_COMPUTE_MANIFEST_HASH, exception);
    }
  }

  private String toJson(Map<String, Object> metadata) {
    try {
      return objectMapper.writeValueAsString(metadata);
    } catch (JsonProcessingException exception) {
      throw new IllegalStateException(SystemMessages.FAILED_TO_SERIALIZE_METADATA, exception);
    }
  }
}
