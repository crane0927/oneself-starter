package com.oneself.redis.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oneself.redis")
public class OneselfRedisProperties {

    /**
     * Enable the enhanced Redis helpers provided by this starter.
     */
    private boolean enabled = true;

    /**
     * Optional key prefix applied by RedisOps.
     */
    private String keyPrefix = "";

    /**
     * Enable key pattern validation.
     */
    private boolean validateKeyPattern = false;

    /**
     * Regex pattern for Redis keys (applies after prefix).
     */
    private String keyPattern = "^[a-zA-Z0-9:_-]+$";

    /**
     * Enable metrics collection (requires Micrometer on classpath).
     */
    private boolean metricsEnabled = false;

    /**
     * Enable structured logging for operations.
     */
    private boolean loggingEnabled = false;

    /**
     * Enable Spring Cache integration.
     */
    private boolean cacheEnabled = false;

    /**
     * Default cache TTL.
     */
    private java.time.Duration cacheTtl = java.time.Duration.ofMinutes(30);

    /**
     * Cache name prefix.
     */
    private String cacheKeyPrefix = "";

    /**
     * Redis deployment mode.
     */
    private Mode mode = Mode.SINGLE;

    /**
     * Redis password (single or cluster).
     */
    private String password;

    /**
     * Sentinel password (optional, separate from Redis password).
     */
    private String sentinelPassword;

    /**
     * Redis database index (single only).
     */
    private Integer database = 0;

    /**
     * Single node host.
     */
    private String host = "localhost";

    /**
     * Single node port.
     */
    private Integer port = 6379;

    /**
     * Cluster node list (host:port).
     */
    private String[] nodes = new String[0];

    /**
     * Sentinel master name.
     */
    private String sentinelMaster;

    /**
     * Sentinel node list (host:port).
     */
    private String[] sentinelNodes = new String[0];

    /**
     * Enable SSL/TLS for Redis connections.
     */
    private boolean sslEnabled = false;

    /**
     * Verify SSL peer certificate.
     */
    private boolean sslVerifyPeer = true;

    /**
     * SSL truststore path.
     */
    private String sslTruststore;

    /**
     * SSL truststore password.
     */
    private String sslTruststorePassword;

    /**
     * SSL keystore path.
     */
    private String sslKeystore;

    /**
     * SSL keystore password.
     */
    private String sslKeystorePassword;

    /**
     * Command timeout.
     */
    private java.time.Duration timeout = java.time.Duration.ofSeconds(2);

    /**
     * Connect timeout.
     */
    private java.time.Duration connectTimeout = java.time.Duration.ofSeconds(2);

    /**
     * Enable lettuce connection pooling.
     */
    private boolean poolEnabled = false;

    /**
     * Lettuce pool max active connections.
     */
    private int poolMaxActive = 8;

    /**
     * Lettuce pool max idle connections.
     */
    private int poolMaxIdle = 8;

    /**
     * Lettuce pool min idle connections.
     */
    private int poolMinIdle = 0;

    /**
     * Lettuce pool max wait.
     */
    private java.time.Duration poolMaxWait = java.time.Duration.ofSeconds(1);

    /**
     * Lettuce client name.
     */
    private String clientName;

    /**
     * Read preference: MASTER, MASTER_PREFERRED, REPLICA, REPLICA_PREFERRED, NEAREST, ANY.
     */
    private String readFrom;

    /**
     * Auto reconnect.
     */
    private boolean autoReconnect = true;

    /**
     * Disconnected behavior: DEFAULT, REJECT_COMMANDS, ACCEPT_COMMANDS.
     */
    private String disconnectedBehavior = "DEFAULT";

    /**
     * PING before activating a connection.
     */
    private boolean pingBeforeActivateConnection = false;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public boolean isValidateKeyPattern() {
        return validateKeyPattern;
    }

    public void setValidateKeyPattern(boolean validateKeyPattern) {
        this.validateKeyPattern = validateKeyPattern;
    }

    public String getKeyPattern() {
        return keyPattern;
    }

    public void setKeyPattern(String keyPattern) {
        this.keyPattern = keyPattern;
    }

    public boolean isMetricsEnabled() {
        return metricsEnabled;
    }

    public void setMetricsEnabled(boolean metricsEnabled) {
        this.metricsEnabled = metricsEnabled;
    }

    public boolean isLoggingEnabled() {
        return loggingEnabled;
    }

    public void setLoggingEnabled(boolean loggingEnabled) {
        this.loggingEnabled = loggingEnabled;
    }

    public boolean isCacheEnabled() {
        return cacheEnabled;
    }

