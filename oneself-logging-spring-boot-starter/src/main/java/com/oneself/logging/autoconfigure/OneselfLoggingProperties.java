package com.oneself.logging.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 日志 Starter 配置。
 */
@ConfigurationProperties(prefix = "oneself.logging")
public class OneselfLoggingProperties {

    /**
     * 是否启用访问日志。
     */
    private boolean accessEnabled = true;

    /**
     * 日志加密默认密钥。
     */
    private String encryptKey;

    /**
     * 自定义脱敏正则规则（逗号分隔或 YAML 列表）。
     */
    private String[] maskPatterns = new String[0];

    /**
     * 固定脱敏字段名（Map/JSON）。
     */
    private String[] maskFields = new String[0];

    /**
     * 固定脱敏字段密钥。
     */
    private String maskFieldsKey;

    /**
     * 慢请求阈值（毫秒），超过则输出 WARN。
     */
    private long accessSlowThresholdMs = 1000;

    /**
     * 访问日志 logger 名称。
     */
    private String accessLoggerName = "ACCESS";

    /**
     * 是否AccessEnabled。
     */
    public boolean isAccessEnabled() {
        return accessEnabled;
    }

    /**
     * 设置AccessEnabled。
     */
    public void setAccessEnabled(boolean accessEnabled) {
        this.accessEnabled = accessEnabled;
    }

    /**
     * 获取EncryptKey。
     */
    public String getEncryptKey() {
        return encryptKey;
    }

    /**
     * 设置EncryptKey。
     */
    public void setEncryptKey(String encryptKey) {
        this.encryptKey = encryptKey;
    }

    /**
     * 获取MaskPatterns。
     */
    public String[] getMaskPatterns() {
        return maskPatterns;
    }

    /**
     * 设置MaskPatterns。
     */
    public void setMaskPatterns(String[] maskPatterns) {
        this.maskPatterns = maskPatterns;
    }

    /**
     * 获取MaskFields。
     */
    public String[] getMaskFields() {
        return maskFields;
    }

    /**
     * 设置MaskFields。
     */
    public void setMaskFields(String[] maskFields) {
        this.maskFields = maskFields;
    }

    /**
     * 获取MaskFieldsKey。
     */
    public String getMaskFieldsKey() {
        return maskFieldsKey;
    }

    /**
     * 设置MaskFieldsKey。
     */
    public void setMaskFieldsKey(String maskFieldsKey) {
        this.maskFieldsKey = maskFieldsKey;
    }

    /**
     * 获取AccessSlowThresholdMs。
     */
    public long getAccessSlowThresholdMs() {
        return accessSlowThresholdMs;
    }

    /**
     * 设置AccessSlowThresholdMs。
     */
    public void setAccessSlowThresholdMs(long accessSlowThresholdMs) {
        this.accessSlowThresholdMs = accessSlowThresholdMs;
    }

    /**
     * 获取AccessLoggerName。
     */
    public String getAccessLoggerName() {
        return accessLoggerName;
    }

    /**
     * 设置AccessLoggerName。
     */
    public void setAccessLoggerName(String accessLoggerName) {
        this.accessLoggerName = accessLoggerName;
    }
}
