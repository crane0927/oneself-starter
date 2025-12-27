package com.oneself.kafka.autoconfigure;

import com.oneself.kafka.core.DbKafkaIdempotentExecutor;
import com.oneself.kafka.core.JdbcKafkaIdempotentRepository;
import com.oneself.kafka.core.KafkaIdempotentExecutor;
import com.oneself.kafka.core.KafkaIdempotentRepository;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@AutoConfiguration
@ConditionalOnClass(name = "org.springframework.jdbc.core.JdbcTemplate")
public class OneselfKafkaJdbcAutoConfiguration {

    /**
     * DB 幂等仓储。
     */
    @Bean
    @ConditionalOnMissingBean(KafkaIdempotentRepository.class)
    @ConditionalOnProperty(prefix = "oneself.kafka", name = "idempotent-store", havingValue = "DB")
    public KafkaIdempotentRepository jdbcKafkaIdempotentRepository(JdbcTemplate jdbcTemplate,
                                                                    OneselfKafkaProperties properties) {
        return new JdbcKafkaIdempotentRepository(jdbcTemplate, properties.getIdempotentTableName());
    }

    /**
     * DB 幂等执行器。
     */
    @Bean
    @ConditionalOnMissingBean(KafkaIdempotentExecutor.class)
    @ConditionalOnProperty(prefix = "oneself.kafka", name = "idempotent-store", havingValue = "DB")
    public KafkaIdempotentExecutor dbKafkaIdempotentExecutor(KafkaIdempotentRepository repository,
                                                             OneselfKafkaProperties properties) {
        String groupId = properties.getConsumerGroupId() == null ? "unknown" : properties.getConsumerGroupId();
        return new DbKafkaIdempotentExecutor(repository, groupId);
    }
}
