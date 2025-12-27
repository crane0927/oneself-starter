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
     * 节点地址列表。
     */
    private String[] uris = new String[] {"http://localhost:9200"};

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
    public String[] getUris() {
        return uris;
    }

    /**
     * 设置Uris。
     */
    public void setUris(String[] uris) {
        this.uris = uris;
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
}
