package com.ihu.scorm.engine.runtime.adapter;

import com.ihu.scorm.engine.common.error.ErrorMessages;
import com.ihu.scorm.engine.standard.LearningStandard;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class StandardAdapterRegistry {

  private final Map<LearningStandard, StandardAdapter> adapters;

  public StandardAdapterRegistry(java.util.List<StandardAdapter> adapters) {
    this.adapters = adapters.stream().collect(Collectors.toMap(StandardAdapter::standard, Function.identity()));
  }

  public StandardAdapter getRequired(LearningStandard standard) {
    StandardAdapter adapter = adapters.get(standard);
    if (adapter == null) {
      throw new IllegalArgumentException(ErrorMessages.NO_ADAPTER_REGISTERED_FOR_STANDARD + standard);
    }
    return adapter;
  }
}
