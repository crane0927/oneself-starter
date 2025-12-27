package com.oneself.kafka.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Kafka Starter 统一配置。
 */
@ConfigurationProperties(prefix = "oneself.kafka")
public class OneselfKafkaProperties {

    /**
     * 是否启用本 Starter。
     */
    private boolean enabled = true;

    /**
     * 默认发送主题。
     */
    private String defaultTopic;

    /**
     * Kafka 集群地址（bootstrap.servers）。
     */
    private String bootstrapServers;

    /**
     * 安全协议（security.protocol）。
     */
    private String securityProtocol;

    /**
     * SASL 机制（sasl.mechanism）。
     */
    private String saslMechanism;

    /**
     * SASL JAAS 配置（sasl.jaas.config）。
     */
    private String saslJaasConfig;

    /**
     * SSL truststore 路径。
     */
    private String sslTruststoreLocation;

    /**
     * SSL truststore 密码。
     */
    private String sslTruststorePassword;

    /**
     * SSL keystore 路径。
     */
    private String sslKeystoreLocation;

    /**
     * SSL keystore 密码。
     */
    private String sslKeystorePassword;

    /**
     * 事件 schema 版本。
     */
    private String schemaVersion = "v1";

    /**
     * 需要保证顺序的 topic 列表。
     */
    private String[] orderedTopics = new String[0];

    /**
     * 非顺序 topic 的 key 策略。
     */
    private UnorderedKeyStrategy unorderedKeyStrategy = UnorderedKeyStrategy.NONE;

    /**
     * 是否启用重试/死信处理。
     */
    private boolean retryEnabled = true;

    /**
     * 重试 topic 延迟级别（用于命名）。
     */
    private String[] retryTopicDelays = new String[] {"1m", "10m", "1h"};

    /**
     * 死信 topic 后缀。
     */
    private String dlqSuffix = "dlq";

    /**
     * 本地重试次数（不含首次）。
     */
    private int localRetryAttempts = 2;

    /**
     * 本地重试间隔。
     */
    private java.time.Duration localRetryBackoff = java.time.Duration.ofSeconds(1);

    /**
     * 非重试异常类名列表（全限定名）。
     */
    private String[] nonRetryableExceptions = new String[0];

    /**
     * 是否启用 Outbox（仅提供模型与接口）。
     */
    private boolean outboxEnabled = false;

    /**
     * 是否启用幂等生产者。
     */
    private boolean idempotenceEnabled = true;

    /**
     * 幂等存储类型。
     */
    private IdempotentStore idempotentStore = IdempotentStore.NONE;

    /**
     * 幂等 key 前缀。
     */
    private String idempotentKeyPrefix = "kafka:idem:";

    /**
     * 处理中状态 TTL。
     */
    private java.time.Duration idempotentProcessingTtl = java.time.Duration.ofMinutes(10);

    /**
     * 完成状态 TTL。
     */
    private java.time.Duration idempotentDoneTtl = java.time.Duration.ofDays(7);

    /**
     * 幂等表名（DB 模式）。
     */
    private String idempotentTableName = "kafka_idempotent_record";

    /**
     * 生产者默认 acks。
     */
    private String producerAcks = "all";

    /**
     * 生产者 delivery timeout。
     */
    private java.time.Duration producerDeliveryTimeout = java.time.Duration.ofSeconds(120);

    /**
     * 生产者 request timeout。
     */
    private java.time.Duration producerRequestTimeout = java.time.Duration.ofSeconds(30);

    /**
     * 生产者 linger.ms。
     */
    private java.time.Duration producerLinger = java.time.Duration.ofMillis(10);

    /**
     * 生产者 batch.size。
     */
    private Integer producerBatchSize = 32768;

    /**
     * 生产者 compression.type。
     */
    private String producerCompressionType = "lz4";

    /**
     * 消费者是否自动提交 offset。
     */
    private boolean consumerEnableAutoCommit = false;

    /**
     * 消费者 auto.offset.reset。
     */
    private String consumerAutoOffsetReset = "latest";

    /**
     * 消费者 max.poll.records。
     */
    private Integer consumerMaxPollRecords = 300;

