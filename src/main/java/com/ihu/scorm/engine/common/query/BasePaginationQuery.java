package com.ihu.scorm.engine.common.query;

import com.ihu.scorm.engine.common.error.ErrorMessages;

public record BasePaginationQuery(int page, int size, String sort) {

  private static final int MAX_SIZE = 200;

  public BasePaginationQuery {
    if (page < 0) {
      throw new IllegalArgumentException(ErrorMessages.PAGE_MUST_BE_GREATER_OR_EQUAL_TO_ZERO);
    }
    if (size <= 0 || size > MAX_SIZE) {
      throw new IllegalArgumentException(ErrorMessages.SIZE_MUST_BE_BETWEEN_1_AND_200);
    }
  }

  public static BasePaginationQuery defaultPage() {
    return new BasePaginationQuery(
        PaginationConstants.DEFAULT_PAGE,
        PaginationConstants.DEFAULT_SIZE,
        PaginationConstants.DEFAULT_SORT);
  }
}
