package com.ihu.scorm.engine.runtime.state;

import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class ObservedRuntimeStateStore implements RuntimeStateStore {

  private static final Logger LOGGER = LoggerFactory.getLogger(ObservedRuntimeStateStore.class);

  private final RuntimeStateStore delegate;
  private final MeterRegistry meterRegistry;
  private final DistributionSummary findDirtyResultSize;

  public ObservedRuntimeStateStore(
      RedisRuntimeStateStore delegate,
      MeterRegistry meterRegistry) {
    this.delegate = delegate;
    this.meterRegistry = meterRegistry;
    this.findDirtyResultSize = DistributionSummary.builder("scorm.runtime_state_store.find_dirty.result_size")
        .description("Number of runtime states returned by findDirtyBefore")
        .register(meterRegistry);
  }

  @Override
  public LaunchRuntimeState get(UUID launchId) {
    return observe("get", () -> delegate.get(launchId));
  }

  @Override
  public void save(LaunchRuntimeState state, int ttlSeconds) {
    observe("save", () -> {
      delegate.save(state, ttlSeconds);
      return null;
    });
  }

  @Override
  public void delete(UUID launchId) {
    observe("delete", () -> {
      delegate.delete(launchId);
      return null;
    });
  }

  @Override
  public List<LaunchRuntimeState> findDirtyBefore(Instant lastSeenBefore) {
    List<LaunchRuntimeState> states = observe("findDirtyBefore", () -> delegate.findDirtyBefore(lastSeenBefore));
    findDirtyResultSize.record(states.size());
    return states;
  }

  private <T> T observe(String operation, Supplier<T> supplier) {
    long startedAtNanos = System.nanoTime();
    String outcome = "success";
    try {
      return supplier.get();
    } catch (RuntimeException exception) {
      outcome = "error";
      throw exception;
    } finally {
      long durationNanos = System.nanoTime() - startedAtNanos;
      Timer.builder("scorm.runtime_state_store.operation")
          .description("RuntimeStateStore operation duration")
          .tag("operation", operation)
          .tag("outcome", outcome)
          .register(meterRegistry)
          .record(durationNanos, TimeUnit.NANOSECONDS);
      LOGGER.debug("runtime_state_store operation={} outcome={} durationNanos={}", operation, outcome, durationNanos);
    }
  }
}
