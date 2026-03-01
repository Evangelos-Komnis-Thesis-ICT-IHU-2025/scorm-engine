package com.ihu.scorm.engine.attempt.crud;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface AttemptCrudRepository extends JpaRepository<AttemptEntity, UUID>, JpaSpecificationExecutor<AttemptEntity> {

  Optional<AttemptEntity> findFirstByEnrollmentIdAndStatusOrderByCreatedAtDesc(UUID enrollmentId, AttemptStatus status);

  Optional<AttemptEntity> findTopByEnrollmentIdOrderByAttemptNoDesc(UUID enrollmentId);

  long countByCourseId(UUID courseId);

  long countByCourseIdAndStatus(UUID courseId, AttemptStatus status);

  long countByCourseIdAndCompletionStatus(UUID courseId, CompletionStatus completionStatus);

  @Query("select avg(a.totalTimeSeconds) from AttemptEntity a where a.courseId = :courseId")
  Double avgTotalTimeSecondsByCourseId(UUID courseId);

  @Query("select avg(a.scoreRaw) from AttemptEntity a where a.courseId = :courseId and a.scoreRaw is not null")
  Double avgScoreRawByCourseId(UUID courseId);
}
