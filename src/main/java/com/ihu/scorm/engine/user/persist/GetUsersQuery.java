package com.ihu.scorm.engine.user.persist;

import com.ihu.scorm.engine.common.query.BasePaginationQuery;
import com.ihu.scorm.engine.common.query.BaseQuery;
import java.time.Instant;
import java.util.EnumSet;

public final class GetUsersQuery extends BaseQuery<UserEmbed> {

  private final String search;

  public GetUsersQuery(BasePaginationQuery pagination, String search, EnumSet<UserEmbed> embeds, Instant requestTime) {
    super(pagination, embeds, requestTime);
    this.search = search;
  }

  public String search() {
    return search;
  }
}
