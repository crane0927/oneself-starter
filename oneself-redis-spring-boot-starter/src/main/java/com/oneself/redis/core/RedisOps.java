package com.oneself.redis.core;

import java.time.Duration;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.oneself.redis.autoconfigure.OneselfRedisProperties;

public class RedisOps {

    private static final Logger log = LoggerFactory.getLogger(RedisOps.class);

    private final RedisTemplate<String, Object> redisTemplate;
    private final String keyPrefix;
    private final boolean validateKeyPattern;
    private final Pattern keyPattern;
    private final boolean metricsEnabled;
    private final boolean loggingEnabled;
    private final MeterRegistry meterRegistry;

    public RedisOps(RedisTemplate<String, Object> redisTemplate,
                    OneselfRedisProperties properties,
                    MeterRegistry meterRegistry) {
        this.redisTemplate = redisTemplate;
        this.keyPrefix = properties.getKeyPrefix() == null ? "" : properties.getKeyPrefix();
        this.validateKeyPattern = properties.isValidateKeyPattern();
        this.keyPattern = Pattern.compile(properties.getKeyPattern());
        this.metricsEnabled = properties.isMetricsEnabled();
        this.loggingEnabled = properties.isLoggingEnabled();
        this.meterRegistry = meterRegistry;
    }

    public void set(String key, Object value, Duration ttl) {
        run("set", () -> {
            String realKey = prefix(key);
            if (ttl == null || ttl.isZero() || ttl.isNegative()) {
                redisTemplate.opsForValue().set(realKey, value);
                return;
            }
            redisTemplate.opsForValue().set(realKey, value, ttl);
        });
    }

    public Optional<Object> get(String key) {
        return execute("get", () -> Optional.ofNullable(redisTemplate.opsForValue().get(prefix(key))));
    }

    public Boolean delete(String key) {
        return execute("delete", () -> redisTemplate.delete(prefix(key)));
    }

    private String prefix(String key) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("Redis key must not be blank");
        }
        if (keyPrefix.isEmpty()) {
            return validateKey(key);
        }
        return validateKey(keyPrefix + ":" + key);
    }

    private String validateKey(String key) {
        if (!validateKeyPattern) {
            return key;
        }
        if (!keyPattern.matcher(key).matches()) {
            throw new IllegalArgumentException("Redis key does not match pattern: " + key);
        }
        return key;
    }

    private void run(String operation, Runnable action) {
        execute(operation, () -> {
            action.run();
            return null;
        });
    }

    private <T> T execute(String operation, Supplier<T> action) {
        long startNanos = System.nanoTime();
        try {
            T result = action.get();
            recordMetrics(operation, startNanos, true);
            if (loggingEnabled) {
                log.debug("oneself.redis {} success", operation);
            }
            return result;
        } catch (RuntimeException ex) {
            recordMetrics(operation, startNanos, false);
            if (loggingEnabled) {
                log.warn("oneself.redis {} failed: {}", operation, ex.getMessage());
            }
            throw ex;
        }
    }

    private void recordMetrics(String operation, long startNanos, boolean success) {
        if (!metricsEnabled || meterRegistry == null) {
            return;
        }
        long duration = System.nanoTime() - startNanos;
        Timer.builder("oneself.redis.ops")
                .tag("operation", operation)
                .tag("success", Boolean.toString(success))
                .register(meterRegistry)
                .record(duration, java.util.concurrent.TimeUnit.NANOSECONDS);
        Counter.builder("oneself.redis.ops.count")
                .tag("operation", operation)
                .tag("success", Boolean.toString(success))
                .register(meterRegistry)
                .increment();
    }
}
