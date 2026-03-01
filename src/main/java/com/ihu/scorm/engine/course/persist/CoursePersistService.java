package com.ihu.scorm.engine.course.persist;

import com.ihu.scorm.engine.course.crud.CourseCrudService;
import com.ihu.scorm.engine.course.crud.CourseEntity;
import org.springframework.stereotype.Service;

@Service
public class CoursePersistService {

  private final CourseCrudService courseCrudService;

  public CoursePersistService(CourseCrudService courseCrudService) {
    this.courseCrudService = courseCrudService;
  }

  public CourseEntity save(CourseEntity entity) {
    return courseCrudService.save(entity);
  }
}
