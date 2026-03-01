package com.ihu.scorm.engine.enrollment.crud;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EnrollmentCrudRepository extends JpaRepository<EnrollmentEntity, UUID>, JpaSpecificationExecutor<EnrollmentEntity> {

  boolean existsByUserIdAndCourseId(UUID userId, UUID courseId);

  Optional<EnrollmentEntity> findByUserIdAndCourseId(UUID userId, UUID courseId);

  long countByCourseId(UUID courseId);
}
