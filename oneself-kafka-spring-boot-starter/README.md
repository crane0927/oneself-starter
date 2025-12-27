# Oneself Kafka Spring Boot Starter

## 模块说明
面向企业场景的 Kafka Starter，统一生产者与消费者的基础逻辑，包含：
- `KafkaOps`：统一生产者发送接口，强制 Envelope（JSON）并支持默认主题与自定义 header。
- `KafkaConsumerAdapter`：统一消费者消息适配，适配 Envelope 并输出标准消息模型。
- `KafkaMessage`：统一消息模型，包含 topic、key、payload、headers、timestamp。
- `KafkaEnvelope`：统一事件封装（JSON），包含 id/type/traceId/schemaVersion/payload/headers/timestamp。

## 安装方式
在应用中引入依赖：

```xml
<dependency>
  <groupId>com.oneself</groupId>
  <artifactId>oneself-kafka-spring-boot-starter</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```

## 配置示例
`application.yml`:

```yaml
oneself:
  kafka:
    enabled: true
    producer-enabled: true
    consumer-enabled: true
    idempotence-enabled: true
    default-topic: "order.events"
    bootstrap-servers: "127.0.0.1:9092"
    security-protocol: "PLAINTEXT"
    sasl-mechanism: ""
    sasl-jaas-config: ""
    ssl-truststore-location: ""
    ssl-truststore-password: ""
    ssl-keystore-location: ""
    ssl-keystore-password: ""
    schema-version: "v1"
    ordered-topics: ["order.events"]
    unordered-key-strategy: "NONE"
    consumer-group-id: "order-service"
    producer-client-id: "order-service"
    outbox-enabled: false
    retry-enabled: true
    retry-topic-delays: ["1m", "10m", "1h"]
    dlq-suffix: "dlq"
    local-retry-attempts: 2
    local-retry-backoff: 1s
    non-retryable-exceptions: ["com.oneself.common.NonRetryableException"]
    idempotent-store: "NONE"
    idempotent-key-prefix: "kafka:idem:"
    idempotent-processing-ttl: 10m
    idempotent-done-ttl: 7d
    idempotent-table-name: "kafka_idempotent_record"
    producer-acks: "all"
    producer-delivery-timeout: 120s
    producer-request-timeout: 30s
    producer-linger: 10ms
    producer-batch-size: 32768
    producer-compression-type: "lz4"
    consumer-enable-auto-commit: false
    consumer-auto-offset-reset: "latest"
    consumer-max-poll-records: 300
    consumer-max-poll-interval: 5m
    consumer-session-timeout: 15s
    consumer-heartbeat-interval: 5s
    consumer-fetch-max-bytes: 52428800
    consumer-isolation-level: "read_committed"
    listener-ack-mode: "MANUAL"
    listener-concurrency: 4
```

## 配置字段说明
| 配置项 | 类型 | 默认值 | 说明 |
| --- | --- | --- | --- |
| `oneself.kafka.enabled` | boolean | `true` | 是否启用 Starter。 |
| `oneself.kafka.default-topic` | string | `""` | 默认发送主题。 |
| `oneself.kafka.bootstrap-servers` | string | `""` | Kafka 地址（bootstrap.servers）。 |
| `oneself.kafka.security-protocol` | string | `""` | 安全协议（security.protocol）。 |
| `oneself.kafka.sasl-mechanism` | string | `""` | SASL 机制（sasl.mechanism）。 |
| `oneself.kafka.sasl-jaas-config` | string | `""` | SASL JAAS 配置。 |
| `oneself.kafka.ssl-truststore-location` | string | `""` | SSL truststore 路径。 |
| `oneself.kafka.ssl-truststore-password` | string | `""` | SSL truststore 密码。 |
| `oneself.kafka.ssl-keystore-location` | string | `""` | SSL keystore 路径。 |
| `oneself.kafka.ssl-keystore-password` | string | `""` | SSL keystore 密码。 |
| `oneself.kafka.schema-version` | string | `v1` | 事件 schema 版本。 |
| `oneself.kafka.ordered-topics` | list | `[]` | 需要顺序的 topic 列表。 |
| `oneself.kafka.unordered-key-strategy` | enum | `NONE` | 非顺序 topic key 策略：`NONE`/`RANDOM`。 |
| `oneself.kafka.retry-enabled` | boolean | `true` | 是否启用重试/死信。 |
| `oneself.kafka.retry-topic-delays` | list | `[1m,10m,1h]` | 重试 topic 延迟级别。 |
| `oneself.kafka.dlq-suffix` | string | `dlq` | DLQ 后缀。 |
| `oneself.kafka.local-retry-attempts` | int | `2` | 本地重试次数。 |
| `oneself.kafka.local-retry-backoff` | duration | `1s` | 本地重试间隔。 |
| `oneself.kafka.non-retryable-exceptions` | list | `[]` | 不重试异常类名。 |
| `oneself.kafka.idempotent-store` | enum | `NONE` | 幂等存储：`NONE`/`REDIS`/`DB`。 |
| `oneself.kafka.idempotent-key-prefix` | string | `kafka:idem:` | 幂等 key 前缀。 |
| `oneself.kafka.idempotent-processing-ttl` | duration | `10m` | PROCESSING TTL。 |
| `oneself.kafka.idempotent-done-ttl` | duration | `7d` | DONE TTL。 |
| `oneself.kafka.idempotent-table-name` | string | `kafka_idempotent_record` | 幂等表名。 |
| `oneself.kafka.producer-acks` | string | `all` | 生产者 acks。 |
| `oneself.kafka.producer-delivery-timeout` | duration | `120s` | 发送总超时。 |
| `oneself.kafka.producer-request-timeout` | duration | `30s` | 单次请求超时。 |
| `oneself.kafka.producer-linger` | duration | `10ms` | 批量等待时间。 |
| `oneself.kafka.producer-batch-size` | int | `32768` | 批量大小。 |
| `oneself.kafka.producer-compression-type` | string | `lz4` | 压缩算法。 |
| `oneself.kafka.consumer-enable-auto-commit` | boolean | `false` | 是否自动提交 offset。 |
| `oneself.kafka.consumer-auto-offset-reset` | string | `latest` | offset 重置策略。 |
| `oneself.kafka.consumer-max-poll-records` | int | `300` | 单批拉取条数。 |
| `oneself.kafka.consumer-max-poll-interval` | duration | `5m` | 最大处理间隔。 |
| `oneself.kafka.consumer-session-timeout` | duration | `15s` | 会话超时。 |
| `oneself.kafka.consumer-heartbeat-interval` | duration | `5s` | 心跳间隔。 |
| `oneself.kafka.consumer-fetch-max-bytes` | int | `52428800` | 拉取最大字节数。 |
| `oneself.kafka.consumer-isolation-level` | string | `read_committed` | 隔离级别。 |
| `oneself.kafka.listener-ack-mode` | string | `MANUAL` | Ack 模式。 |
| `oneself.kafka.listener-concurrency` | int | `null` | 监听并发数。 |

