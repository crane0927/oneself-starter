# Oneself Starter

企业级 Spring Boot Starter 集合，统一各基础组件的最佳实践与默认约定，降低重复配置与工程成本。

## Features
- 统一配置入口与自动装配
- 生产可用的默认配置（日志、Kafka、Redis、Elasticsearch）
- 清晰的模块分层，便于扩展新的 starter

## Modules
- `oneself-redis-spring-boot-starter`: Redis 连接、序列化、Key 规范、缓存与指标能力
- `oneself-kafka-spring-boot-starter`: Kafka 生产/消费封装、幂等、重试/DLQ、Envelope 规范
- `oneself-logging-spring-boot-starter`: Logback 统一配置、脱敏、访问日志、JSON 输出
- `oneself-elasticsearch-spring-boot-starter`: Elasticsearch 连接与 Bulk 支持

## Requirements
- Java 21
- Maven 3.9+

## Quick Start
在需要的应用中引入对应模块：

```xml
<dependency>
  <groupId>com.oneself</groupId>
  <artifactId>oneself-redis-spring-boot-starter</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```

## Configuration
每个模块均提供独立 README：
- `oneself-redis-spring-boot-starter/README.md`
- `oneself-kafka-spring-boot-starter/README.md`
- `oneself-logging-spring-boot-starter/README.md`
- `oneself-elasticsearch-spring-boot-starter/README.md`

## Build

```bash
./mvnw clean package
```

## Contributing
- 新增 starter 请遵循现有模块结构与命名规则
- 增强默认配置需给出清晰的 README 说明与配置字段

## License
内部项目，未开放许可。
