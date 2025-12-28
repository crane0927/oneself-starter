package com.oneself.redis.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Redis Starter 增强能力配置（连接信息由 Spring 提供）。
 */
@ConfigurationProperties(prefix = "oneself.redis")
public class OneselfRedisProperties {

    /**
     * 是否启用本 Starter 提供的增强能力。
     */
    private boolean enabled = true;

    /**
     * RedisOps 使用的 key 前缀。
     */
    private String keyPrefix = "";

    /**
     * 是否启用 key 规则校验。
     */
    private boolean validateKeyPattern = false;

    /**
     * key 正则规则（包含前缀后的完整 key）。
     */
    private String keyPattern = "^[a-zA-Z0-9:_-]+$";

    /**
     * 是否启用指标采集（需要 Micrometer）。
     */
    private boolean metricsEnabled = false;

    /**
     * 是否启用操作日志。
     */
    private boolean loggingEnabled = false;

    /**
     * 是否启用 Spring Cache 集成。
     */
    private boolean cacheEnabled = false;

    /**
     * 缓存默认 TTL。
     */
    private java.time.Duration cacheTtl = java.time.Duration.ofMinutes(30);

    /**
     * 缓存名称前缀。
     */
    private String cacheKeyPrefix = "";

    /**
     * 是否Enabled。
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * 设置Enabled。
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * 获取KeyPrefix。
     */
    public String getKeyPrefix() {
        return keyPrefix;
    }

    /**
     * 设置KeyPrefix。
     */
    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    /**
     * 是否ValidateKeyPattern。
     */
    public boolean isValidateKeyPattern() {
        return validateKeyPattern;
    }

    /**
     * 设置ValidateKeyPattern。
     */
    public void setValidateKeyPattern(boolean validateKeyPattern) {
        this.validateKeyPattern = validateKeyPattern;
    }

    /**
     * 获取KeyPattern。
     */
    public String getKeyPattern() {
        return keyPattern;
    }

    /**
     * 设置KeyPattern。
     */
    public void setKeyPattern(String keyPattern) {
        this.keyPattern = keyPattern;
    }

    /**
     * 是否MetricsEnabled。
     */
    public boolean isMetricsEnabled() {
        return metricsEnabled;
    }

    /**
     * 设置MetricsEnabled。
     */
    public void setMetricsEnabled(boolean metricsEnabled) {
        this.metricsEnabled = metricsEnabled;
    }

    /**
     * 是否LoggingEnabled。
     */
    public boolean isLoggingEnabled() {
        return loggingEnabled;
    }

    /**
     * 设置LoggingEnabled。
     */
    public void setLoggingEnabled(boolean loggingEnabled) {
        this.loggingEnabled = loggingEnabled;
    }

    /**
     * 是否CacheEnabled。
     */
    public boolean isCacheEnabled() {
        return cacheEnabled;
    }

    /**
     * 设置CacheEnabled。
     */
    public void setCacheEnabled(boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
    }

    /**
     * 获取CacheTtl。
     */
    public java.time.Duration getCacheTtl() {
        return cacheTtl;
    }

    /**
     * 设置CacheTtl。
     */
    public void setCacheTtl(java.time.Duration cacheTtl) {
        this.cacheTtl = cacheTtl;
    }

    /**
     * 获取CacheKeyPrefix。
     */
    public String getCacheKeyPrefix() {
        return cacheKeyPrefix;
    }

    /**
     * 设置CacheKeyPrefix。
     */
    public void setCacheKeyPrefix(String cacheKeyPrefix) {
        this.cacheKeyPrefix = cacheKeyPrefix;
    }
}
