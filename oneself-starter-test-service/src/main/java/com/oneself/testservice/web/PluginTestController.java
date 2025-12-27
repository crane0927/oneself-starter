package com.oneself.testservice.web;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.oneself.elasticsearch.core.ElasticsearchOps;
//import com.oneself.kafka.core.KafkaOps;
import com.oneself.logging.core.LogEncrypt;
import com.oneself.redis.core.RedisOps;
//import com.oneself.testservice.kafka.TestEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/plugins")
public class PluginTestController {

    private static final Logger log = LoggerFactory.getLogger(PluginTestController.class);

    private final ObjectProvider<RedisOps> redisOpsProvider;
//    private final ObjectProvider<KafkaOps> kafkaOpsProvider;
    private final ObjectProvider<ElasticsearchOps> elasticsearchOpsProvider;

    public PluginTestController(ObjectProvider<RedisOps> redisOpsProvider,
//                                ObjectProvider<KafkaOps> kafkaOpsProvider,
                                ObjectProvider<ElasticsearchOps> elasticsearchOpsProvider) {
        this.redisOpsProvider = redisOpsProvider;
//        this.kafkaOpsProvider = kafkaOpsProvider;
        this.elasticsearchOpsProvider = elasticsearchOpsProvider;
    }

    @GetMapping("/status")
    public Map<String, Object> status() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("redis", redisOpsProvider.getIfAvailable() != null);
//        result.put("kafka", kafkaOpsProvider.getIfAvailable() != null);
        result.put("elasticsearch", elasticsearchOpsProvider.getIfAvailable() != null);
        return result;
    }

    @PostMapping("/redis/{key}")
    public Map<String, Object> redisSet(@PathVariable String key, @RequestParam String value) {
        RedisOps ops = requireBean(redisOpsProvider, "RedisOps");
        ops.set(key, value, Duration.ofMinutes(5));
        return Map.of("key", key, "value", value);
    }

    @GetMapping("/redis/{key}")
    public Map<String, Object> redisGet(@PathVariable String key) {
        RedisOps ops = requireBean(redisOpsProvider, "RedisOps");
        Optional<Object> value = ops.get(key);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("key", key);
        result.put("found", value.isPresent());
        result.put("value", value.orElse(null));
        return result;
    }

//    @PostMapping("/kafka/publish")
//    public Map<String, Object> kafkaPublish(@RequestParam String message) {
//        KafkaOps ops = requireBean(kafkaOpsProvider, "KafkaOps");
//        String orderId = UUID.randomUUID().toString();
//        TestEvent event = new TestEvent(orderId, message);
//        ops.sendDefault(event);
//        return Map.of("orderId", orderId, "message", message);
//    }

    @GetMapping("/elasticsearch/index")
    public Map<String, Object> elasticsearchIndex(@RequestParam String raw) {
        ElasticsearchOps ops = requireBean(elasticsearchOpsProvider, "ElasticsearchOps");
        return Map.of("raw", raw, "index", ops.indexName(raw));
    }

    @GetMapping("/logging/demo")
    public Map<String, Object> loggingDemo(@RequestParam String password, @RequestParam String idCard) {
        LoggingPayload payload = new LoggingPayload(password, idCard);
        log.info("logging demo payload: {}", payload);
        return Map.of("logged", true);
    }

    private <T> T requireBean(ObjectProvider<T> provider, String name) {
        T bean = provider.getIfAvailable();
        if (bean == null) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, name + " is not enabled");
        }
        return bean;
    }

    private static class LoggingPayload {

        @LogEncrypt(mode = LogEncrypt.Mode.MASK)
        private final String password;
        @LogEncrypt(mode = LogEncrypt.Mode.ENCRYPT, name = "idCard")
        private final String idCard;

        private LoggingPayload(String password, String idCard) {
            this.password = password;
            this.idCard = idCard;
        }

        public String getPassword() {
            return password;
        }

        public String getIdCard() {
            return idCard;
        }
    }
}
