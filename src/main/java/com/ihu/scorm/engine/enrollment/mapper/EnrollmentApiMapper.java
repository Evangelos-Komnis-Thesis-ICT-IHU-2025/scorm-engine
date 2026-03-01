package com.ihu.scorm.engine.enrollment.mapper;

import com.ihu.scorm.engine.common.query.PageResult;
import com.ihu.scorm.engine.enrollment.api.CreateEnrollmentForm;
import com.ihu.scorm.engine.enrollment.api.EnrollmentDto;
import com.ihu.scorm.engine.enrollment.crud.EnrollmentEntity;
import com.ihu.scorm.engine.enrollment.orchestrator.CreateEnrollmentCommand;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface EnrollmentApiMapper {

  CreateEnrollmentCommand toCommand(CreateEnrollmentForm form);

  EnrollmentDto toDto(EnrollmentEntity entity);

  List<EnrollmentDto> toDtos(List<EnrollmentEntity> entities);

  default PageResult<EnrollmentDto> toPageResult(PageResult<EnrollmentEntity> pageResult) {
    return new PageResult<>(
        toDtos(pageResult.items()),
        pageResult.page(),
        pageResult.size(),
        pageResult.totalItems(),
        pageResult.totalPages());
  }
}
