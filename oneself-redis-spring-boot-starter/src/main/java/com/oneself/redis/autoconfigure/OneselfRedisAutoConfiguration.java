package com.oneself.redis.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import io.lettuce.core.api.StatefulConnection;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.ReadFrom;
import io.lettuce.core.SocketOptions;
import io.lettuce.core.SslOptions;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import java.io.File;
import java.time.Duration;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import com.oneself.redis.core.RedisOps;

/**
 * Redis Starter 自动装配。
 */
@AutoConfiguration
@EnableConfigurationProperties(OneselfRedisProperties.class)
@ConditionalOnClass(RedisConnectionFactory.class)
@ConditionalOnProperty(prefix = "oneself.redis", name = "enabled", havingValue = "true", matchIfMissing = true)
public class OneselfRedisAutoConfiguration {

    /**
     * 默认 RedisTemplate，提供 JSON Value 与 String Key 序列化。
     */
    @Bean
    @ConditionalOnMissingBean(name = "oneselfRedisTemplate")
    public RedisTemplate<String, Object> oneselfRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        RedisSerializer<Object> serializer = RedisSerializer.json();
        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);
        template.afterPropertiesSet();
        return template;
    }

    /**
     * 根据 mode 构建 RedisConnectionFactory，支持单机/集群/哨兵。
     */
    @Bean
    @ConditionalOnMissingBean(RedisConnectionFactory.class)
    public RedisConnectionFactory redisConnectionFactory(OneselfRedisProperties properties) {
        LettuceClientConfiguration clientConfiguration = buildClientConfiguration(properties);
        if (properties.getMode() == OneselfRedisProperties.Mode.CLUSTER) {
            RedisClusterConfiguration cluster = new RedisClusterConfiguration();
            if (properties.getNodes() != null && properties.getNodes().length > 0) {
                for (String node : properties.getNodes()) {
                    RedisNode redisNode = parseRedisNode(node);
                    if (redisNode != null) {
                        cluster.addClusterNode(redisNode);
                    }
                }
            } else {
                throw new IllegalArgumentException("Redis cluster mode requires nodes");
            }
            if (hasText(properties.getPassword())) {
                cluster.setPassword(RedisPassword.of(properties.getPassword()));
            }
            return new LettuceConnectionFactory(cluster, clientConfiguration);
        }
        if (properties.getMode() == OneselfRedisProperties.Mode.SENTINEL) {
            RedisSentinelConfiguration sentinel = new RedisSentinelConfiguration();
            if (!hasText(properties.getSentinelMaster())) {
                throw new IllegalArgumentException("Redis sentinel mode requires sentinelMaster");
            }
            sentinel.master(properties.getSentinelMaster());
            if (properties.getSentinelNodes() != null && properties.getSentinelNodes().length > 0) {
                for (String node : properties.getSentinelNodes()) {
                    RedisNode redisNode = parseRedisNode(node);
                    if (redisNode != null) {
                        sentinel.addSentinel(redisNode);
                    }
                }
            } else {
                throw new IllegalArgumentException("Redis sentinel mode requires sentinelNodes");
            }
            sentinel.setDatabase(properties.getDatabase());
            if (hasText(properties.getPassword())) {
                sentinel.setPassword(RedisPassword.of(properties.getPassword()));
            }
            if (hasText(properties.getSentinelPassword())) {
                sentinel.setSentinelPassword(RedisPassword.of(properties.getSentinelPassword()));
            }
            return new LettuceConnectionFactory(sentinel, clientConfiguration);
        }
        RedisStandaloneConfiguration standalone = new RedisStandaloneConfiguration();
        standalone.setHostName(properties.getHost());
        standalone.setPort(properties.getPort());
        standalone.setDatabase(properties.getDatabase());
        if (hasText(properties.getPassword())) {
            standalone.setPassword(RedisPassword.of(properties.getPassword()));
        }
        return new LettuceConnectionFactory(standalone, clientConfiguration);
    }

    /**
     * 解析节点配置（host:port）。
     */
    private RedisNode parseRedisNode(String node) {
        if (node == null || node.isBlank()) {
            return null;
        }
        String[] parts = node.split(":", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid redis node, expected host:port but got: " + node);
        }
        String host = parts[0].trim();
        int port = Integer.parseInt(parts[1].trim());
        return new RedisNode(host, port);
    }

    /**
     * 构建 Lettuce 客户端配置（SSL、超时、连接池等）。
     */
    private LettuceClientConfiguration buildClientConfiguration(OneselfRedisProperties properties) {
        LettuceClientConfiguration.LettuceClientConfigurationBuilder builder;
        if (properties.isPoolEnabled()) {
            GenericObjectPoolConfig<StatefulConnection<?, ?>> poolConfig = new GenericObjectPoolConfig<>();
            poolConfig.setMaxTotal(properties.getPoolMaxActive());
            poolConfig.setMaxIdle(properties.getPoolMaxIdle());
            poolConfig.setMinIdle(properties.getPoolMinIdle());
            if (properties.getPoolMaxWait() != null) {
                poolConfig.setMaxWait(properties.getPoolMaxWait());
            }
            builder = LettucePoolingClientConfiguration.builder().poolConfig(poolConfig);
        } else {
            builder = LettuceClientConfiguration.builder();
        }
        if (hasText(properties.getClientName())) {
            builder.clientName(properties.getClientName());
        }
        ReadFrom readFrom = parseReadFrom(properties.getReadFrom());
        if (readFrom != null) {
            builder.readFrom(readFrom);
        }
        if (properties.isSslEnabled()) {
            builder.useSsl();
        }
        builder.commandTimeout(properties.getTimeout());
        builder.clientOptions(ClientOptions.builder()
                .autoReconnect(properties.isAutoReconnect())
                .disconnectedBehavior(parseDisconnectedBehavior(properties.getDisconnectedBehavior()))
                .pingBeforeActivateConnection(properties.isPingBeforeActivateConnection())
                .socketOptions(SocketOptions.builder().connectTimeout(properties.getConnectTimeout()).build())
                .sslOptions(buildSslOptions(properties))
                .build());
        return builder.build();
    }

    /**
     * 字符串判空。
     */
    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    /**
     * 解析 ReadFrom 配置。
     */
    private ReadFrom parseReadFrom(String value) {
        if (!hasText(value)) {
            return null;
        }
        String normalized = value.trim().toUpperCase();
        return switch (normalized) {
            case "MASTER" -> ReadFrom.MASTER;
            case "MASTER_PREFERRED" -> ReadFrom.MASTER_PREFERRED;
            case "REPLICA" -> ReadFrom.REPLICA;
            case "REPLICA_PREFERRED" -> ReadFrom.REPLICA_PREFERRED;
            case "ANY" -> ReadFrom.ANY;
            default -> throw new IllegalArgumentException("Unsupported readFrom: " + value);
        };
    }

    /**
     * 解析断连策略。
     */
    private ClientOptions.DisconnectedBehavior parseDisconnectedBehavior(String value) {
        if (!hasText(value)) {
            return ClientOptions.DisconnectedBehavior.DEFAULT;
        }
        String normalized = value.trim().toUpperCase();
        return switch (normalized) {
            case "DEFAULT" -> ClientOptions.DisconnectedBehavior.DEFAULT;
            case "REJECT_COMMANDS" -> ClientOptions.DisconnectedBehavior.REJECT_COMMANDS;
            case "ACCEPT_COMMANDS" -> ClientOptions.DisconnectedBehavior.ACCEPT_COMMANDS;
            default -> throw new IllegalArgumentException("Unsupported disconnectedBehavior: " + value);
        };
    }

    /**
     * 构建 SSL 配置。
     */
    private SslOptions buildSslOptions(OneselfRedisProperties properties) {
        if (!properties.isSslEnabled()) {
            return null;
        }
        SslOptions.Builder ssl = SslOptions.builder();
        if (hasText(properties.getSslTruststore())) {
            ssl.truststore(new File(properties.getSslTruststore()),
                    properties.getSslTruststorePassword());
        }
        if (!properties.isSslVerifyPeer()) {
            ssl.trustManager(InsecureTrustManagerFactory.INSTANCE);
        }
        if (hasText(properties.getSslKeystore())) {
            ssl.keystore(new File(properties.getSslKeystore()),
                    toCharArray(properties.getSslKeystorePassword()));
        }
        return ssl.build();
    }

    /**
     * 转为 char[]。
     */
    private char[] toCharArray(String value) {
        if (!hasText(value)) {
            return null;
        }
        return value.toCharArray();
    }

    /**
     * 业务侧 Redis 操作封装。
     */
    @Bean
    @ConditionalOnMissingBean
    public RedisOps redisOps(@Qualifier("oneselfRedisTemplate") RedisTemplate<String, Object> redisTemplate,
                             OneselfRedisProperties properties,
                             ObjectProvider<MeterRegistry> meterRegistry) {
        return new RedisOps(redisTemplate, properties, meterRegistry.getIfAvailable());
    }

    /**
     * Spring Cache 集成，使用 RedisCacheManager。
     */
    @Bean
    @ConditionalOnMissingBean(CacheManager.class)
    @ConditionalOnProperty(prefix = "oneself.redis", name = "cache-enabled", havingValue = "true")
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory, OneselfRedisProperties properties) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()));
        Duration ttl = properties.getCacheTtl();
        if (ttl != null && !ttl.isZero() && !ttl.isNegative()) {
            config = config.entryTtl(ttl);
        }
        if (hasText(properties.getCacheKeyPrefix())) {
            config = config.prefixCacheNameWith(properties.getCacheKeyPrefix());
        }
        return RedisCacheManager.builder(connectionFactory).cacheDefaults(config).build();
    }
}
