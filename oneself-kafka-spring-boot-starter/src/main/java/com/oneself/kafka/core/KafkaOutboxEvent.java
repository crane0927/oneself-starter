package com.oneself.kafka.core;

import java.time.Instant;

/**
 * Outbox 事件模型（用于业务侧持久化）。
 */
public class KafkaOutboxEvent {

    private final String id;
    private final String topic;
    private final String key;
    private final String payload;
    private final Instant createdAt;

    public KafkaOutboxEvent(String id, String topic, String key, String payload, Instant createdAt) {
        this.id = id;
        this.topic = topic;
        this.key = key;
        this.payload = payload;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getTopic() {
        return topic;
    }

    public String getKey() {
        return key;
    }

    public String getPayload() {
        return payload;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
