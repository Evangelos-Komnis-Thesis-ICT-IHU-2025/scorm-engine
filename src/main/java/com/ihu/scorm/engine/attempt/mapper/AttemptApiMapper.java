package com.ihu.scorm.engine.attempt.mapper;

import com.ihu.scorm.engine.attempt.api.AttemptDto;
import com.ihu.scorm.engine.attempt.crud.AttemptEntity;
import com.ihu.scorm.engine.common.query.PageResult;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AttemptApiMapper {

  AttemptDto toDto(AttemptEntity entity);

  List<AttemptDto> toDtos(List<AttemptEntity> entities);

  default PageResult<AttemptDto> toPageResult(PageResult<AttemptEntity> pageResult) {
    return new PageResult<>(
        toDtos(pageResult.items()),
        pageResult.page(),
        pageResult.size(),
        pageResult.totalItems(),
        pageResult.totalPages());
  }
}
