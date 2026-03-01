package com.ihu.scorm.engine.runtime.state;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ihu.scorm.engine.common.constant.SystemMessages;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component("redisRuntimeStateStore")
public class RedisRuntimeStateStore implements RuntimeStateStore {

  private final StringRedisTemplate redisTemplate;
  private final ObjectMapper objectMapper;

  public RedisRuntimeStateStore(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
    this.redisTemplate = redisTemplate;
    this.objectMapper = objectMapper;
  }

  @Override
  public LaunchRuntimeState get(UUID launchId) {
    String payload = redisTemplate.opsForValue().get(key(launchId));
    if (payload == null) {
      return null;
    }
    try {
      return objectMapper.readValue(payload, LaunchRuntimeState.class);
    } catch (Exception exception) {
      throw new IllegalStateException(SystemMessages.FAILED_TO_DESERIALIZE_RUNTIME_STATE, exception);
    }
  }

  @Override
  public void save(LaunchRuntimeState state, int ttlSeconds) {
    try {
      String payload = objectMapper.writeValueAsString(state);
      redisTemplate.opsForValue().set(key(state.getLaunchId()), payload, ttlSeconds, TimeUnit.SECONDS);
    } catch (Exception exception) {
      throw new IllegalStateException(SystemMessages.FAILED_TO_SERIALIZE_RUNTIME_STATE, exception);
    }
  }

  @Override
  public void delete(UUID launchId) {
    redisTemplate.delete(key(launchId));
  }

  @Override
  public List<LaunchRuntimeState> findDirtyBefore(Instant lastSeenBefore) {
    Set<String> keys = redisTemplate.keys(RuntimeStateKeys.LAUNCH_KEY_WILDCARD);
    if (keys == null || keys.isEmpty()) {
      return List.of();
    }

    List<LaunchRuntimeState> result = new ArrayList<>();
    for (String key : keys) {
      String payload = redisTemplate.opsForValue().get(key);
      if (payload == null) {
        continue;
      }
      try {
        LaunchRuntimeState state = objectMapper.readValue(payload, LaunchRuntimeState.class);
        if (state.isDirty() && state.getLastSeenAt() != null && state.getLastSeenAt().isBefore(lastSeenBefore)) {
          result.add(state);
        }
      } catch (Exception ignored) {
      }
    }
    return result;
  }

  private String key(UUID launchId) {
    return RuntimeStateKeys.LAUNCH_KEY_PREFIX + launchId;
  }
}
