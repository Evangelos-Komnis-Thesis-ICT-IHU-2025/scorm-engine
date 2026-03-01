package com.ihu.scorm.engine.launch.mapper;

import com.ihu.scorm.engine.launch.api.CreateLaunchForm;
import com.ihu.scorm.engine.launch.orchestrator.CreateLaunchCommand;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface LaunchApiMapper {

  @Mapping(target = "forceNewAttempt", expression = "java(form.forceNewAttempt() != null && form.forceNewAttempt())")
  CreateLaunchCommand toCommand(CreateLaunchForm form);
}
