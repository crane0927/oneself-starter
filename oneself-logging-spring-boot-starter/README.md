# Oneself Logging Spring Boot Starter

## 模块说明
基于 Logback 的企业级日志 Starter，提供统一日志配置与最佳实践，包含：
- 开发环境控制台彩色输出。
- 文件输出，按天/大小滚动并自动压缩。
- 错误日志单独文件存储。
- 多环境配置（dev/test/prod）。
- 异步写入提升性能。
- 可选 JSON 格式日志输出。
- 敏感信息自动脱敏（密码、账号、身份证号、手机号）。
- TraceId/SpanId 透传（与 Micrometer Tracing 集成）。
- 访问日志（access.log），慢接口自动 WARN。

## 安装方式
在应用中引入依赖：

```xml
<dependency>
  <groupId>com.oneself</groupId>
  <artifactId>oneself-logging-spring-boot-starter</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```

## 配置示例
`application.yml`:

```yaml
spring:
  application:
    name: order-service
logging:
  file:
    path: /var/log/order-service
  level:
    root: INFO
oneself:
  logging:
    json-enabled: false
    encrypt-key: ""
    mask-patterns: ["(?i)token\\s*[:=]\\s*[^\\s,;]+", "\\\\b\\\\d{18}\\\\b"]
    mask-fields: ["password", "idCard", "mobile"]
    mask-fields-key: ""
    access-enabled: true
    access-slow-threshold-ms: 1000
    access-logger-name: "ACCESS"
```

## 配置字段说明
| 配置项 | 类型 | 默认值 | 说明 |
| --- | --- | --- | --- |
| `spring.application.name` | string | `application` | 应用名称，用于日志文件名。 |
| `logging.file.path` | string | `./logs` | 日志目录。 |
| `logging.level.root` | string | `INFO` | 根日志级别。 |
| `oneself.logging.json-enabled` | boolean | `false` | 是否启用 JSON 日志文件输出。 |
| `oneself.logging.encrypt-key` | string | `""` | 日志加密默认密钥。 |
| `oneself.logging.mask-patterns` | list | `[]` | 自定义脱敏正则（命中后替换为 ***）。 |
| `oneself.logging.mask-fields` | list | `[]` | 固定脱敏字段名（Map/JSON）。 |
| `oneself.logging.mask-fields-key` | string | `""` | 固定脱敏字段密钥（默认 ENCRYPT）。 |
| `oneself.logging.access-enabled` | boolean | `true` | 是否启用访问日志。 |
| `oneself.logging.access-slow-threshold-ms` | long | `1000` | 慢请求阈值（毫秒）。 |
| `oneself.logging.access-logger-name` | string | `ACCESS` | 访问日志 logger 名称。 |

## 常见组合示例

本地开发（彩色控制台）：
```yaml
spring:
  profiles:
    active: dev
logging:
  file:
    path: ./logs
  level:
    root: DEBUG
```

生产环境（仅文件输出）：
```yaml
spring:
  profiles:
    active: prod
logging:
  file:
    path: /var/log/app
  level:
    root: INFO
oneself:
  logging:
    json-enabled: true
```

## 使用方式
无额外代码改动，Starter 会提供默认 `logback-spring.xml`。
若应用侧需要自定义格式或追加 appender，可在业务工程中覆盖 `logback-spring.xml`。

### 字段加密注解
在需要脱敏/加密的字段上标注 `@LogEncrypt`：

```java
import com.oneself.logging.core.LogEncrypt;

public class LoginRequest {
    @LogEncrypt(mode = LogEncrypt.Mode.MASK)
    private String password;

    @LogEncrypt(mode = LogEncrypt.Mode.ENCRYPT, key = "your-key", name = "idCard")
    private String idCard;
}
```
未指定 key 时使用 `oneself.logging.encrypt-key` 作为默认密钥。
支持两种模式：`MASK`（直接替换为 ***）与 `ENCRYPT`（密钥加密）。
Map/JSON 字段会根据 `@LogEncrypt.name`（或字段名）以及 `oneself.logging.mask-fields` 进行脱敏/加密。

## 注意事项
- 默认日志文件位于 `${logging.file.path}/${spring.application.name}.log`。
- JSON 日志文件位于 `${logging.file.path}/${spring.application.name}.json.log`（启用后）。
- 错误日志单独输出到 `${logging.file.path}/${spring.application.name}.error.log`。
- 访问日志输出到 `${logging.file.path}/${spring.application.name}.access.log`。
- 日志默认异步写入，队列满时丢弃低优先级日志以保护主流程。
- TraceId/SpanId 从 MDC 读取，使用 Micrometer Tracing 时自动生效。
- JSON 日志同样应用脱敏规则。
- 带 `@LogEncrypt` 的字段会按配置进行掩码或密钥加密。
- `mask` 转换器按普通转换器使用，pattern 中使用 `%mask`（不要写 `%mask(%msg)`），避免被当作复合转换器解析。

## 企业级增强点（建议）
- 与 ELK/EFK 打通，统一采集与检索。
- 为关键模块配置独立 logger 和级别。
- 结合审计日志或安全日志，隔离敏感操作记录。
