package com.ihu.scorm.engine.course.orchestrator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ihu.scorm.engine.course.crud.CourseEntity;
import com.ihu.scorm.engine.course.persist.CourseReadService;
import org.junit.jupiter.api.Test;

class CourseOrchestratorDelegationTest {

  @Test
  void importCourseDelegatesToCommandHandler() {
    ImportCourseCommandHandler handler = mock(ImportCourseCommandHandler.class);
    CourseReadService readService = mock(CourseReadService.class);
    CourseOrchestrator orchestrator = new CourseOrchestrator(handler, readService);

    ImportCourseCommand command = new ImportCourseCommand(new byte[] {1}, "course.zip", "application/zip", "C-1", "v1");
    CourseEntity expected = new CourseEntity();
    when(handler.handle(command)).thenReturn(expected);

    CourseEntity actual = orchestrator.importCourse(command);

    assertThat(actual).isSameAs(expected);
    verify(handler).handle(command);
  }
}
