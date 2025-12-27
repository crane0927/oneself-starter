# Oneself Redis Spring Boot Starter

## 模块说明
面向企业场景的 Redis Starter，提供统一约定与轻量封装，包含：
- 自动配置 `RedisTemplate`（Key/HashKey 使用 String 序列化，Value/HashValue 使用 JSON 序列化）。
- 轻量 `RedisOps` 封装，简化常用 get/set/delete 操作。
- 可配置 key 前缀，统一缓存与数据键名规范。

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
    mode: SINGLE
    host: localhost
    port: 6379
    database: 0
    password: ""
    nodes: ["127.0.0.1:6379", "127.0.0.1:6380"]
    sentinel-master: "mymaster"
    sentinel-nodes: ["127.0.0.1:26379", "127.0.0.1:26380"]
    sentinel-password: ""
    ssl-enabled: false
    ssl-verify-peer: true
    ssl-truststore: "/path/to/truststore.jks"
    ssl-truststore-password: ""
    ssl-keystore: "/path/to/keystore.jks"
    ssl-keystore-password: ""
    timeout: 2s
    connect-timeout: 2s
    pool-enabled: false
    pool-max-active: 8
    pool-max-idle: 8
    pool-min-idle: 0
    pool-max-wait: 1s
    client-name: "order-service"
    read-from: "MASTER"
    auto-reconnect: true
    disconnected-behavior: "DEFAULT"
    ping-before-activate-connection: false
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
- 单机模式使用 `host/port/database`；集群模式使用 `nodes`，并可通过 `password` 配置密码。
- 哨兵模式使用 `sentinel-master` 与 `sentinel-nodes`。
- 可通过 `sentinel-password` 设置哨兵认证密码，`password` 为 Redis 数据节点密码。
- SSL 相关参数用于启用 TLS 连接及证书校验。

## 企业级增强点（建议）
- 统一 key 规范：建议使用 `业务域:子域:标识`，配合 `key-prefix` 做环境/租户隔离。
- key 校验：在 `RedisOps` 外层增加校验，避免超长 key 或不符合规范的 key 写入。
- 观测能力：对 `set/get/delete` 增加指标与日志（如耗时、命中率、异常统计）。
- 容错策略：为关键读操作提供降级逻辑（如本地缓存兜底或返回默认值）。

## 序列化策略说明
- 默认使用 JSON 序列化（`RedisSerializer.json()`），适合大多数业务对象。
- 建议为缓存对象定义稳定的 DTO，避免直接存储复杂实体导致升级兼容问题。
- 若需要自定义 ObjectMapper（如日期格式/多态类型），请自行注册 `oneselfRedisTemplate` 并设置对应序列化器。
