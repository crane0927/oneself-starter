package com.oneself.kafka.core;

import java.nio.charset.StandardCharsets;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;

import com.oneself.kafka.autoconfigure.OneselfKafkaProperties;

/**
 * 重试/死信转发器。
 */
public class KafkaRetryRecoverer implements ConsumerRecordRecoverer {

    private final KafkaTemplate<Object, Object> kafkaTemplate;
    private final OneselfKafkaProperties properties;

    public KafkaRetryRecoverer(KafkaTemplate<Object, Object> kafkaTemplate, OneselfKafkaProperties properties) {
        this.kafkaTemplate = kafkaTemplate;
        this.properties = properties;
    }

    @Override
    public void accept(ConsumerRecord<?, ?> record, Exception exception) {
        String targetTopic = resolveTargetTopic(record);
        ProducerRecord<Object, Object> producerRecord = new ProducerRecord<>(
                targetTopic,
                record.partition(),
                record.key(),
                record.value(),
                record.headers()
        );
        appendFailureHeaders(producerRecord.headers(), record, exception);
        kafkaTemplate.send(producerRecord);
    }

    private String resolveTargetTopic(ConsumerRecord<?, ?> record) {
        if (!properties.isRetryEnabled()) {
            return record.topic() + "." + properties.getDlqSuffix();
        }
        int retryCount = readRetryCount(record.headers());
        String[] delays = properties.getRetryTopicDelays();
        if (delays != null && retryCount < delays.length) {
            return record.topic() + ".retry." + delays[retryCount];
        }
        return record.topic() + "." + properties.getDlqSuffix();
    }

    private int readRetryCount(Headers headers) {
        Header header = headers.lastHeader(KafkaHeaderNames.RETRY_COUNT);
        if (header == null) {
            return 0;
        }
        try {
            return Integer.parseInt(new String(header.value(), StandardCharsets.UTF_8));
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    private void appendFailureHeaders(Headers headers, ConsumerRecord<?, ?> record, Exception exception) {
        int retryCount = readRetryCount(headers);
        headers.remove(KafkaHeaderNames.RETRY_COUNT);
        headers.add(KafkaHeaderNames.RETRY_COUNT, String.valueOf(retryCount + 1).getBytes(StandardCharsets.UTF_8));
        addIfAbsent(headers, KafkaHeaderNames.ORIGINAL_TOPIC, record.topic());
        addIfAbsent(headers, KafkaHeaderNames.ORIGINAL_PARTITION, String.valueOf(record.partition()));
        addIfAbsent(headers, KafkaHeaderNames.ORIGINAL_OFFSET, String.valueOf(record.offset()));
        headers.remove(KafkaHeaderNames.FAILURE_CLASS);
        headers.remove(KafkaHeaderNames.FAILURE_MESSAGE);
        headers.add(KafkaHeaderNames.FAILURE_CLASS, exception.getClass().getName().getBytes(StandardCharsets.UTF_8));
        headers.add(KafkaHeaderNames.FAILURE_MESSAGE, sanitize(exception.getMessage()).getBytes(StandardCharsets.UTF_8));
    }

    private void addIfAbsent(Headers headers, String name, String value) {
        if (headers.lastHeader(name) != null || value == null) {
            return;
        }
        headers.add(name, value.getBytes(StandardCharsets.UTF_8));
    }

    private String sanitize(String message) {
        if (message == null) {
            return "";
        }
        String trimmed = message.replaceAll("[\r\n]+", " ");
        if (trimmed.length() > 256) {
            return trimmed.substring(0, 256);
        }
        return trimmed;
    }
}
