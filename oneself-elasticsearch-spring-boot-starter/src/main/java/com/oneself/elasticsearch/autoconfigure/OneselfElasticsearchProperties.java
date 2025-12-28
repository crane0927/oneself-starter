package com.oneself.elasticsearch.autoconfigure;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Elasticsearch Starter 统一配置。
 */
@ConfigurationProperties(prefix = "oneself.elasticsearch")
public class OneselfElasticsearchProperties {

    /**
     * 是否启用本 Starter。
     */
    private boolean enabled = true;

    /**
     * 节点地址列表（ip:port 逗号分隔）。
     */
    private String uris = "127.0.0.1:9200";

    /**
     * 协议（http/https）。
     */
    private String scheme = "http";

    /**
     * 用户名。
     */
    private String username;

    /**
     * 密码。
     */
    private String password;

    /**
     * 连接超时。
     */
    private Duration connectTimeout = Duration.ofSeconds(2);

    /**
     * 读超时。
     */
    private Duration socketTimeout = Duration.ofSeconds(30);

    /**
     * 索引前缀。
     */
    private String indexPrefix = "";

    /**
     * 是否启用 Bulk。
     */
    private boolean bulkEnabled = true;

    /**
     * Bulk 单批最大操作数。
     */
    private int bulkMaxOperations = 1000;

    /**
     * Bulk 单批最大字节数。
     */
    private long bulkMaxBytes = 5 * 1024 * 1024;

    /**
     * Bulk 刷新间隔。
     */
    private Duration bulkFlushInterval = Duration.ofSeconds(1);

    /**
     * Bulk 并发请求数。
     */
    private int bulkConcurrentRequests = 1;


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
     * 获取Uris。
     */
    public String getUris() {
        return uris;
    }

    /**
     * 设置Uris。
     */
    public void setUris(String uris) {
        this.uris = uris;
    }

    /**
     * 获取Scheme。
     */
    public String getScheme() {
        return scheme;
    }

    /**
     * 设置Scheme。
     */
    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    /**
     * 获取Username。
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置Username。
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取Password。
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置Password。
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取ConnectTimeout。
     */
    public Duration getConnectTimeout() {
        return connectTimeout;
    }

    /**
     * 设置ConnectTimeout。
     */
    public void setConnectTimeout(Duration connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    /**
     * 获取SocketTimeout。
     */
    public Duration getSocketTimeout() {
        return socketTimeout;
    }

    /**
     * 设置SocketTimeout。
     */
    public void setSocketTimeout(Duration socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    /**
     * 获取IndexPrefix。
     */
    public String getIndexPrefix() {
        return indexPrefix;
    }

    /**
     * 设置IndexPrefix。
     */
    public void setIndexPrefix(String indexPrefix) {
        this.indexPrefix = indexPrefix;
    }

    /**
     * 是否BulkEnabled。
     */
    public boolean isBulkEnabled() {
        return bulkEnabled;
    }

    /**
     * 设置BulkEnabled。
     */
    public void setBulkEnabled(boolean bulkEnabled) {
        this.bulkEnabled = bulkEnabled;
    }

    /**
     * 获取BulkMaxOperations。
     */
    public int getBulkMaxOperations() {
        return bulkMaxOperations;
    }

    /**
     * 设置BulkMaxOperations。
     */
    public void setBulkMaxOperations(int bulkMaxOperations) {
        this.bulkMaxOperations = bulkMaxOperations;
    }

    /**
     * 获取BulkMaxBytes。
     */
    public long getBulkMaxBytes() {
        return bulkMaxBytes;
    }

    /**
     * 设置BulkMaxBytes。
     */
    public void setBulkMaxBytes(long bulkMaxBytes) {
        this.bulkMaxBytes = bulkMaxBytes;
    }

    /**
     * 获取BulkFlushInterval。
     */
    public Duration getBulkFlushInterval() {
        return bulkFlushInterval;
    }

    /**
     * 设置BulkFlushInterval。
     */
    public void setBulkFlushInterval(Duration bulkFlushInterval) {
        this.bulkFlushInterval = bulkFlushInterval;
    }

    /**
     * 获取BulkConcurrentRequests。
     */
    public int getBulkConcurrentRequests() {
        return bulkConcurrentRequests;
    }

    /**
     * 设置BulkConcurrentRequests。
     */
    public void setBulkConcurrentRequests(int bulkConcurrentRequests) {
        this.bulkConcurrentRequests = bulkConcurrentRequests;
    }

}
