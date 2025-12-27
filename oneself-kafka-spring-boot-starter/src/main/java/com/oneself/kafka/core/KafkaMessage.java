package com.oneself.kafka.core;

import java.time.Instant;
import java.util.Map;

/**
 * Kafka 统一消息封装。
 */
public class KafkaMessage<T> {

    private final String topic;
    private final String key;
    private final T payload;
    private final Map<String, String> headers;
    private final Instant timestamp;

    public KafkaMessage(String topic, String key, T payload, Map<String, String> headers, Instant timestamp) {
        this.topic = topic;
        this.key = key;
        this.payload = payload;
        this.headers = headers;
        this.timestamp = timestamp;
    }

    public String getTopic() {
        return topic;
    }

    public String getKey() {
        return key;
    }

    public T getPayload() {
        return payload;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
