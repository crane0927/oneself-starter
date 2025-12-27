package com.oneself.kafka.core;

/**
 * Outbox 发布接口，由业务侧实现。
 */
public interface KafkaOutboxPublisher {

    /**
     * 发布 Outbox 事件。
     */
    void publish(KafkaOutboxEvent event);
}
