package com.ihu.scorm.engine.enrollment.crud;

import com.ihu.scorm.engine.common.persistence.BaseEntity;
import com.ihu.scorm.engine.enrollment.EnrollmentDatabaseFieldConstants;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.UUID;

@Entity
@Table(name = EnrollmentDatabaseFieldConstants.TABLE_NAME, uniqueConstraints = {
    @UniqueConstraint(
        name = EnrollmentDatabaseFieldConstants.UNIQUE_CONSTRAINT_USER_COURSE,
        columnNames = {EnrollmentDatabaseFieldConstants.USER_ID, EnrollmentDatabaseFieldConstants.COURSE_ID})
})
public class EnrollmentEntity extends BaseEntity {

  @Column(nullable = false)
  private UUID userId;

  @Column(nullable = false)
  private UUID courseId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private EnrollmentStatus status;

  public UUID getUserId() {
    return userId;
  }

  public void setUserId(UUID userId) {
    this.userId = userId;
  }

  public UUID getCourseId() {
    return courseId;
  }

  public void setCourseId(UUID courseId) {
    this.courseId = courseId;
  }

  public EnrollmentStatus getStatus() {
    return status;
  }

  public void setStatus(EnrollmentStatus status) {
    this.status = status;
  }
}
