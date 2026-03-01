package com.ihu.scorm.engine.enrollment.persist;

import com.ihu.scorm.engine.common.query.BasePaginationQuery;
import com.ihu.scorm.engine.common.query.BaseQuery;
import java.time.Instant;
import java.util.EnumSet;
import java.util.UUID;

public final class GetEnrollmentsQuery extends BaseQuery<EnrollmentEmbed> {

  private final UUID userId;
  private final UUID courseId;

  public GetEnrollmentsQuery(
      BasePaginationQuery pagination,
      UUID userId,
      UUID courseId,
      EnumSet<EnrollmentEmbed> embeds,
      Instant requestTime) {
    super(pagination, embeds, requestTime);
    this.userId = userId;
    this.courseId = courseId;
  }

  public UUID userId() {
    return userId;
  }

  public UUID courseId() {
    return courseId;
  }
}