    /**
     * 消费者 max.poll.interval.ms。
     */
    private java.time.Duration consumerMaxPollInterval = java.time.Duration.ofMinutes(5);

    /**
     * 消费者 session.timeout.ms。
     */
    private java.time.Duration consumerSessionTimeout = java.time.Duration.ofSeconds(15);

    /**
     * 消费者 heartbeat.interval.ms。
     */
    private java.time.Duration consumerHeartbeatInterval = java.time.Duration.ofSeconds(5);

    /**
     * 消费者 fetch.max.bytes。
     */
    private Integer consumerFetchMaxBytes = 50 * 1024 * 1024;

    /**
     * 消费者 isolation.level。
     */
    private String consumerIsolationLevel = "read_committed";

    /**
     * 监听器 ack 模式。
     */
    private String listenerAckMode = "MANUAL";

    /**
     * 监听器并发数。
     */
    private Integer listenerConcurrency;

    /**
     * 是否启用统一生产者封装。
     */
    private boolean producerEnabled = true;

    /**
     * 是否启用统一消费者封装。
     */
    private boolean consumerEnabled = true;

    /**
     * 消费者默认 groupId（可在 @KafkaListener 覆盖）。
     */
    private String consumerGroupId;

    /**
     * 生产者默认 clientId。
     */
    private String producerClientId;

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
     * 获取DefaultTopic。
     */
    public String getDefaultTopic() {
        return defaultTopic;
    }

    /**
     * 设置DefaultTopic。
     */
    public void setDefaultTopic(String defaultTopic) {
        this.defaultTopic = defaultTopic;
    }

    /**
     * 获取BootstrapServers。
     */
    public String getBootstrapServers() {
        return bootstrapServers;
    }

    /**
     * 设置BootstrapServers。
     */
    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    /**
     * 获取SecurityProtocol。
     */
    public String getSecurityProtocol() {
        return securityProtocol;
    }

    /**
     * 设置SecurityProtocol。
     */
    public void setSecurityProtocol(String securityProtocol) {
        this.securityProtocol = securityProtocol;
    }

    /**
     * 获取SaslMechanism。
     */
    public String getSaslMechanism() {
        return saslMechanism;
    }

    /**
     * 设置SaslMechanism。
     */
    public void setSaslMechanism(String saslMechanism) {
        this.saslMechanism = saslMechanism;
    }

    /**
     * 获取SaslJaasConfig。
     */
    public String getSaslJaasConfig() {
        return saslJaasConfig;
    }

    /**
     * 设置SaslJaasConfig。
     */
    public void setSaslJaasConfig(String saslJaasConfig) {
        this.saslJaasConfig = saslJaasConfig;
    }

    /**
     * 获取SslTruststoreLocation。
     */
    public String getSslTruststoreLocation() {
        return sslTruststoreLocation;
    }

