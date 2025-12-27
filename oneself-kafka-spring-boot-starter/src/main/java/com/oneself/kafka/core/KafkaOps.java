package com.oneself.kafka.core;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.slf4j.MDC;

import com.oneself.kafka.autoconfigure.OneselfKafkaProperties;

/**
 * Kafka 统一生产者封装。
 */
public class KafkaOps {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final OneselfKafkaProperties properties;
    private final KafkaKeyResolver keyResolver;

    /**
     * 构造 KafkaOps。
     */
    public KafkaOps(KafkaTemplate<String, Object> kafkaTemplate, OneselfKafkaProperties properties) {
        this.kafkaTemplate = kafkaTemplate;
        this.properties = properties;
        this.keyResolver = new KafkaKeyResolver();
    }

    /**
     * 发送到指定主题。
     */
    public void send(String topic, Object payload) {
        send(topic, null, payload, null);
    }

    /**
     * 发送到指定主题并指定 key。
     */
    public void send(String topic, String key, Object payload) {
        send(topic, key, payload, null);
    }

    /**
     * 发送到默认主题。
     */
    public void sendDefault(Object payload) {
        String topic = properties.getDefaultTopic();
        if (topic == null || topic.isBlank()) {
            throw new IllegalStateException("Default topic is not configured");
        }
        send(topic, null, payload, null);
    }

    /**
     * 发送自定义 header 的消息。
     */
    public void send(String topic, String key, Object payload, Map<String, String> headers) {
        String resolvedKey = resolveKey(topic, key, payload);
        Map<String, String> mergedHeaders = new HashMap<>();
        if (headers != null) {
            mergedHeaders.putAll(headers);
        }
        String eventId = ensureHeader(mergedHeaders, KafkaHeaderNames.EVENT_ID, UUID.randomUUID().toString());
        String traceId = ensureHeader(mergedHeaders, KafkaHeaderNames.TRACE_ID, mdcTraceId());
        KafkaEnvelope<Object> envelope = new KafkaEnvelope<>(
                eventId,
                payload == null ? "unknown" : payload.getClass().getName(),
                traceId,
                properties.getSchemaVersion(),
                payload,
                mergedHeaders,
                Instant.now()
        );
        ProducerRecord<String, Object> record = new ProducerRecord<>(topic, resolvedKey, envelope);
        mergedHeaders.forEach((headerKey, headerValue) -> {
            if (headerKey != null && headerValue != null) {
                record.headers().add(new RecordHeader(headerKey, headerValue.getBytes(StandardCharsets.UTF_8)));
            }
        });
        kafkaTemplate.send(record);
    }

    /**
     * 解析消息 key，顺序 topic 必须保证有 key。
     */
    private String resolveKey(String topic, String key, Object payload) {
        boolean ordered = isOrderedTopic(topic);
        if (ordered) {
            if (key != null && !key.isBlank()) {
                return key;
            }
            String resolved = keyResolver.resolveFromPayload(payload);
            if (resolved == null || resolved.isBlank()) {
                throw new IllegalStateException("Ordered topic requires message key");
            }
            return resolved;
        }
        if (key != null && !key.isBlank()) {
            return key;
        }
        if (properties.getUnorderedKeyStrategy() == OneselfKafkaProperties.UnorderedKeyStrategy.RANDOM) {
            return UUID.randomUUID().toString();
        }
        return null;
    }

    /**
     * 判断是否为顺序 topic。
     */
    private boolean isOrderedTopic(String topic) {
        if (topic == null || topic.isBlank()) {
            return false;
        }
        String[] orderedTopics = properties.getOrderedTopics();
        if (orderedTopics == null || orderedTopics.length == 0) {
            return false;
        }
        for (String ordered : orderedTopics) {
            if (topic.equals(ordered)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 若未指定 header，则填充默认值。
     */
    private String ensureHeader(Map<String, String> headers, String name, String value) {
        if (headers.containsKey(name)) {
            return headers.get(name);
        }
        if (value == null || value.isBlank()) {
            return null;
        }
        headers.put(name, value);
        return value;
    }

    private String mdcTraceId() {
        String traceId = MDC.get(KafkaHeaderNames.TRACE_ID);
        if (traceId == null || traceId.isBlank()) {
            traceId = MDC.get("traceId");
        }
        return traceId;
    }
}
