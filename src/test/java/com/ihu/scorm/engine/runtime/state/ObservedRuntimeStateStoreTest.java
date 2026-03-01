package com.ihu.scorm.engine.runtime.state;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ObservedRuntimeStateStoreTest {

  @Test
  void getRecordsSuccessTimer() {
    RedisRuntimeStateStore delegate = mock(RedisRuntimeStateStore.class);
    LaunchRuntimeState state = new LaunchRuntimeState();
    UUID launchId = UUID.randomUUID();
    when(delegate.get(launchId)).thenReturn(state);

    SimpleMeterRegistry meterRegistry = new SimpleMeterRegistry();
    ObservedRuntimeStateStore store = new ObservedRuntimeStateStore(delegate, meterRegistry);

    LaunchRuntimeState result = store.get(launchId);

    assertThat(result).isSameAs(state);
    Timer timer = meterRegistry.find("scorm.runtime_state_store.operation")
        .tag("operation", "get")
        .tag("outcome", "success")
        .timer();
    assertThat(timer).isNotNull();
    assertThat(timer.count()).isEqualTo(1);
  }

  @Test
  void getRecordsErrorTimer() {
    RedisRuntimeStateStore delegate = mock(RedisRuntimeStateStore.class);
    UUID launchId = UUID.randomUUID();
    when(delegate.get(launchId)).thenThrow(new IllegalStateException("boom"));

    SimpleMeterRegistry meterRegistry = new SimpleMeterRegistry();
    ObservedRuntimeStateStore store = new ObservedRuntimeStateStore(delegate, meterRegistry);

    assertThatThrownBy(() -> store.get(launchId)).isInstanceOf(IllegalStateException.class);

    Timer timer = meterRegistry.find("scorm.runtime_state_store.operation")
        .tag("operation", "get")
        .tag("outcome", "error")
        .timer();
    assertThat(timer).isNotNull();
    assertThat(timer.count()).isEqualTo(1);
  }

  @Test
  void findDirtyBeforeRecordsResultSize() {
    RedisRuntimeStateStore delegate = mock(RedisRuntimeStateStore.class);
    when(delegate.findDirtyBefore(Instant.parse("2026-03-01T10:00:00Z")))
        .thenReturn(List.of(new LaunchRuntimeState(), new LaunchRuntimeState()));

    SimpleMeterRegistry meterRegistry = new SimpleMeterRegistry();
    ObservedRuntimeStateStore store = new ObservedRuntimeStateStore(delegate, meterRegistry);

    List<LaunchRuntimeState> result = store.findDirtyBefore(Instant.parse("2026-03-01T10:00:00Z"));

    assertThat(result).hasSize(2);
    DistributionSummary summary = meterRegistry.find("scorm.runtime_state_store.find_dirty.result_size").summary();
    assertThat(summary).isNotNull();
    double total = summary.totalAmount();
    assertThat(total).isEqualTo(2.0d);
  }
}
