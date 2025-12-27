//package com.oneself.testservice.kafka;
//
//import com.oneself.kafka.core.KafkaConsumerAdapter;
//import com.oneself.kafka.core.KafkaEnvelope;
//import com.oneself.kafka.core.KafkaMessage;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//
//@Component
//@ConditionalOnProperty(prefix = "oneself.kafka", name = "consumer-enabled", havingValue = "true")
//public class TestEventListener {
//
//    private static final Logger log = LoggerFactory.getLogger(TestEventListener.class);
//
//    private final KafkaConsumerAdapter adapter;
//
//    public TestEventListener(KafkaConsumerAdapter adapter) {
//        this.adapter = adapter;
//    }
//
//    @KafkaListener(topics = "oneself.test.events", groupId = "oneself-test-service")
//    public void onMessage(ConsumerRecord<String, KafkaEnvelope<String>> record) {
//        KafkaMessage<String> message = adapter.adapt(record);
//        log.info("received kafka message: {}", message);
//    }
//}