## 常见组合示例

基础连接：
```yaml
oneself:
  kafka:
    enabled: true
    bootstrap-servers: "10.0.0.1:9092,10.0.0.2:9092"
```

启用顺序 topic：
```yaml
oneself:
  kafka:
    ordered-topics: ["order.events"]
```

启用重试与 DLQ：
```yaml
oneself:
  kafka:
    retry-enabled: true
    retry-topic-delays: ["1m", "10m", "1h"]
    dlq-suffix: "dlq"
    local-retry-attempts: 2
    local-retry-backoff: 1s
```

启用 Redis 幂等：
```yaml
oneself:
  kafka:
    idempotent-store: "REDIS"
    idempotent-key-prefix: "kafka:idem:"
    idempotent-processing-ttl: 10m
    idempotent-done-ttl: 7d
```

启用 DB 幂等：
```yaml
oneself:
  kafka:
    idempotent-store: "DB"
    idempotent-table-name: "kafka_idempotent_record"
```

## 使用方式

### 生产者发送

```java
import com.oneself.kafka.core.KafkaOps;
import org.springframework.stereotype.Service;

@Service
public class OrderEventPublisher {
    private final KafkaOps kafkaOps;

    public OrderEventPublisher(KafkaOps kafkaOps) {
        this.kafkaOps = kafkaOps;
    }

    public void publish(Object payload) {
        kafkaOps.sendDefault(payload);
    }
}
```

### 消费者适配

```java
import com.oneself.kafka.core.KafkaConsumerAdapter;
import com.oneself.kafka.core.KafkaEnvelope;
import com.oneself.kafka.core.KafkaMessage;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    private final KafkaConsumerAdapter adapter;

    public OrderEventListener(KafkaConsumerAdapter adapter) {
        this.adapter = adapter;
    }

    @KafkaListener(topics = "order.events", groupId = "order-service")
    public void onMessage(ConsumerRecord<String, KafkaEnvelope<String>> record) {
        KafkaMessage<String> message = adapter.adapt(record);
        // 统一处理逻辑
    }
}
```

### 顺序策略与 @KafkaOrderKey
若 topic 需要顺序性，必须提供 key。可以显式传 key，或在 payload 字段上标注 `@KafkaOrderKey`：

```java
import com.oneself.kafka.core.KafkaOrderKey;

public class OrderCreatedEvent {
    @KafkaOrderKey("orderId")
    private final String orderId;

    public OrderCreatedEvent(String orderId) {
        this.orderId = orderId;
    }
}
```

未配置 key 且为顺序 topic 时会在运行时抛错。

### 幂等消费（Redis / DB）
Starter 提供 `KafkaIdempotentExecutor`，可选 Redis 或 DB 实现。

```java
import com.oneself.kafka.core.KafkaIdempotentExecutor;

public class OrderHandler {
    private final KafkaIdempotentExecutor executor;

    public OrderHandler(KafkaIdempotentExecutor executor) {
        this.executor = executor;
    }

    public void handle(String eventId, Runnable action) {
        executor.execute(eventId, () -> {
            action.run();
            return null;
        }, () -> null);
    }
}
```

### Outbox
Starter 提供 `KafkaOutboxEvent` 与 `KafkaOutboxPublisher` 接口，业务侧可基于本地事务落库并异步发布。

## 注意事项
- 该 Starter 仅统一基础生产/消费逻辑；连接参数可用 `oneself.kafka.*` 配置，也可继续使用 Spring Boot Kafka 原生配置。
- 幂等生产者默认开启（可通过 `oneself.kafka.idempotence-enabled=false` 关闭）。
- Envelope 以 JSON 发送，建议配置 `spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer`。
- 消费侧建议使用 `JsonDeserializer` 并配置 `spring.kafka.consumer.properties.spring.json.trusted.packages`。
- 若需全局错误处理、重试、并发等策略，可在应用中配置 `KafkaListenerContainerFactory`。

## 企业级增强点（建议）
- 统一 header 规范：eventId/traceId/retry 信息，便于排障与回溯。
- 统一异常分类：可重试/不可重试异常分层配置。
- 统一重试与 DLQ 主题命名，支持回溯重放。
- 观测能力：生产/消费耗时、失败率、重试与 DLQ 指标。
