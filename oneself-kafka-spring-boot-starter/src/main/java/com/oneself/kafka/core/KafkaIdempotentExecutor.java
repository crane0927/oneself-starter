package com.oneself.kafka.core;

import java.util.function.Supplier;

/**
 * Kafka 幂等执行器。
 */
public interface KafkaIdempotentExecutor {

    /**
     * 执行幂等逻辑。
     */
    <T> T execute(String eventId, Supplier<T> action, Supplier<T> onDuplicate);
}
