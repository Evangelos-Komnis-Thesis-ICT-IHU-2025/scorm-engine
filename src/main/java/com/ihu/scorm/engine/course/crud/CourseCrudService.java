package com.ihu.scorm.engine.course.crud;

import com.ihu.scorm.engine.course.CourseDatabaseFieldConstants;
import com.ihu.scorm.engine.course.persist.GetCoursesQuery;
import com.ihu.scorm.engine.standard.LearningStandard;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class CourseCrudService {

  private final CourseCrudRepository repository;

  public CourseCrudService(CourseCrudRepository repository) {
    this.repository = repository;
  }

  public CourseEntity save(CourseEntity entity) {
    return repository.save(entity);
  }

  public CourseEntity getRequired(UUID id) {
    return repository.findById(id).orElse(null);
  }

  public Page<CourseEntity> findPage(GetCoursesQuery query) {
    Sort sort = Sort.by(Sort.Direction.DESC, CourseDatabaseFieldConstants.CREATED_AT);
    String sortRaw = query.pagination().sort();
    if (sortRaw != null && !sortRaw.isBlank()) {
      String[] parts = sortRaw.split(",");
      if (parts.length == 2) {
        sort = Sort.by(Sort.Direction.fromString(parts[1]), parts[0]);
      }
    }
    Pageable pageable = PageRequest.of(query.pagination().page(), query.pagination().size(), sort);

    Specification<CourseEntity> spec = Specification.where(null);
    if (query.search() != null && !query.search().isBlank()) {
      String search = "%" + query.search().toLowerCase() + "%";
      spec = spec.and((root, cq, cb) -> cb.or(
          cb.like(cb.lower(root.get(CourseDatabaseFieldConstants.TITLE)), search),
          cb.like(cb.lower(root.get(CourseDatabaseFieldConstants.CODE)), search)));
    }
    LearningStandard standard = query.standard();
    if (standard != null) {
      spec = spec.and((root, cq, cb) -> cb.equal(root.get(CourseDatabaseFieldConstants.STANDARD), standard));
    }
    return repository.findAll(spec, pageable);
  }
}
