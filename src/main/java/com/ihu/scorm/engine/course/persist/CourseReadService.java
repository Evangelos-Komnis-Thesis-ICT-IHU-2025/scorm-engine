package com.ihu.scorm.engine.course.persist;

import com.ihu.scorm.engine.common.constant.DetailKey;
import com.ihu.scorm.engine.common.error.ErrorCode;
import com.ihu.scorm.engine.common.error.ErrorMessages;
import com.ihu.scorm.engine.common.error.NotFoundException;
import com.ihu.scorm.engine.course.crud.CourseCrudService;
import com.ihu.scorm.engine.course.crud.CourseEntity;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class CourseReadService {

  private final CourseCrudService courseCrudService;

  public CourseReadService(CourseCrudService courseCrudService) {
    this.courseCrudService = courseCrudService;
  }

  public CourseEntity getRequired(UUID courseId) {
    CourseEntity entity = courseCrudService.getRequired(courseId);
    if (entity == null) {
      throw new NotFoundException(
          ErrorCode.COURSE_NOT_FOUND,
          ErrorMessages.COURSE_NOT_FOUND,
          Map.of(DetailKey.COURSE_ID, courseId));
    }
    return entity;
  }

  public Page<CourseEntity> getPage(GetCoursesQuery query) {
    return courseCrudService.findPage(query);
  }
}
