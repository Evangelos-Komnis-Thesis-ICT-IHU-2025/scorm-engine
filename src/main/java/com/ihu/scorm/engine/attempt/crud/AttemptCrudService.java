package com.ihu.scorm.engine.attempt.crud;

import com.ihu.scorm.engine.attempt.AttemptDatabaseFieldConstants;
import com.ihu.scorm.engine.attempt.persist.GetAttemptsQuery;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class AttemptCrudService {

  private final AttemptCrudRepository attemptCrudRepository;
  private final AttemptRuntimeSnapshotCrudRepository snapshotRepository;

  public AttemptCrudService(AttemptCrudRepository attemptCrudRepository,
      AttemptRuntimeSnapshotCrudRepository snapshotRepository) {
    this.attemptCrudRepository = attemptCrudRepository;
    this.snapshotRepository = snapshotRepository;
  }

  public AttemptEntity save(AttemptEntity entity) {
    return attemptCrudRepository.save(entity);
  }

  public AttemptRuntimeSnapshotEntity saveSnapshot(AttemptRuntimeSnapshotEntity snapshotEntity) {
    return snapshotRepository.save(snapshotEntity);
  }

  public AttemptEntity findById(UUID attemptId) {
    return attemptCrudRepository.findById(attemptId).orElse(null);
  }

  public AttemptEntity findActiveByEnrollmentId(UUID enrollmentId) {
    return attemptCrudRepository.findFirstByEnrollmentIdAndStatusOrderByCreatedAtDesc(enrollmentId, AttemptStatus.IN_PROGRESS)
        .orElse(null);
  }

  public Integer findLastAttemptNo(UUID enrollmentId) {
    return attemptCrudRepository.findTopByEnrollmentIdOrderByAttemptNoDesc(enrollmentId)
        .map(AttemptEntity::getAttemptNo)
        .orElse(0);
  }

  public Page<AttemptEntity> list(GetAttemptsQuery query) {
    Pageable pageable = PageRequest.of(
        query.pagination().page(),
        query.pagination().size(),
        Sort.by(Sort.Direction.DESC, AttemptDatabaseFieldConstants.CREATED_AT));
    Specification<AttemptEntity> spec = Specification.where(null);
    if (query.userId() != null) {
      spec = spec.and((root, cq, cb) -> cb.equal(root.get(AttemptDatabaseFieldConstants.USER_ID), query.userId()));
    }
    if (query.courseId() != null) {
      spec = spec.and((root, cq, cb) -> cb.equal(root.get(AttemptDatabaseFieldConstants.COURSE_ID), query.courseId()));
    }
    return attemptCrudRepository.findAll(spec, pageable);
  }

  public long countByCourseId(UUID courseId) {
    return attemptCrudRepository.countByCourseId(courseId);
  }

  public long countInProgressByCourseId(UUID courseId) {
    return attemptCrudRepository.countByCourseIdAndStatus(courseId, AttemptStatus.IN_PROGRESS);
  }

  public long countCompletedByCourseId(UUID courseId) {
    return attemptCrudRepository.countByCourseIdAndCompletionStatus(courseId, CompletionStatus.COMPLETE);
  }

  public Double averageTotalTimeSeconds(UUID courseId) {
    return attemptCrudRepository.avgTotalTimeSecondsByCourseId(courseId);
  }

  public Double averageScoreRaw(UUID courseId) {
    return attemptCrudRepository.avgScoreRawByCourseId(courseId);
  }
}
