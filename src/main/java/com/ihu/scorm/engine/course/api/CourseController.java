package com.ihu.scorm.engine.course.api;

import com.ihu.scorm.engine.common.query.BasePaginationQuery;
import com.ihu.scorm.engine.common.query.PageResult;
import com.ihu.scorm.engine.course.mapper.CourseApiMapper;
import com.ihu.scorm.engine.course.orchestrator.CourseOrchestrator;
import com.ihu.scorm.engine.course.orchestrator.ImportCourseCommand;
import com.ihu.scorm.engine.course.persist.CourseEmbed;
import com.ihu.scorm.engine.course.persist.GetCoursesQuery;
import com.ihu.scorm.engine.standard.LearningStandard;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.io.IOException;
import java.time.Instant;
import java.util.EnumSet;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/courses")
@Validated
public class CourseController {

  private final CourseOrchestrator courseOrchestrator;
  private final CourseApiMapper mapper;

  public CourseController(CourseOrchestrator courseOrchestrator, CourseApiMapper mapper) {
    this.courseOrchestrator = courseOrchestrator;
    this.mapper = mapper;
  }

  @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<CourseDto> importCourse(
      @RequestPart("file") MultipartFile file,
      @RequestPart(value = "code", required = false) String code,
      @RequestPart(value = "versionLabel", required = false) String versionLabel) throws IOException {

    ImportCourseCommand command = new ImportCourseCommand(
        file.getBytes(),
        file.getOriginalFilename(),
        file.getContentType(),
        code,
        versionLabel);

    CourseDto dto = mapper.toDto(courseOrchestrator.importCourse(command));
    return ResponseEntity.status(HttpStatus.CREATED).body(dto);
  }

  @GetMapping
  public ResponseEntity<PageResult<CourseDto>> listCourses(
      @RequestParam(defaultValue = "0") @Min(0) int page,
      @RequestParam(defaultValue = "20") @Min(1) @Max(200) int size,
      @RequestParam(defaultValue = "createdAt,desc") String sort,
      @RequestParam(required = false) String search,
      @RequestParam(required = false) LearningStandard standard) {

    GetCoursesQuery query = new GetCoursesQuery(
        new BasePaginationQuery(page, size, sort),
        search,
        standard,
        EnumSet.noneOf(CourseEmbed.class),
        Instant.now());

    return ResponseEntity.ok(mapper.toPageResult(courseOrchestrator.listCourses(query)));
  }

  @GetMapping("/{courseId}")
  public ResponseEntity<CourseDto> getCourse(@PathVariable UUID courseId) {
    return ResponseEntity.ok(mapper.toDto(courseOrchestrator.getCourse(courseId)));
  }
}
