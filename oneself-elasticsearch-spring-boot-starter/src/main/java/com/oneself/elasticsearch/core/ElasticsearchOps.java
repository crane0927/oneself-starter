package com.oneself.elasticsearch.core;

import com.oneself.elasticsearch.autoconfigure.OneselfElasticsearchProperties;
import co.elastic.clients.elasticsearch.ElasticsearchClient;

/**
 * Elasticsearch 常用操作封装。
 */
public class ElasticsearchOps {

    private final ElasticsearchClient client;
    private final String indexPrefix;

    public ElasticsearchOps(ElasticsearchClient client, OneselfElasticsearchProperties properties) {
        this.client = client;
        this.indexPrefix = properties.getIndexPrefix() == null ? "" : properties.getIndexPrefix();
    }

    public ElasticsearchClient client() {
        return client;
    }

    public String indexName(String raw) {
        if (indexPrefix.isEmpty()) {
            return raw;
        }
        return indexPrefix + "_" + raw;
    }
}
