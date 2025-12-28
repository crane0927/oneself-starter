# Oneself Swagger Spring Boot Starter

## 模块说明
统一封装 springdoc-openapi，提供常用 OpenAPI 信息配置与分组能力，开箱即用的 Swagger UI：
- 自动注册 `OpenAPI` 基础信息（title/description/version/contact/license）。
- 支持通过配置指定扫描包与路径匹配规则，生成分组文档。
- 可通过开关快速启停。

## 安装方式
在应用中引入依赖：

```xml
<dependency>
  <groupId>com.oneself</groupId>
  <artifactId>oneself-swagger-spring-boot-starter</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```

## 配置示例
`application.yml`:

```yaml
oneself:
  swagger:
    enabled: true
    title: "Oneself Service API"
    description: "Unified APIs for Oneself services"
    version: "v1"
    terms-of-service: "https://example.com/terms"
    contact:
      name: "API Team"
      url: "https://example.com"
      email: "api@example.com"
    license:
      name: "Apache 2.0"
      url: "https://www.apache.org/licenses/LICENSE-2.0"
    group-enabled: true
    group-name: "default"
    base-packages:
      - "com.oneself"
    paths-to-match:
      - "/api/**"
    paths-to-exclude:
      - "/internal/**"
```

## 配置字段说明
| 配置项 | 类型 | 默认值 | 说明 |
| --- | --- | --- | --- |
| `oneself.swagger.enabled` | boolean | `true` | 是否启用 Starter。 |
| `oneself.swagger.group-enabled` | boolean | `true` | 是否启用分组配置。 |
| `oneself.swagger.title` | string | `Oneself API` | OpenAPI 标题。 |
| `oneself.swagger.description` | string | `""` | OpenAPI 描述。 |
| `oneself.swagger.version` | string | `v1` | OpenAPI 版本。 |
| `oneself.swagger.terms-of-service` | string | `""` | 服务条款链接。 |
| `oneself.swagger.group-name` | string | `default` | 分组名称。 |
| `oneself.swagger.base-packages` | list | `[]` | 扫描的包路径。 |
| `oneself.swagger.paths-to-match` | list | `[]` | 匹配的路径模式。 |
| `oneself.swagger.paths-to-exclude` | list | `[]` | 排除的路径模式。 |
| `oneself.swagger.contact.name` | string | `""` | 联系人名称。 |
| `oneself.swagger.contact.url` | string | `""` | 联系人 URL。 |
| `oneself.swagger.contact.email` | string | `""` | 联系人邮箱。 |
| `oneself.swagger.license.name` | string | `""` | License 名称。 |
| `oneself.swagger.license.url` | string | `""` | License URL。 |

## 使用方式
启动应用后访问：
- Swagger UI: `/swagger-ui/index.html`
- OpenAPI JSON: `/v3/api-docs`

## 注意事项
- 本模块依赖 springdoc-openapi-starter-webmvc-ui，需要 WebMVC 环境。
- 若应用中已有自定义 `OpenAPI` 或 `GroupedOpenApi` Bean，将优先使用自定义实现。
- 可通过 `oneself.swagger.enabled=false` 关闭自动装配。
