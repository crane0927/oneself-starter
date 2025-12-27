package com.oneself.kafka.core;

import java.lang.reflect.Field;

/**
 * 顺序 key 解析器。
 */
public class KafkaKeyResolver {

    /**
     * 从 payload 中解析顺序 key。
     */
    public String resolveFromPayload(Object payload) {
        if (payload == null) {
            return null;
        }
        Class<?> targetClass = payload.getClass();
        for (Field field : targetClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(KafkaOrderKey.class)) {
                field.setAccessible(true);
                try {
                    Object value = field.get(payload);
                    return value == null ? null : String.valueOf(value);
                } catch (IllegalAccessException ex) {
                    throw new IllegalStateException("Failed to access @KafkaOrderKey field", ex);
                }
            }
        }
        return null;
    }
}
