package com.ihu.scorm.engine.runtime.orchestrator;

import com.ihu.scorm.engine.common.constant.DetailKey;
import com.ihu.scorm.engine.common.error.ErrorCode;
import com.ihu.scorm.engine.common.error.ErrorMessages;
import com.ihu.scorm.engine.common.error.RateLimitException;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Component;

@Component
public class RuntimeRateLimiter {

  private static final int MAX_REQUESTS_PER_SECOND = 20;

  private final ConcurrentHashMap<UUID, WindowCounter> counters = new ConcurrentHashMap<>();

  public void checkOrThrow(UUID launchId, Instant now) {
    WindowCounter counter = counters.computeIfAbsent(launchId, key -> new WindowCounter(now.getEpochSecond()));

    synchronized (counter) {
      long currentSecond = now.getEpochSecond();
      if (counter.second != currentSecond) {
        counter.second = currentSecond;
        counter.count.set(0);
      }
      int next = counter.count.incrementAndGet();
      if (next > MAX_REQUESTS_PER_SECOND) {
        throw new RateLimitException(
            ErrorCode.RUNTIME_RATE_LIMIT,
            ErrorMessages.RUNTIME_COMMITS_RATE_LIMITED,
            Map.of(DetailKey.LAUNCH_ID, launchId, DetailKey.MAX_PER_SECOND, MAX_REQUESTS_PER_SECOND));
      }
    }
  }

  private static final class WindowCounter {
    private long second;
    private final AtomicInteger count;

    private WindowCounter(long second) {
      this.second = second;
      this.count = new AtomicInteger(0);
    }
  }
}
