package com.ihu.scorm.engine.user.mapper;

import com.ihu.scorm.engine.common.query.PageResult;
import com.ihu.scorm.engine.user.api.CreateUserForm;
import com.ihu.scorm.engine.user.api.UserDto;
import com.ihu.scorm.engine.user.crud.UserEntity;
import com.ihu.scorm.engine.user.orchestrator.CreateUserCommand;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserApiMapper {

  CreateUserCommand toCommand(CreateUserForm form);

  UserDto toDto(UserEntity entity);

  List<UserDto> toDtos(List<UserEntity> entities);

  default PageResult<UserDto> toPageResult(PageResult<UserEntity> pageResult) {
    return new PageResult<>(
        toDtos(pageResult.items()),
        pageResult.page(),
        pageResult.size(),
        pageResult.totalItems(),
        pageResult.totalPages());
  }
}
