package com.oneself.kafka.core;

/**
 * Kafka 统一 header 名称。
 */
public final class KafkaHeaderNames {

    public static final String EVENT_ID = "x-event-id";
    public static final String TRACE_ID = "x-trace-id";
    public static final String RETRY_COUNT = "x-retry-count";
    public static final String ORIGINAL_TOPIC = "x-original-topic";
    public static final String ORIGINAL_PARTITION = "x-original-partition";
    public static final String ORIGINAL_OFFSET = "x-original-offset";
    public static final String FAILURE_CLASS = "x-failure-class";
    public static final String FAILURE_MESSAGE = "x-failure-message";

    private KafkaHeaderNames() {
    }
}
