package com.ihu.scorm.engine.attempt.persist;

import com.ihu.scorm.engine.common.query.BasePaginationQuery;
import com.ihu.scorm.engine.common.query.BaseQuery;
import java.time.Instant;
import java.util.EnumSet;
import java.util.UUID;

public final class GetAttemptsQuery extends BaseQuery<AttemptEmbed> {

  private final UUID userId;
  private final UUID courseId;

  public GetAttemptsQuery(BasePaginationQuery pagination, UUID userId, UUID courseId,
      EnumSet<AttemptEmbed> embeds, Instant requestTime) {
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
