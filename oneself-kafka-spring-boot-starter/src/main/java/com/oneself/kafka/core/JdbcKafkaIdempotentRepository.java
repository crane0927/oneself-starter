package com.oneself.kafka.core;

import java.time.Instant;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * JDBC 幂等记录仓储（基于唯一键约束）。
 */
public class JdbcKafkaIdempotentRepository implements KafkaIdempotentRepository {

    private final JdbcTemplate jdbcTemplate;
    private final String tableName;

    public JdbcKafkaIdempotentRepository(JdbcTemplate jdbcTemplate, String tableName) {
        this.jdbcTemplate = jdbcTemplate;
        this.tableName = tableName;
    }

    @Override
    public boolean tryInsert(String eventId, String groupId, String topic, int partition, long offset) {
        String sql = "INSERT INTO " + tableName
                + " (event_id, consumer_group, topic, partition_id, offset_id, status, created_at, updated_at)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            jdbcTemplate.update(sql, eventId, groupId, topic, partition, offset,
                    "PROCESSING", Instant.now(), Instant.now());
            return true;
        } catch (DuplicateKeyException ex) {
            return false;
        }
    }

    @Override
    public void markDone(String eventId) {
        String sql = "UPDATE " + tableName + " SET status = ?, updated_at = ? WHERE event_id = ?";
        jdbcTemplate.update(sql, "DONE", Instant.now(), eventId);
    }

    @Override
    public void markFailed(String eventId, String errorMessage) {
        String sql = "UPDATE " + tableName + " SET status = ?, error_msg = ?, updated_at = ? WHERE event_id = ?";
        jdbcTemplate.update(sql, "FAILED", errorMessage, Instant.now(), eventId);
    }
}
