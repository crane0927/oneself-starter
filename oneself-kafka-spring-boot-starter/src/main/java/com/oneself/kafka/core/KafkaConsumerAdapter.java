package com.oneself.kafka.core;

import java.time.Instant;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerRecord;


/**
 * 消费者通用适配器，将原始 ConsumerRecord 统一封装。
 */
public class KafkaConsumerAdapter {

    /**
     * 将 ConsumerRecord 转为统一消息模型。
     */
    public <T> KafkaMessage<T> adapt(ConsumerRecord<String, KafkaEnvelope<T>> record) {
        Map<String, String> headers = new HashMap<>();
        record.headers().forEach(header -> headers.put(header.key(), new String(header.value(), StandardCharsets.UTF_8)));
        Instant timestamp = Instant.ofEpochMilli(record.timestamp());
        KafkaEnvelope<T> envelope = record.value();
        if (envelope == null) {
            return new KafkaMessage<>(record.topic(), record.key(), null, headers, timestamp);
        }
        Map<String, String> mergedHeaders = mergeHeaders(headers, envelope.headers());
        if (!mergedHeaders.containsKey(KafkaHeaderNames.EVENT_ID) && envelope.id() != null) {
            mergedHeaders.put(KafkaHeaderNames.EVENT_ID, envelope.id());
        }
        if (!mergedHeaders.containsKey(KafkaHeaderNames.TRACE_ID) && envelope.traceId() != null) {
            mergedHeaders.put(KafkaHeaderNames.TRACE_ID, envelope.traceId());
        }
        return new KafkaMessage<>(record.topic(),
                Optional.ofNullable(record.key()).orElse(envelope.id()),
                envelope.payload(),
                mergedHeaders,
                envelope.timestamp() == null ? timestamp : envelope.timestamp());
    }

    /**
     * 合并 Record 与 Envelope 的 headers。
     */
    private Map<String, String> mergeHeaders(Map<String, String> recordHeaders, Map<String, String> envelopeHeaders) {
        Map<String, String> merged = new HashMap<>(recordHeaders);
        if (envelopeHeaders != null) {
            merged.putAll(envelopeHeaders);
        }
        return merged;
    }
}
