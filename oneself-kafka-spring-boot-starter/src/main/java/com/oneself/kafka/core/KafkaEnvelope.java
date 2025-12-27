package com.oneself.kafka.core;

import java.time.Instant;
import java.util.Map;

/**
 * 统一事件封装（JSON）。
 */
public record KafkaEnvelope<T>(String id, String type, String traceId, String schemaVersion, T payload,
                               Map<String, String> headers, Instant timestamp) {

}
