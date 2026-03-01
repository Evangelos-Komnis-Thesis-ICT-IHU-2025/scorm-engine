package com.ihu.scorm.engine.course.crud;

import com.ihu.scorm.engine.common.persistence.BaseEntity;
import com.ihu.scorm.engine.course.CourseDatabaseFieldConstants;
import com.ihu.scorm.engine.standard.LearningStandard;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = CourseDatabaseFieldConstants.TABLE_NAME)
public class CourseEntity extends BaseEntity {

  @Column(unique = true)
  private String code;

  @Column(nullable = false)
  private String title;

  @Column(columnDefinition = "text")
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private LearningStandard standard;

  @Column
  private String versionLabel;

  @Column(nullable = false)
  private String entrypointPath;

  @Column(nullable = false)
  private String manifestHash;

  @Column(columnDefinition = "jsonb")
  private String metadataJson;

  @Column(nullable = false)
  private String storageBucket;

  @Column(nullable = false)
  private String storageObjectKeyZip;

  @Column
  private String storageObjectKeyExtractedPrefix;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public LearningStandard getStandard() {
    return standard;
  }

  public void setStandard(LearningStandard standard) {
    this.standard = standard;
  }

  public String getVersionLabel() {
    return versionLabel;
  }

  public void setVersionLabel(String versionLabel) {
    this.versionLabel = versionLabel;
  }

  public String getEntrypointPath() {
    return entrypointPath;
  }

  public void setEntrypointPath(String entrypointPath) {
    this.entrypointPath = entrypointPath;
  }

  public String getManifestHash() {
    return manifestHash;
  }

  public void setManifestHash(String manifestHash) {
    this.manifestHash = manifestHash;
  }

  public String getMetadataJson() {
    return metadataJson;
  }

  public void setMetadataJson(String metadataJson) {
    this.metadataJson = metadataJson;
  }

  public String getStorageBucket() {
    return storageBucket;
  }

  public void setStorageBucket(String storageBucket) {
    this.storageBucket = storageBucket;
  }

  public String getStorageObjectKeyZip() {
    return storageObjectKeyZip;
  }

  public void setStorageObjectKeyZip(String storageObjectKeyZip) {
    this.storageObjectKeyZip = storageObjectKeyZip;
  }

  public String getStorageObjectKeyExtractedPrefix() {
    return storageObjectKeyExtractedPrefix;
  }

  public void setStorageObjectKeyExtractedPrefix(String storageObjectKeyExtractedPrefix) {
    this.storageObjectKeyExtractedPrefix = storageObjectKeyExtractedPrefix;
  }
}
