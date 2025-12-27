package com.oneself.redis.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Redis Starter 统一配置。
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
     * Redis 部署模式。
     */
    private Mode mode = Mode.SINGLE;

    /**
     * Redis 密码（单机或集群）。
     */
    private String password;

    /**
     * Sentinel 密码（可选，与 Redis 密码区分）。
     */
    private String sentinelPassword;

    /**
     * Redis 数据库索引（单机/哨兵模式）。
     */
    private Integer database = 0;

    /**
     * 单机地址。
     */
    private String host = "localhost";

    /**
     * 单机端口。
     */
    private Integer port = 6379;

    /**
     * 集群节点列表（host:port）。
     */
    private String[] nodes = new String[0];

    /**
     * Sentinel 主节点名称。
     */
    private String sentinelMaster;

    /**
     * Sentinel 节点列表（host:port）。
     */
    private String[] sentinelNodes = new String[0];

    /**
     * 是否启用 SSL/TLS。
     */
    private boolean sslEnabled = false;

    /**
     * 是否校验对端证书。
     */
    private boolean sslVerifyPeer = true;

    /**
     * SSL truststore 路径。
     */
    private String sslTruststore;

    /**
     * SSL truststore 密码。
     */
    private String sslTruststorePassword;

    /**
     * SSL keystore 路径。
     */
    private String sslKeystore;

    /**
     * SSL keystore 密码。
     */
    private String sslKeystorePassword;

    /**
     * 命令超时。
     */
    private java.time.Duration timeout = java.time.Duration.ofSeconds(2);

    /**
     * 连接超时。
     */
    private java.time.Duration connectTimeout = java.time.Duration.ofSeconds(2);

    /**
     * 是否启用 Lettuce 连接池。
     */
    private boolean poolEnabled = false;

    /**
     * 连接池最大活跃连接数。
     */
    private int poolMaxActive = 8;

    /**
     * 连接池最大空闲连接数。
     */
    private int poolMaxIdle = 8;

    /**
     * 连接池最小空闲连接数。
     */
    private int poolMinIdle = 0;

    /**
     * 连接池最大等待时间。
     */
    private java.time.Duration poolMaxWait = java.time.Duration.ofSeconds(1);

    /**
     * Lettuce client name。
     */
    private String clientName;

    /**
     * 读策略：MASTER, MASTER_PREFERRED, REPLICA, REPLICA_PREFERRED, ANY。
     */
    private String readFrom;

    /**
     * 是否自动重连。
     */
    private boolean autoReconnect = true;

    /**
     * 断连时行为：DEFAULT, REJECT_COMMANDS, ACCEPT_COMMANDS。
     */
    private String disconnectedBehavior = "DEFAULT";

    /**
     * 激活连接前是否发送 PING。
     */
    private boolean pingBeforeActivateConnection = false;

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

    /**
     * 获取Mode。
     */
    public Mode getMode() {
        return mode;
    }

    /**
     * 设置Mode。
     */
    public void setMode(Mode mode) {
        this.mode = mode;
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
     * 获取SentinelPassword。
     */
    public String getSentinelPassword() {
        return sentinelPassword;
    }

    /**
     * 设置SentinelPassword。
     */
    public void setSentinelPassword(String sentinelPassword) {
        this.sentinelPassword = sentinelPassword;
    }

    /**
     * 获取Database。
     */
    public Integer getDatabase() {
        return database;
    }

    /**
     * 设置Database。
     */
    public void setDatabase(Integer database) {
        this.database = database;
    }

    /**
     * 获取Host。
     */
    public String getHost() {
        return host;
    }

    /**
     * 设置Host。
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * 获取Port。
     */
    public Integer getPort() {
        return port;
    }

    /**
     * 设置Port。
     */
    public void setPort(Integer port) {
        this.port = port;
    }

    /**
     * 获取Nodes。
     */
    public String[] getNodes() {
        return nodes;
    }

    /**
     * 设置Nodes。
     */
    public void setNodes(String[] nodes) {
        this.nodes = nodes;
    }

    /**
     * 获取SentinelMaster。
     */
    public String getSentinelMaster() {
        return sentinelMaster;
    }

    /**
     * 设置SentinelMaster。
     */
    public void setSentinelMaster(String sentinelMaster) {
        this.sentinelMaster = sentinelMaster;
    }

    /**
     * 获取SentinelNodes。
     */
    public String[] getSentinelNodes() {
        return sentinelNodes;
    }

    /**
     * 设置SentinelNodes。
     */
    public void setSentinelNodes(String[] sentinelNodes) {
        this.sentinelNodes = sentinelNodes;
    }

    /**
     * 是否SslEnabled。
     */
    public boolean isSslEnabled() {
        return sslEnabled;
    }

    /**
     * 设置SslEnabled。
     */
    public void setSslEnabled(boolean sslEnabled) {
        this.sslEnabled = sslEnabled;
    }

    /**
     * 是否SslVerifyPeer。
     */
    public boolean isSslVerifyPeer() {
        return sslVerifyPeer;
    }

    /**
     * 设置SslVerifyPeer。
     */
    public void setSslVerifyPeer(boolean sslVerifyPeer) {
        this.sslVerifyPeer = sslVerifyPeer;
    }

    /**
     * 获取SslTruststore。
     */
    public String getSslTruststore() {
        return sslTruststore;
    }

    /**
     * 设置SslTruststore。
     */
    public void setSslTruststore(String sslTruststore) {
        this.sslTruststore = sslTruststore;
    }

    /**
     * 获取SslTruststorePassword。
     */
    public String getSslTruststorePassword() {
        return sslTruststorePassword;
    }

    /**
     * 设置SslTruststorePassword。
     */
    public void setSslTruststorePassword(String sslTruststorePassword) {
        this.sslTruststorePassword = sslTruststorePassword;
    }

    /**
     * 获取SslKeystore。
     */
    public String getSslKeystore() {
        return sslKeystore;
    }

    /**
     * 设置SslKeystore。
     */
    public void setSslKeystore(String sslKeystore) {
        this.sslKeystore = sslKeystore;
    }

    /**
     * 获取SslKeystorePassword。
     */
    public String getSslKeystorePassword() {
        return sslKeystorePassword;
    }

    /**
     * 设置SslKeystorePassword。
     */
    public void setSslKeystorePassword(String sslKeystorePassword) {
        this.sslKeystorePassword = sslKeystorePassword;
    }

    /**
     * 获取Timeout。
     */
    public java.time.Duration getTimeout() {
        return timeout;
    }

    /**
     * 设置Timeout。
     */
    public void setTimeout(java.time.Duration timeout) {
        this.timeout = timeout;
    }

    /**
     * 获取ConnectTimeout。
     */
    public java.time.Duration getConnectTimeout() {
        return connectTimeout;
    }

    /**
     * 设置ConnectTimeout。
     */
    public void setConnectTimeout(java.time.Duration connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    /**
     * 是否PoolEnabled。
     */
    public boolean isPoolEnabled() {
        return poolEnabled;
    }

    /**
     * 设置PoolEnabled。
     */
    public void setPoolEnabled(boolean poolEnabled) {
        this.poolEnabled = poolEnabled;
    }

    /**
     * 获取PoolMaxActive。
     */
    public int getPoolMaxActive() {
        return poolMaxActive;
    }

    /**
     * 设置PoolMaxActive。
     */
    public void setPoolMaxActive(int poolMaxActive) {
        this.poolMaxActive = poolMaxActive;
    }

    /**
     * 获取PoolMaxIdle。
     */
    public int getPoolMaxIdle() {
        return poolMaxIdle;
    }

    /**
     * 设置PoolMaxIdle。
     */
    public void setPoolMaxIdle(int poolMaxIdle) {
        this.poolMaxIdle = poolMaxIdle;
    }

    /**
     * 获取PoolMinIdle。
     */
    public int getPoolMinIdle() {
        return poolMinIdle;
    }

    /**
     * 设置PoolMinIdle。
     */
    public void setPoolMinIdle(int poolMinIdle) {
        this.poolMinIdle = poolMinIdle;
    }

    /**
     * 获取PoolMaxWait。
     */
    public java.time.Duration getPoolMaxWait() {
        return poolMaxWait;
    }

    /**
     * 设置PoolMaxWait。
     */
    public void setPoolMaxWait(java.time.Duration poolMaxWait) {
        this.poolMaxWait = poolMaxWait;
    }

    /**
     * 获取ClientName。
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * 设置ClientName。
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * 获取ReadFrom。
     */
    public String getReadFrom() {
        return readFrom;
    }

    /**
     * 设置ReadFrom。
     */
    public void setReadFrom(String readFrom) {
        this.readFrom = readFrom;
    }

    /**
     * 是否AutoReconnect。
     */
    public boolean isAutoReconnect() {
        return autoReconnect;
    }

    /**
     * 设置AutoReconnect。
     */
    public void setAutoReconnect(boolean autoReconnect) {
        this.autoReconnect = autoReconnect;
    }

    /**
     * 获取DisconnectedBehavior。
     */
    public String getDisconnectedBehavior() {
        return disconnectedBehavior;
    }

    /**
     * 设置DisconnectedBehavior。
     */
    public void setDisconnectedBehavior(String disconnectedBehavior) {
        this.disconnectedBehavior = disconnectedBehavior;
    }

    /**
     * 是否PingBeforeActivateConnection。
     */
    public boolean isPingBeforeActivateConnection() {
        return pingBeforeActivateConnection;
    }

    /**
     * 设置PingBeforeActivateConnection。
     */
    public void setPingBeforeActivateConnection(boolean pingBeforeActivateConnection) {
        this.pingBeforeActivateConnection = pingBeforeActivateConnection;
    }

    /**
     * Redis 部署模式枚举。
     */
    public enum Mode {
        /** 单机模式。 */
        SINGLE,
        /** 集群模式。 */
        CLUSTER,
        /** 哨兵模式。 */
        SENTINEL
    }
}
