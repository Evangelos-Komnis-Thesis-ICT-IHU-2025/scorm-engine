package com.ihu.scorm.engine.enrollment.crud;

import com.ihu.scorm.engine.enrollment.EnrollmentDatabaseFieldConstants;
import com.ihu.scorm.engine.enrollment.persist.GetEnrollmentsQuery;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentCrudService {

  private final EnrollmentCrudRepository repository;

  public EnrollmentCrudService(EnrollmentCrudRepository repository) {
    this.repository = repository;
  }

  public EnrollmentEntity save(EnrollmentEntity entity) {
    return repository.save(entity);
  }

  public boolean exists(UUID userId, UUID courseId) {
    return repository.existsByUserIdAndCourseId(userId, courseId);
  }

  public EnrollmentEntity findByUserCourse(UUID userId, UUID courseId) {
    return repository.findByUserIdAndCourseId(userId, courseId).orElse(null);
  }

  public Page<EnrollmentEntity> findPage(GetEnrollmentsQuery query) {
    Sort sort = Sort.by(Sort.Direction.DESC, EnrollmentDatabaseFieldConstants.CREATED_AT);
    Pageable pageable = PageRequest.of(query.pagination().page(), query.pagination().size(), sort);

    Specification<EnrollmentEntity> spec = Specification.where(null);
    if (query.userId() != null) {
      spec = spec.and((root, cq, cb) -> cb.equal(root.get(EnrollmentDatabaseFieldConstants.USER_ID), query.userId()));
    }
    if (query.courseId() != null) {
      spec = spec.and((root, cq, cb) -> cb.equal(root.get(EnrollmentDatabaseFieldConstants.COURSE_ID), query.courseId()));
    }
    return repository.findAll(spec, pageable);
  }

  public long countByCourseId(UUID courseId) {
    return repository.countByCourseId(courseId);
  }
}
