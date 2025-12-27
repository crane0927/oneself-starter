//package com.oneself.testservice.kafka;
//
//import com.oneself.kafka.core.KafkaOrderKey;
//
//public class TestEvent {
//
//    @KafkaOrderKey("orderId")
//    private final String orderId;
//    private final String message;
//
//    public TestEvent(String orderId, String message) {
//        this.orderId = orderId;
//        this.message = message;
//    }
//
//    public String getOrderId() {
//        return orderId;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//}
