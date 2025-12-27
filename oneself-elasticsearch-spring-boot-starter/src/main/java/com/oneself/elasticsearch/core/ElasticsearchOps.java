package com.oneself.elasticsearch.core;

import com.oneself.elasticsearch.autoconfigure.OneselfElasticsearchProperties;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._helpers.bulk.BulkIngester;

/**
 * Elasticsearch 常用操作封装。
 */
public class ElasticsearchOps {

    private final ElasticsearchClient client;
    private final String indexPrefix;
    private final BulkIngester<Object> bulkIngester;

    public ElasticsearchOps(ElasticsearchClient client,
                            OneselfElasticsearchProperties properties,
                            BulkIngester<Object> bulkIngester) {
        this.client = client;
        this.indexPrefix = properties.getIndexPrefix() == null ? "" : properties.getIndexPrefix();
        this.bulkIngester = bulkIngester;
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

    public BulkIngester<Object> bulkIngester() {
        return bulkIngester;
    }

    public boolean hasBulkIngester() {
        return bulkIngester != null;
    }
}
