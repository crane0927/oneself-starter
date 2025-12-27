package com.oneself.kafka.core;

import java.time.Duration;
import java.util.function.Supplier;

import org.springframework.data.redis.core.StringRedisTemplate;

import com.oneself.kafka.autoconfigure.OneselfKafkaProperties;

/**
 * Redis 幂等执行器。
 */
public class RedisKafkaIdempotentExecutor implements KafkaIdempotentExecutor {

    private static final String PROCESSING = "PROCESSING";
    private static final String DONE = "DONE";

    private final StringRedisTemplate redisTemplate;
    private final OneselfKafkaProperties properties;

    public RedisKafkaIdempotentExecutor(StringRedisTemplate redisTemplate, OneselfKafkaProperties properties) {
        this.redisTemplate = redisTemplate;
        this.properties = properties;
    }

    @Override
    public <T> T execute(String eventId, Supplier<T> action, Supplier<T> onDuplicate) {
        String key = properties.getIdempotentKeyPrefix() + eventId;
        Duration processingTtl = properties.getIdempotentProcessingTtl();
        Boolean acquired = redisTemplate.opsForValue().setIfAbsent(key, PROCESSING, processingTtl);
        if (Boolean.FALSE.equals(acquired)) {
            return onDuplicate.get();
        }
        try {
            T result = action.get();
            redisTemplate.opsForValue().set(key, DONE, properties.getIdempotentDoneTtl());
            return result;
        } catch (RuntimeException ex) {
            redisTemplate.delete(key);
            throw ex;
        }
    }
}
