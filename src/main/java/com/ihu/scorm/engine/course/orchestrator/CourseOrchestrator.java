package com.ihu.scorm.engine.course.orchestrator;

import com.ihu.scorm.engine.common.query.PageResult;
import com.ihu.scorm.engine.course.crud.CourseEntity;
import com.ihu.scorm.engine.course.persist.CourseReadService;
import com.ihu.scorm.engine.course.persist.GetCoursesQuery;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class CourseOrchestrator {

  private final ImportCourseCommandHandler importCourseCommandHandler;
  private final CourseReadService courseReadService;

  public CourseOrchestrator(
      ImportCourseCommandHandler importCourseCommandHandler,
      CourseReadService courseReadService) {
    this.importCourseCommandHandler = importCourseCommandHandler;
    this.courseReadService = courseReadService;
  }

  public CourseEntity importCourse(ImportCourseCommand command) {
    return importCourseCommandHandler.handle(command);
  }

  public CourseEntity getCourse(UUID courseId) {
    return courseReadService.getRequired(courseId);
  }

  public PageResult<CourseEntity> listCourses(GetCoursesQuery query) {
    Page<CourseEntity> page = courseReadService.getPage(query);
    return new PageResult<>(
        page.getContent(),
        page.getNumber(),
        page.getSize(),
        page.getTotalElements(),
        page.getTotalPages());
  }
}
