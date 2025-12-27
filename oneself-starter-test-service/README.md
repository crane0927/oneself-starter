# Oneself Starter Test Service

A small Spring Boot service for verifying the Oneself starter modules.

## Run

```bash
../mvnw -pl oneself-starter-test-service -am spring-boot:run
```

## Endpoints

- `GET /plugins/status`
- `POST /plugins/redis/{key}?value=hello`
- `GET /plugins/redis/{key}`
- `POST /plugins/kafka/publish?message=hello`
- `GET /plugins/elasticsearch/index?raw=orders`
- `GET /plugins/logging/demo?password=secret&idCard=110101199001011234`

## Notes

- Requires local Redis/Kafka/Elasticsearch if you want to fully validate connectivity.
- The Kafka consumer listens on `oneself.test.events` when `oneself.kafka.consumer-enabled=true`.
