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
    uris: ["http://127.0.0.1:9200"]
    username: ""
    password: ""
    connect-timeout: 2s
    socket-timeout: 30s
    index-prefix: "order"
```

## 配置字段说明
| 配置项 | 类型 | 默认值 | 说明 |
| --- | --- | --- | --- |
| `oneself.elasticsearch.enabled` | boolean | `true` | 是否启用 Starter。 |
| `oneself.elasticsearch.uris` | list | `[http://localhost:9200]` | 节点地址列表。 |
| `oneself.elasticsearch.username` | string | `""` | 用户名。 |
| `oneself.elasticsearch.password` | string | `""` | 密码。 |
| `oneself.elasticsearch.connect-timeout` | duration | `2s` | 连接超时。 |
| `oneself.elasticsearch.socket-timeout` | duration | `30s` | 读超时。 |
| `oneself.elasticsearch.index-prefix` | string | `""` | 索引前缀。 |

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
