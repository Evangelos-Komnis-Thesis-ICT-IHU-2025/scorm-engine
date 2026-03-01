package com.ihu.scorm.engine.course.mapper;

import com.ihu.scorm.engine.common.query.PageResult;
import com.ihu.scorm.engine.course.api.CourseDto;
import com.ihu.scorm.engine.course.crud.CourseEntity;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CourseApiMapper {

  CourseDto toDto(CourseEntity entity);

  List<CourseDto> toDtos(List<CourseEntity> entities);

  default PageResult<CourseDto> toPageResult(PageResult<CourseEntity> page) {
    return new PageResult<>(
        toDtos(page.items()),
        page.page(),
        page.size(),
        page.totalItems(),
        page.totalPages());
  }
}
