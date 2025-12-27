# Oneself Elasticsearch Spring Boot Starter

## 模块说明
面向企业场景的 Elasticsearch Starter，提供统一连接配置与轻量封装，包含：
- 自动构建 `RestClient` / `ElasticsearchClient`。
- `ElasticsearchOps` 提供索引前缀与客户端访问。

## 安装方式
在应用中引入依赖：

```xml
<dependency>
  <groupId>com.oneself</groupId>
  <artifactId>oneself-elasticsearch-spring-boot-starter</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```

## 配置示例
`application.yml`:

```yaml
oneself:
  elasticsearch:
    enabled: true
    uris: "127.0.0.1:9200,127.0.0.2:9200"
    scheme: "http"
    username: ""
    password: ""
    connect-timeout: 2s
    socket-timeout: 30s
    index-prefix: "order"
    bulk-enabled: true
    bulk-max-operations: 1000
    bulk-max-bytes: 5242880
    bulk-flush-interval: 1s
    bulk-concurrent-requests: 1
```

## 配置字段说明
| 配置项 | 类型 | 默认值 | 说明 |
| --- | --- | --- | --- |
| `oneself.elasticsearch.enabled` | boolean | `true` | 是否启用 Starter。 |
| `oneself.elasticsearch.uris` | string | `127.0.0.1:9200` | 节点地址列表（逗号分隔）。 |
| `oneself.elasticsearch.scheme` | string | `http` | 协议（http/https）。 |
| `oneself.elasticsearch.username` | string | `""` | 用户名。 |
| `oneself.elasticsearch.password` | string | `""` | 密码。 |
| `oneself.elasticsearch.connect-timeout` | duration | `2s` | 连接超时。 |
| `oneself.elasticsearch.socket-timeout` | duration | `30s` | 读超时。 |
| `oneself.elasticsearch.index-prefix` | string | `""` | 索引前缀。 |
| `oneself.elasticsearch.bulk-enabled` | boolean | `true` | 是否启用 Bulk 写入器。 |
| `oneself.elasticsearch.bulk-max-operations` | int | `1000` | 单批最大操作数。 |
| `oneself.elasticsearch.bulk-max-bytes` | long | `5242880` | 单批最大字节数。 |
| `oneself.elasticsearch.bulk-flush-interval` | duration | `1s` | 批量刷新间隔。 |
| `oneself.elasticsearch.bulk-concurrent-requests` | int | `1` | 并发请求数。 |

## 使用方式

```java
import com.oneself.elasticsearch.core.ElasticsearchOps;
import org.springframework.stereotype.Service;

@Service
public class OrderSearchService {
    private final ElasticsearchOps ops;

    public OrderSearchService(ElasticsearchOps ops) {
        this.ops = ops;
    }

    public String indexName(String raw) {
        return ops.indexName(raw);
    }
}
```

## 注意事项
- Starter 会优先使用应用侧已有的 `RestClient` / `ElasticsearchClient`。
- 如需自定义序列化或 HTTP 配置，可自行定义 `RestClient` 覆盖默认实现。
- `bulkIngester()` 仅在启用 `bulk-enabled` 时可用。

## 企业级最佳实践（摘要）
- 索引分片数量与单分片大小需要容量评估后确定，避免过多小分片。
- 写入优先使用 Bulk（5~15MB/批）并结合 refresh 策略控制延迟与吞吐。
- 业务索引建议使用别名+版本化重建（reindex）流程。
- 日志类建议使用 ILM + rollover。
- 查询优先 filter，深分页使用 search_after。
