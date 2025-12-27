package com.oneself.kafka.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记顺序 key 字段，用于顺序 topic 自动提取 key。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface KafkaOrderKey {
    /**
     * 可选名称，用于标识字段含义。
     */
    String value() default "";
}
