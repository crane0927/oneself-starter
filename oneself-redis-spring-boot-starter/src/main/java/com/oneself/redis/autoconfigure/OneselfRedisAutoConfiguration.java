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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import io.micrometer.core.instrument.MeterRegistry;
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

    /**
     * 字符串判空。
     */
    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