    public void setCacheEnabled(boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
    }

    public java.time.Duration getCacheTtl() {
        return cacheTtl;
    }

    public void setCacheTtl(java.time.Duration cacheTtl) {
        this.cacheTtl = cacheTtl;
    }

    public String getCacheKeyPrefix() {
        return cacheKeyPrefix;
    }

    public void setCacheKeyPrefix(String cacheKeyPrefix) {
        this.cacheKeyPrefix = cacheKeyPrefix;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSentinelPassword() {
        return sentinelPassword;
    }

    public void setSentinelPassword(String sentinelPassword) {
        this.sentinelPassword = sentinelPassword;
    }

    public Integer getDatabase() {
        return database;
    }

    public void setDatabase(Integer database) {
        this.database = database;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String[] getNodes() {
        return nodes;
    }

    public void setNodes(String[] nodes) {
        this.nodes = nodes;
    }

    public String getSentinelMaster() {
        return sentinelMaster;
    }

    public void setSentinelMaster(String sentinelMaster) {
        this.sentinelMaster = sentinelMaster;
    }

    public String[] getSentinelNodes() {
        return sentinelNodes;
    }

    public void setSentinelNodes(String[] sentinelNodes) {
        this.sentinelNodes = sentinelNodes;
    }

    public boolean isSslEnabled() {
        return sslEnabled;
    }

    public void setSslEnabled(boolean sslEnabled) {
        this.sslEnabled = sslEnabled;
    }

    public boolean isSslVerifyPeer() {
        return sslVerifyPeer;
    }

    public void setSslVerifyPeer(boolean sslVerifyPeer) {
        this.sslVerifyPeer = sslVerifyPeer;
    }

    public String getSslTruststore() {
        return sslTruststore;
    }

    public void setSslTruststore(String sslTruststore) {
        this.sslTruststore = sslTruststore;
    }

    public String getSslTruststorePassword() {
        return sslTruststorePassword;
    }

    public void setSslTruststorePassword(String sslTruststorePassword) {
        this.sslTruststorePassword = sslTruststorePassword;
    }

    public String getSslKeystore() {
        return sslKeystore;
    }

    public void setSslKeystore(String sslKeystore) {
        this.sslKeystore = sslKeystore;
    }

    public String getSslKeystorePassword() {
        return sslKeystorePassword;
    }

    public void setSslKeystorePassword(String sslKeystorePassword) {
        this.sslKeystorePassword = sslKeystorePassword;
    }

    public java.time.Duration getTimeout() {
        return timeout;
    }

    public void setTimeout(java.time.Duration timeout) {
        this.timeout = timeout;
    }

    public java.time.Duration getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(java.time.Duration connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public boolean isPoolEnabled() {
        return poolEnabled;
    }

    public void setPoolEnabled(boolean poolEnabled) {
        this.poolEnabled = poolEnabled;
    }

    public int getPoolMaxActive() {
        return poolMaxActive;
    }

    public void setPoolMaxActive(int poolMaxActive) {
        this.poolMaxActive = poolMaxActive;
    }

    public int getPoolMaxIdle() {
        return poolMaxIdle;
    }

    public void setPoolMaxIdle(int poolMaxIdle) {
        this.poolMaxIdle = poolMaxIdle;
    }

    public int getPoolMinIdle() {
        return poolMinIdle;
    }

    public void setPoolMinIdle(int poolMinIdle) {
        this.poolMinIdle = poolMinIdle;
    }

    public java.time.Duration getPoolMaxWait() {
        return poolMaxWait;
    }

    public void setPoolMaxWait(java.time.Duration poolMaxWait) {
        this.poolMaxWait = poolMaxWait;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getReadFrom() {
        return readFrom;
    }

    public void setReadFrom(String readFrom) {
        this.readFrom = readFrom;
    }

    public boolean isAutoReconnect() {
        return autoReconnect;
    }

    public void setAutoReconnect(boolean autoReconnect) {
        this.autoReconnect = autoReconnect;
    }

    public String getDisconnectedBehavior() {
        return disconnectedBehavior;
    }

    public void setDisconnectedBehavior(String disconnectedBehavior) {
        this.disconnectedBehavior = disconnectedBehavior;
    }

    public boolean isPingBeforeActivateConnection() {
        return pingBeforeActivateConnection;
    }

    public void setPingBeforeActivateConnection(boolean pingBeforeActivateConnection) {
        this.pingBeforeActivateConnection = pingBeforeActivateConnection;
    }

    public enum Mode {
        SINGLE,
        CLUSTER,
        SENTINEL
    }
}
