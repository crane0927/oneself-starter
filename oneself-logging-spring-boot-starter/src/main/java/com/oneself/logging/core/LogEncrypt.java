package com.oneself.logging.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日志字段加密注解。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface LogEncrypt {

    /**
     * 加密模式。
     */
    Mode mode() default Mode.MASK;

    /**
     * 密钥（可选，未设置则使用全局密钥）。
     */
    String key() default "";

    /**
     * 字段名称（用于 Map/JSON 中的字段匹配）。
     */
    String name() default "";

    enum Mode {
        /**
         * 直接替换为默认掩码。
         */
        MASK,
        /**
         * 使用密钥加密。
         */
        ENCRYPT
    }
}
