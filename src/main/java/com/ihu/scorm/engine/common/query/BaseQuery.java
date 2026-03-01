package com.ihu.scorm.engine.common.query;

import java.time.Instant;
import java.util.EnumSet;

public abstract class BaseQuery<E extends Enum<E>> {

  private final BasePaginationQuery pagination;
  private final EnumSet<E> embeds;
  private final Instant requestTime;

  protected BaseQuery(BasePaginationQuery pagination, EnumSet<E> embeds, Instant requestTime) {
    this.pagination = pagination;
    this.embeds = embeds;
    this.requestTime = requestTime;
  }

  public BasePaginationQuery pagination() {
    return pagination;
  }

  public EnumSet<E> embeds() {
    return embeds;
  }

  public Instant requestTime() {
    return requestTime;
  }
}
