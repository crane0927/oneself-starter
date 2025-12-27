package com.oneself.kafka.core;

import java.time.Instant;
import java.util.Map;

/**
 * 统一事件封装（JSON）。
 */
public class KafkaEnvelope<T> {

    private final String id;
    private final String type;
    private final String traceId;
    private final String schemaVersion;
    private final T payload;
    private final Map<String, String> headers;
    private final Instant timestamp;

    public KafkaEnvelope(String id, String type, String traceId, String schemaVersion, T payload,
                         Map<String, String> headers, Instant timestamp) {
        this.id = id;
        this.type = type;
        this.traceId = traceId;
        this.schemaVersion = schemaVersion;
        this.payload = payload;
        this.headers = headers;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getTraceId() {
        return traceId;
    }

    public String getSchemaVersion() {
        return schemaVersion;
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