    /**
     * 设置SslTruststoreLocation。
     */
    public void setSslTruststoreLocation(String sslTruststoreLocation) {
        this.sslTruststoreLocation = sslTruststoreLocation;
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
     * 获取SslKeystoreLocation。
     */
    public String getSslKeystoreLocation() {
        return sslKeystoreLocation;
    }

    /**
     * 设置SslKeystoreLocation。
     */
    public void setSslKeystoreLocation(String sslKeystoreLocation) {
        this.sslKeystoreLocation = sslKeystoreLocation;
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
     * 获取SchemaVersion。
     */
    public String getSchemaVersion() {
        return schemaVersion;
    }

    /**
     * 设置SchemaVersion。
     */
    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    /**
     * 获取OrderedTopics。
     */
    public String[] getOrderedTopics() {
        return orderedTopics;
    }

    /**
     * 设置OrderedTopics。
     */
    public void setOrderedTopics(String[] orderedTopics) {
        this.orderedTopics = orderedTopics;
    }

    /**
     * 获取UnorderedKeyStrategy。
     */
    public UnorderedKeyStrategy getUnorderedKeyStrategy() {
        return unorderedKeyStrategy;
    }

    /**
     * 设置UnorderedKeyStrategy。
     */
    public void setUnorderedKeyStrategy(UnorderedKeyStrategy unorderedKeyStrategy) {
        this.unorderedKeyStrategy = unorderedKeyStrategy;
    }

    /**
     * 是否RetryEnabled。
     */
    public boolean isRetryEnabled() {
        return retryEnabled;
    }

    /**
     * 设置RetryEnabled。
     */
    public void setRetryEnabled(boolean retryEnabled) {
        this.retryEnabled = retryEnabled;
    }

    /**
     * 获取RetryTopicDelays。
     */
    public String[] getRetryTopicDelays() {
        return retryTopicDelays;
    }

    /**
     * 设置RetryTopicDelays。
     */
    public void setRetryTopicDelays(String[] retryTopicDelays) {
        this.retryTopicDelays = retryTopicDelays;
    }

    /**
     * 获取DlqSuffix。
     */
    public String getDlqSuffix() {
        return dlqSuffix;
    }

    /**
     * 设置DlqSuffix。
     */
    public void setDlqSuffix(String dlqSuffix) {
        this.dlqSuffix = dlqSuffix;
    }

    /**
     * 获取LocalRetryAttempts。
     */
    public int getLocalRetryAttempts() {
        return localRetryAttempts;
    }

    /**
     * 设置LocalRetryAttempts。
     */
    public void setLocalRetryAttempts(int localRetryAttempts) {
        this.localRetryAttempts = localRetryAttempts;
    }

    /**
     * 获取LocalRetryBackoff。
     */
    public java.time.Duration getLocalRetryBackoff() {
        return localRetryBackoff;
    }

    /**
     * 设置LocalRetryBackoff。
     */
    public void setLocalRetryBackoff(java.time.Duration localRetryBackoff) {
        this.localRetryBackoff = localRetryBackoff;
    }

    /**
     * 获取NonRetryableExceptions。
     */
    public String[] getNonRetryableExceptions() {
        return nonRetryableExceptions;
    }

    /**
     * 设置NonRetryableExceptions。
     */
    public void setNonRetryableExceptions(String[] nonRetryableExceptions) {
        this.nonRetryableExceptions = nonRetryableExceptions;
    }

    /**
     * 是否OutboxEnabled。
     */
    public boolean isOutboxEnabled() {
        return outboxEnabled;
    }

    /**
     * 设置OutboxEnabled。
     */
    public void setOutboxEnabled(boolean outboxEnabled) {
        this.outboxEnabled = outboxEnabled;
    }

    /**
     * 是否IdempotenceEnabled。
     */
    public boolean isIdempotenceEnabled() {
        return idempotenceEnabled;
    }

    /**
     * 设置IdempotenceEnabled。
     */
    public void setIdempotenceEnabled(boolean idempotenceEnabled) {
        this.idempotenceEnabled = idempotenceEnabled;
    }

    /**
     * 获取IdempotentStore。
     */
    public IdempotentStore getIdempotentStore() {
        return idempotentStore;
    }

    /**
     * 设置IdempotentStore。
     */
    public void setIdempotentStore(IdempotentStore idempotentStore) {
        this.idempotentStore = idempotentStore;
    }

    /**
     * 获取IdempotentKeyPrefix。
     */
    public String getIdempotentKeyPrefix() {
        return idempotentKeyPrefix;
    }

    /**
     * 设置IdempotentKeyPrefix。
     */
    public void setIdempotentKeyPrefix(String idempotentKeyPrefix) {
        this.idempotentKeyPrefix = idempotentKeyPrefix;
    }

    /**
     * 获取IdempotentProcessingTtl。
     */
    public java.time.Duration getIdempotentProcessingTtl() {
        return idempotentProcessingTtl;
    }

    /**
     * 设置IdempotentProcessingTtl。
     */
    public void setIdempotentProcessingTtl(java.time.Duration idempotentProcessingTtl) {
        this.idempotentProcessingTtl = idempotentProcessingTtl;
    }

    /**
     * 获取IdempotentDoneTtl。
     */
    public java.time.Duration getIdempotentDoneTtl() {
        return idempotentDoneTtl;
    }

    /**
     * 设置IdempotentDoneTtl。
     */
    public void setIdempotentDoneTtl(java.time.Duration idempotentDoneTtl) {
        this.idempotentDoneTtl = idempotentDoneTtl;
    }

    /**
     * 获取IdempotentTableName。
     */
    public String getIdempotentTableName() {
        return idempotentTableName;
    }

    /**
     * 设置IdempotentTableName。
     */
    public void setIdempotentTableName(String idempotentTableName) {
        this.idempotentTableName = idempotentTableName;
    }

    /**
     * 获取ProducerAcks。
     */
    public String getProducerAcks() {
        return producerAcks;
    }

    /**
     * 设置ProducerAcks。
     */
    public void setProducerAcks(String producerAcks) {
        this.producerAcks = producerAcks;
    }

    /**
     * 获取ProducerDeliveryTimeout。
     */
    public java.time.Duration getProducerDeliveryTimeout() {
        return producerDeliveryTimeout;
    }

    /**
     * 设置ProducerDeliveryTimeout。
     */
    public void setProducerDeliveryTimeout(java.time.Duration producerDeliveryTimeout) {
        this.producerDeliveryTimeout = producerDeliveryTimeout;
    }

    /**
     * 获取ProducerRequestTimeout。
     */
    public java.time.Duration getProducerRequestTimeout() {
        return producerRequestTimeout;
    }

    /**
     * 设置ProducerRequestTimeout。
     */
    public void setProducerRequestTimeout(java.time.Duration producerRequestTimeout) {
        this.producerRequestTimeout = producerRequestTimeout;
    }

    /**
     * 获取ProducerLinger。
     */
    public java.time.Duration getProducerLinger() {
        return producerLinger;
    }

    /**
     * 设置ProducerLinger。
     */
    public void setProducerLinger(java.time.Duration producerLinger) {
        this.producerLinger = producerLinger;
    }

    /**
     * 获取ProducerBatchSize。
     */
    public Integer getProducerBatchSize() {
        return producerBatchSize;
    }

    /**
     * 设置ProducerBatchSize。
     */
    public void setProducerBatchSize(Integer producerBatchSize) {
        this.producerBatchSize = producerBatchSize;
    }

    /**
     * 获取ProducerCompressionType。
     */
    public String getProducerCompressionType() {
        return producerCompressionType;
    }

    /**
     * 设置ProducerCompressionType。
     */
    public void setProducerCompressionType(String producerCompressionType) {
        this.producerCompressionType = producerCompressionType;
    }

    /**
     * 是否ConsumerEnableAutoCommit。
     */
    public boolean isConsumerEnableAutoCommit() {
        return consumerEnableAutoCommit;
    }

    /**
     * 设置ConsumerEnableAutoCommit。
     */
    public void setConsumerEnableAutoCommit(boolean consumerEnableAutoCommit) {
        this.consumerEnableAutoCommit = consumerEnableAutoCommit;
    }

    /**
     * 获取ConsumerAutoOffsetReset。
     */
    public String getConsumerAutoOffsetReset() {
        return consumerAutoOffsetReset;
    }

    /**
     * 设置ConsumerAutoOffsetReset。
     */
    public void setConsumerAutoOffsetReset(String consumerAutoOffsetReset) {
        this.consumerAutoOffsetReset = consumerAutoOffsetReset;
    }

    /**
     * 获取ConsumerMaxPollRecords。
     */
    public Integer getConsumerMaxPollRecords() {
        return consumerMaxPollRecords;
    }

    /**
     * 设置ConsumerMaxPollRecords。
     */
    public void setConsumerMaxPollRecords(Integer consumerMaxPollRecords) {
        this.consumerMaxPollRecords = consumerMaxPollRecords;
    }

    /**
     * 获取ConsumerMaxPollInterval。
     */
    public java.time.Duration getConsumerMaxPollInterval() {
        return consumerMaxPollInterval;
    }

    /**
     * 设置ConsumerMaxPollInterval。
     */
    public void setConsumerMaxPollInterval(java.time.Duration consumerMaxPollInterval) {
        this.consumerMaxPollInterval = consumerMaxPollInterval;
    }

    /**
     * 获取ConsumerSessionTimeout。
     */
    public java.time.Duration getConsumerSessionTimeout() {
        return consumerSessionTimeout;
    }

    /**
     * 设置ConsumerSessionTimeout。
     */
    public void setConsumerSessionTimeout(java.time.Duration consumerSessionTimeout) {
        this.consumerSessionTimeout = consumerSessionTimeout;
    }

    /**
     * 获取ConsumerHeartbeatInterval。
     */
    public java.time.Duration getConsumerHeartbeatInterval() {
        return consumerHeartbeatInterval;
    }

    /**
     * 设置ConsumerHeartbeatInterval。
     */
    public void setConsumerHeartbeatInterval(java.time.Duration consumerHeartbeatInterval) {
        this.consumerHeartbeatInterval = consumerHeartbeatInterval;
    }

    /**
     * 获取ConsumerFetchMaxBytes。
     */
    public Integer getConsumerFetchMaxBytes() {
        return consumerFetchMaxBytes;
    }

    /**
     * 设置ConsumerFetchMaxBytes。
     */
    public void setConsumerFetchMaxBytes(Integer consumerFetchMaxBytes) {
        this.consumerFetchMaxBytes = consumerFetchMaxBytes;
    }

    /**
     * 获取ConsumerIsolationLevel。
     */
    public String getConsumerIsolationLevel() {
        return consumerIsolationLevel;
    }

    /**
     * 设置ConsumerIsolationLevel。
     */
    public void setConsumerIsolationLevel(String consumerIsolationLevel) {
        this.consumerIsolationLevel = consumerIsolationLevel;
    }

    /**
     * 获取ListenerAckMode。
     */
    public String getListenerAckMode() {
        return listenerAckMode;
    }

    /**
     * 设置ListenerAckMode。
     */
    public void setListenerAckMode(String listenerAckMode) {
        this.listenerAckMode = listenerAckMode;
    }

    /**
     * 获取ListenerConcurrency。
     */
    public Integer getListenerConcurrency() {
        return listenerConcurrency;
    }

    /**
     * 设置ListenerConcurrency。
     */
    public void setListenerConcurrency(Integer listenerConcurrency) {
        this.listenerConcurrency = listenerConcurrency;
    }

    /**
     * 是否ProducerEnabled。
     */
    public boolean isProducerEnabled() {
        return producerEnabled;
    }

    /**
     * 设置ProducerEnabled。
     */
    public void setProducerEnabled(boolean producerEnabled) {
        this.producerEnabled = producerEnabled;
    }

    /**
     * 是否ConsumerEnabled。
     */
    public boolean isConsumerEnabled() {
        return consumerEnabled;
    }

    /**
     * 设置ConsumerEnabled。
     */
    public void setConsumerEnabled(boolean consumerEnabled) {
        this.consumerEnabled = consumerEnabled;
    }

    /**
     * 获取ConsumerGroupId。
     */
    public String getConsumerGroupId() {
        return consumerGroupId;
    }

    /**
     * 设置ConsumerGroupId。
     */
    public void setConsumerGroupId(String consumerGroupId) {
        this.consumerGroupId = consumerGroupId;
    }

    /**
     * 获取ProducerClientId。
     */
    public String getProducerClientId() {
        return producerClientId;
    }

    /**
     * 设置ProducerClientId。
     */
    public void setProducerClientId(String producerClientId) {
        this.producerClientId = producerClientId;
    }

    /**
     * 非顺序 topic 的 key 策略枚举。
     */
    public enum UnorderedKeyStrategy {
        /** 不设置 key，由 Kafka 使用 round-robin。 */
        NONE,
        /** 生成随机 key。 */
        RANDOM
    }

    /**
     * 幂等存储枚举。
     */
    public enum IdempotentStore {
        /** 不启用幂等存储。 */
        NONE,
        /** Redis 幂等。 */
        REDIS,
        /** DB 幂等。 */
        DB
    }
}
