# Oneself Redis Spring Boot Starter

## 模块说明
面向企业场景的 Redis Starter，提供统一约定与轻量封装，包含：
- 自动配置 `RedisTemplate`（Key/HashKey 使用 String 序列化，Value/HashValue 使用 JSON 序列化）。
- 轻量 `RedisOps` 封装，简化常用 get/set/delete 操作。
- 可配置 key 前缀，统一缓存与数据键名规范。
- 连接信息与连接池等参数沿用 Spring Boot `spring.redis.*` 配置。

## 安装方式
在应用中引入依赖：

```xml
<dependency>
  <groupId>com.oneself</groupId>
  <artifactId>oneself-redis-spring-boot-starter</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```

## 配置示例
`application.yml`:

```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
      database: 0
      password: ""
oneself:
  redis:
    enabled: true
    key-prefix: order
    validate-key-pattern: false
    key-pattern: "^[a-zA-Z0-9:_-]+$"
    metrics-enabled: false
    logging-enabled: false
    cache-enabled: false
    cache-ttl: 30m
    cache-key-prefix: "cache:"
```

## 配置字段说明
| 配置项 | 类型 | 默认值 | 说明 |
| --- | --- | --- | --- |
| `oneself.redis.enabled` | boolean | `true` | 是否启用 Starter。 |
| `oneself.redis.key-prefix` | string | `""` | key 前缀，建议用于环境/租户隔离。 |
| `oneself.redis.validate-key-pattern` | boolean | `false` | 是否校验 key 规则。 |
| `oneself.redis.key-pattern` | string | `^[a-zA-Z0-9:_-]+$` | key 正则（包含前缀后的完整 key）。 |
| `oneself.redis.metrics-enabled` | boolean | `false` | 是否启用指标采集（需要 Micrometer）。 |
| `oneself.redis.logging-enabled` | boolean | `false` | 是否输出操作日志。 |
| `oneself.redis.cache-enabled` | boolean | `false` | 是否启用 Spring Cache 集成。 |
| `oneself.redis.cache-ttl` | duration | `30m` | 缓存默认 TTL。 |
| `oneself.redis.cache-key-prefix` | string | `""` | 缓存名称前缀。 |

## 常见组合示例

单机模式：
```yaml
spring:
  data:
    redis:
      host: 127.0.0.1
      port: 6379
      database: 0
      password: ""
oneself:
  redis:
    enabled: true
    key-prefix: order
```

集群模式：
```yaml
spring:
  data:
    redis:
      cluster:
        nodes:
          - "10.0.0.11:6379"
          - "10.0.0.12:6379"
          - "10.0.0.13:6379"
      password: ""
```

哨兵模式：
```yaml
spring:
  data:
    redis:
      sentinel:
        master: "mymaster"
        nodes:
          - "10.0.0.21:26379"
          - "10.0.0.22:26379"
      password: ""
```

启用 SSL：
```yaml
spring:
  data:
    redis:
      ssl: true
```

启用连接池：
```yaml
spring:
  data:
    redis:
      lettuce:
        pool:
          enabled: true
          max-active: 16
          max-idle: 8
          min-idle: 2
          max-wait: 2s
```

## 缓存示例（@Cacheable / @CachePut / @CacheEvict）
在业务服务中使用 Spring Cache 注解：

```java
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

    @Cacheable(cacheNames = "user:profile", key = "#userId")
    public UserProfile loadProfile(String userId) {
        return queryFromDatabase(userId);
    }

    @CachePut(cacheNames = "user:profile", key = "#userId")
    public UserProfile refreshProfile(String userId) {
        return queryFromDatabase(userId);
    }

    @CacheEvict(cacheNames = "user:profile", key = "#userId")
    public void updateProfile(String userId, UserProfile payload) {
        updateDatabase(userId, payload);
    }

    private UserProfile queryFromDatabase(String userId) {
        return new UserProfile(userId);
    }

    private void updateDatabase(String userId, UserProfile payload) {
        // 更新数据库
    }
}
```

条件缓存与同步锁示例：
```java
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Cacheable(cacheNames = "product:detail",
            key = "#sku",
            condition = "#sku != null",
            unless = "#result == null",
            sync = true)
    public ProductDetail loadDetail(String sku) {
        return queryFromDatabase(sku);
    }

    private ProductDetail queryFromDatabase(String sku) {
        return new ProductDetail(sku);
    }
}
```

## 使用方式
注入 `RedisOps` 进行简单读写：

```java
import java.time.Duration;
import java.util.Optional;

import com.oneself.redis.core.RedisOps;
import org.springframework.stereotype.Service;

@Service
public class OrderCacheService {
    private final RedisOps redisOps;

    public OrderCacheService(RedisOps redisOps) {
        this.redisOps = redisOps;
    }

    public void save(String orderId, Object payload) {
        redisOps.set(orderId, payload, Duration.ofMinutes(30));
    }

    public Optional<Object> find(String orderId) {
        return redisOps.get(orderId);
    }

    public void delete(String orderId) {
        redisOps.delete(orderId);
    }
}
```

## 注意事项
- Starter 会自动提供名为 `oneselfRedisTemplate` 的 `RedisTemplate` Bean。
- 如需定制序列化策略，可自行提供同名 `RedisTemplate` 覆盖默认配置。
- 可通过 `oneself.redis.enabled=false` 关闭自动装配。
- 可通过 `metrics-enabled` 与 `logging-enabled` 控制指标与日志埋点。
- 启用 `cache-enabled` 后会自动配置 `CacheManager`（RedisCacheManager）。
- Redis 连接与连接池等配置请使用 Spring Boot 的 `spring.data.redis.*` 系列配置。

## 企业级增强点（建议）
- 统一 key 规范：建议使用 `业务域:子域:标识`，配合 `key-prefix` 做环境/租户隔离。
- key 校验：在 `RedisOps` 外层增加校验，避免超长 key 或不符合规范的 key 写入。
- 观测能力：对 `set/get/delete` 增加指标与日志（如耗时、命中率、异常统计）。
- 容错策略：为关键读操作提供降级逻辑（如本地缓存兜底或返回默认值）。

## 序列化策略说明
- 默认使用 JSON 序列化（`RedisSerializer.json()`），适合大多数业务对象。
- 建议为缓存对象定义稳定的 DTO，避免直接存储复杂实体导致升级兼容问题。
- 若需要自定义 ObjectMapper（如日期格式/多态类型），请自行注册 `oneselfRedisTemplate` 并设置对应序列化器。
