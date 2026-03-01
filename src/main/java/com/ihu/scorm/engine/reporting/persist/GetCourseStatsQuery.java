package com.ihu.scorm.engine.reporting.persist;

import com.ihu.scorm.engine.common.query.BasePaginationQuery;
import com.ihu.scorm.engine.common.query.BaseQuery;
import java.time.Instant;
import java.util.EnumSet;
import java.util.UUID;

public final class GetCourseStatsQuery extends BaseQuery<GetCourseStatsQuery.StatsEmbed> {

  public enum StatsEmbed {
    NONE
  }

  private final UUID courseId;

  public GetCourseStatsQuery(UUID courseId) {
    super(new BasePaginationQuery(0, 1, "createdAt,desc"), EnumSet.noneOf(StatsEmbed.class), Instant.now());
    this.courseId = courseId;
  }

  public UUID courseId() {
    return courseId;
  }
}
