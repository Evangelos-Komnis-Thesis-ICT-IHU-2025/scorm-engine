package com.ihu.scorm.engine.course.persist;

import com.ihu.scorm.engine.common.query.BasePaginationQuery;
import com.ihu.scorm.engine.common.query.BaseQuery;
import com.ihu.scorm.engine.standard.LearningStandard;
import java.time.Instant;
import java.util.EnumSet;

public final class GetCoursesQuery extends BaseQuery<CourseEmbed> {

  private final String search;
  private final LearningStandard standard;

  public GetCoursesQuery(BasePaginationQuery pagination, String search, LearningStandard standard,
      EnumSet<CourseEmbed> embeds, Instant requestTime) {
    super(pagination, embeds, requestTime);
    this.search = search;
    this.standard = standard;
  }

  public String search() {
    return search;
  }

  public LearningStandard standard() {
    return standard;
  }
}
