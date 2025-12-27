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
    @KafkaOrderKey(\"orderId\")
    private final String orderId;

    public OrderCreatedEvent(String orderId) {
        this.orderId = orderId;
    }
}
```

未配置 key 且为顺序 topic 时会在运行时抛错。

### 重试与死信
- 本地重试：`local-retry-attempts` + `local-retry-backoff`。
- 失败后转发到重试 topic，命名规则：`{topic}.retry.{delay}`。
- 重试次数超限后转发到 DLQ：`{topic}.{dlq-suffix}`。
- 统一 header：`x-event-id`、`x-trace-id`、`x-retry-count`、`x-original-topic`、`x-original-partition`、`x-original-offset`、`x-failure-class`、`x-failure-message`。

### 幂等消费（Redis / DB）
Starter 提供 `KafkaIdempotentExecutor`，可选 Redis 或 DB 实现：
- Redis：`idempotent-store=REDIS`，基于 `SETNX` + 两阶段状态。
- DB：`idempotent-store=DB`，基于唯一键插入与状态更新。

示例用法：
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

### 推荐幂等表结构（DB）
```
kafka_idempotent_record
  event_id (PK/Unique)
  consumer_group
  topic
  partition_id
  offset_id
  status (PROCESSING/DONE/FAILED)
  created_at
  updated_at
  error_msg
```

### Outbox
Starter 提供 `KafkaOutboxEvent` 与 `KafkaOutboxPublisher` 接口，业务侧可基于本地事务落库并异步发布。

## 注意事项
- 该 Starter 仅统一基础生产/消费逻辑，连接参数请使用 Spring Boot Kafka 原生配置。
- 幂等生产者默认开启（可通过 `oneself.kafka.idempotence-enabled=false` 关闭）。
- Envelope 以 JSON 发送，建议配置 `spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer`。
- 消费侧建议使用 `JsonDeserializer` 并配置 `spring.kafka.consumer.properties.spring.json.trusted.packages`。
- 若需全局错误处理、重试、并发等策略，可在应用中配置 `KafkaListenerContainerFactory`。
