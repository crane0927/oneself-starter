package com.oneself.kafka.core;

import java.util.function.Supplier;

/**
 * 数据库幂等执行器。
 */
public class DbKafkaIdempotentExecutor implements KafkaIdempotentExecutor {

    private final KafkaIdempotentRepository repository;
    private final String consumerGroup;

    public DbKafkaIdempotentExecutor(KafkaIdempotentRepository repository, String consumerGroup) {
        this.repository = repository;
        this.consumerGroup = consumerGroup;
    }

    @Override
    public <T> T execute(String eventId, Supplier<T> action, Supplier<T> onDuplicate) {
        if (eventId == null || eventId.isBlank()) {
            return action.get();
        }
        boolean inserted = repository.tryInsert(eventId, consumerGroup, "unknown", -1, -1L);
        if (!inserted) {
            return onDuplicate.get();
        }
        try {
            T result = action.get();
            repository.markDone(eventId);
            return result;
        } catch (RuntimeException ex) {
            repository.markFailed(eventId, ex.getMessage());
            throw ex;
        }
    }
}
