package com.oneself.kafka.autoconfigure;

import com.oneself.kafka.core.DbKafkaIdempotentExecutor;
import com.oneself.kafka.core.JdbcKafkaIdempotentRepository;
import com.oneself.kafka.core.KafkaConsumerAdapter;
import com.oneself.kafka.core.KafkaIdempotentExecutor;
import com.oneself.kafka.core.KafkaIdempotentRepository;
import com.oneself.kafka.core.KafkaOps;
import com.oneself.kafka.core.KafkaRetryRecoverer;
import com.oneself.kafka.core.RedisKafkaIdempotentExecutor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import java.util.Map;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.util.backoff.FixedBackOff;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Kafka Starter 自动装配。
 */
@AutoConfiguration
@EnableConfigurationProperties(OneselfKafkaProperties.class)
@ConditionalOnClass(KafkaTemplate.class)
@ConditionalOnProperty(prefix = "oneself.kafka", name = "enabled", havingValue = "true", matchIfMissing = true)
public class OneselfKafkaAutoConfiguration {

    /**
     * 统一生产者封装。
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "oneself.kafka", name = "producer-enabled", havingValue = "true", matchIfMissing = true)
    public KafkaOps kafkaOps(KafkaTemplate<String, Object> kafkaTemplate, OneselfKafkaProperties properties) {
        return new KafkaOps(kafkaTemplate, properties);
    }

    /**
     * 幂等生产者默认配置。
     */
    @Bean
    @ConditionalOnMissingBean(ProducerFactory.class)
    @ConditionalOnProperty(prefix = "oneself.kafka", name = "idempotence-enabled", havingValue = "true", matchIfMissing = true)
    public ProducerFactory<String, Object> kafkaProducerFactory(KafkaProperties kafkaProperties,
                                                                OneselfKafkaProperties properties) {
        Map<String, Object> configs = kafkaProperties.buildProducerProperties();
        applyConnectionSettings(configs, properties);
        configs.putIfAbsent(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        configs.putIfAbsent(ProducerConfig.ACKS_CONFIG, properties.getProducerAcks());
        configs.putIfAbsent(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE);
        configs.putIfAbsent(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG,
                (int) properties.getProducerDeliveryTimeout().toMillis());
        configs.putIfAbsent(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG,
                (int) properties.getProducerRequestTimeout().toMillis());
        configs.putIfAbsent(ProducerConfig.LINGER_MS_CONFIG,
                (int) properties.getProducerLinger().toMillis());
        configs.putIfAbsent(ProducerConfig.BATCH_SIZE_CONFIG, properties.getProducerBatchSize());
        configs.putIfAbsent(ProducerConfig.COMPRESSION_TYPE_CONFIG, properties.getProducerCompressionType());
        if (properties.getProducerClientId() != null && !properties.getProducerClientId().isBlank()) {
            configs.putIfAbsent(ProducerConfig.CLIENT_ID_CONFIG, properties.getProducerClientId());
        }
        return new DefaultKafkaProducerFactory<>(configs);
    }

    /**
     * 生产者默认配置（可覆盖）。
     */
    @Bean
    @ConditionalOnMissingBean(ProducerFactory.class)
    @ConditionalOnProperty(prefix = "oneself.kafka", name = "idempotence-enabled", havingValue = "false")
    public ProducerFactory<String, Object> kafkaProducerFactoryNonIdempotent(KafkaProperties kafkaProperties,
                                                                             OneselfKafkaProperties properties) {
        Map<String, Object> configs = kafkaProperties.buildProducerProperties();
        applyConnectionSettings(configs, properties);
        configs.putIfAbsent(ProducerConfig.ACKS_CONFIG, properties.getProducerAcks());
        configs.putIfAbsent(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG,
                (int) properties.getProducerDeliveryTimeout().toMillis());
        configs.putIfAbsent(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG,
                (int) properties.getProducerRequestTimeout().toMillis());
        configs.putIfAbsent(ProducerConfig.LINGER_MS_CONFIG,
                (int) properties.getProducerLinger().toMillis());
        configs.putIfAbsent(ProducerConfig.BATCH_SIZE_CONFIG, properties.getProducerBatchSize());
        configs.putIfAbsent(ProducerConfig.COMPRESSION_TYPE_CONFIG, properties.getProducerCompressionType());
        if (properties.getProducerClientId() != null && !properties.getProducerClientId().isBlank()) {
            configs.putIfAbsent(ProducerConfig.CLIENT_ID_CONFIG, properties.getProducerClientId());
        }
        return new DefaultKafkaProducerFactory<>(configs);
    }

    /**
     * 消费者默认配置。
     */
    @Bean
    @ConditionalOnMissingBean(ConsumerFactory.class)
    public ConsumerFactory<Object, Object> kafkaConsumerFactory(KafkaProperties kafkaProperties,
                                                                OneselfKafkaProperties properties) {
        Map<String, Object> configs = kafkaProperties.buildConsumerProperties();
        applyConnectionSettings(configs, properties);
        configs.putIfAbsent(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, properties.isConsumerEnableAutoCommit());
        configs.putIfAbsent(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, properties.getConsumerAutoOffsetReset());
        configs.putIfAbsent(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, properties.getConsumerMaxPollRecords());
        configs.putIfAbsent(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG,
                (int) properties.getConsumerMaxPollInterval().toMillis());
        configs.putIfAbsent(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG,
                (int) properties.getConsumerSessionTimeout().toMillis());
        configs.putIfAbsent(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG,
                (int) properties.getConsumerHeartbeatInterval().toMillis());
        configs.putIfAbsent(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, properties.getConsumerFetchMaxBytes());
        configs.putIfAbsent(ConsumerConfig.ISOLATION_LEVEL_CONFIG, properties.getConsumerIsolationLevel());
        if (properties.getConsumerGroupId() != null && !properties.getConsumerGroupId().isBlank()) {
            configs.putIfAbsent(ConsumerConfig.GROUP_ID_CONFIG, properties.getConsumerGroupId());
        }
        return new DefaultKafkaConsumerFactory<>(configs);
    }

    /**
     * 默认错误处理器（本地重试 + 重试/DLQ）。
     */
    @Bean
    @ConditionalOnMissingBean(CommonErrorHandler.class)
    public DefaultErrorHandler kafkaErrorHandler(ConsumerRecordRecoverer recoverer, OneselfKafkaProperties properties) {
        long backoffMs = properties.getLocalRetryBackoff().toMillis();
        FixedBackOff backOff = new FixedBackOff(backoffMs, properties.getLocalRetryAttempts());
        DefaultErrorHandler handler = new DefaultErrorHandler(recoverer, backOff);
        for (String className : properties.getNonRetryableExceptions()) {
            Class<?> target = loadClass(className);
            if (target != null && Exception.class.isAssignableFrom(target)) {
                handler.addNotRetryableExceptions(target.asSubclass(Exception.class));
            }
        }
        return handler;
    }

    /**
     * 重试/死信转发器。
     */
    @Bean
    @ConditionalOnMissingBean(ConsumerRecordRecoverer.class)
    public ConsumerRecordRecoverer kafkaRetryRecoverer(KafkaTemplate<?, ?> kafkaTemplate,
                                                       OneselfKafkaProperties properties) {
        @SuppressWarnings("unchecked")
        KafkaTemplate<Object, Object> template = (KafkaTemplate<Object, Object>) kafkaTemplate;
        return new KafkaRetryRecoverer(template, properties);
    }

    /**
     * 统一 Listener 容器工厂，默认手动 ack 与统一错误处理。
     */
    @Bean
    @ConditionalOnMissingBean(name = "kafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<Object, Object> kafkaListenerContainerFactory(
            ConsumerFactory<Object, Object> consumerFactory,
            CommonErrorHandler errorHandler,
            OneselfKafkaProperties properties) {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(errorHandler);
        factory.getContainerProperties().setAckMode(parseAckMode(properties.getListenerAckMode()));
        if (properties.getListenerConcurrency() != null) {
            factory.setConcurrency(properties.getListenerConcurrency());
        }
        return factory;
    }
    /**
     * 统一消费者适配器。
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "oneself.kafka", name = "consumer-enabled", havingValue = "true", matchIfMissing = true)
    public KafkaConsumerAdapter kafkaConsumerAdapter() {
        return new KafkaConsumerAdapter();
    }

    /**
     * Redis 幂等执行器。
     */
    @Bean
    @ConditionalOnClass(StringRedisTemplate.class)
    @ConditionalOnMissingBean(KafkaIdempotentExecutor.class)
    @ConditionalOnProperty(prefix = "oneself.kafka", name = "idempotent-store", havingValue = "REDIS")
    public KafkaIdempotentExecutor redisKafkaIdempotentExecutor(StringRedisTemplate redisTemplate,
                                                                OneselfKafkaProperties properties) {
        return new RedisKafkaIdempotentExecutor(redisTemplate, properties);
    }

    /**
     * DB 幂等仓储。
     */
    @Bean
    @ConditionalOnClass(JdbcTemplate.class)
    @ConditionalOnMissingBean(KafkaIdempotentRepository.class)
    @ConditionalOnProperty(prefix = "oneself.kafka", name = "idempotent-store", havingValue = "DB")
    public KafkaIdempotentRepository jdbcKafkaIdempotentRepository(JdbcTemplate jdbcTemplate,
                                                                    OneselfKafkaProperties properties) {
        return new JdbcKafkaIdempotentRepository(jdbcTemplate, properties.getIdempotentTableName());
    }

    /**
     * DB 幂等执行器。
     */
    @Bean
    @ConditionalOnClass(JdbcTemplate.class)
    @ConditionalOnMissingBean(KafkaIdempotentExecutor.class)
    @ConditionalOnProperty(prefix = "oneself.kafka", name = "idempotent-store", havingValue = "DB")
    public KafkaIdempotentExecutor dbKafkaIdempotentExecutor(KafkaIdempotentRepository repository,
                                                             OneselfKafkaProperties properties) {
        String groupId = properties.getConsumerGroupId() == null ? "unknown" : properties.getConsumerGroupId();
        return new DbKafkaIdempotentExecutor(repository, groupId);
    }

    private ContainerProperties.AckMode parseAckMode(String value) {
        if (value == null || value.isBlank()) {
            return ContainerProperties.AckMode.MANUAL;
        }
        String normalized = value.trim().toUpperCase();
        return ContainerProperties.AckMode.valueOf(normalized);
    }

    /**
     * 应用统一连接参数。
     */
    private void applyConnectionSettings(Map<String, Object> configs, OneselfKafkaProperties properties) {
        if (properties.getBootstrapServers() != null && !properties.getBootstrapServers().isBlank()) {
            configs.putIfAbsent(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers());
        }
        if (properties.getSecurityProtocol() != null && !properties.getSecurityProtocol().isBlank()) {
            configs.putIfAbsent(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, properties.getSecurityProtocol());
        }
        if (properties.getSaslMechanism() != null && !properties.getSaslMechanism().isBlank()) {
            configs.putIfAbsent(SaslConfigs.SASL_MECHANISM, properties.getSaslMechanism());
        }
        if (properties.getSaslJaasConfig() != null && !properties.getSaslJaasConfig().isBlank()) {
            configs.putIfAbsent(SaslConfigs.SASL_JAAS_CONFIG, properties.getSaslJaasConfig());
        }
        if (properties.getSslTruststoreLocation() != null && !properties.getSslTruststoreLocation().isBlank()) {
            configs.putIfAbsent(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, properties.getSslTruststoreLocation());
        }
        if (properties.getSslTruststorePassword() != null && !properties.getSslTruststorePassword().isBlank()) {
            configs.putIfAbsent(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, properties.getSslTruststorePassword());
        }
        if (properties.getSslKeystoreLocation() != null && !properties.getSslKeystoreLocation().isBlank()) {
            configs.putIfAbsent(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, properties.getSslKeystoreLocation());
        }
        if (properties.getSslKeystorePassword() != null && !properties.getSslKeystorePassword().isBlank()) {
            configs.putIfAbsent(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, properties.getSslKeystorePassword());
        }
    }

    private Class<?> loadClass(String className) {
        if (className == null || className.isBlank()) {
            return null;
        }
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }
}
