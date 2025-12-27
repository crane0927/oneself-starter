package com.oneself.kafka.core;

/**
 * 幂等记录仓储接口。
 */
public interface KafkaIdempotentRepository {

    /**
     * 尝试插入幂等记录。
     */
    boolean tryInsert(String eventId, String groupId, String topic, int partition, long offset);

    /**
     * 标记完成。
     */
    void markDone(String eventId);

    /**
     * 标记失败。
     */
    void markFailed(String eventId, String errorMessage);
}
