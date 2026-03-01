package com.ihu.scorm.engine.common.query;

import java.util.List;

public record PageResult<T>(List<T> items, int page, int size, long totalItems, int totalPages) {
}
