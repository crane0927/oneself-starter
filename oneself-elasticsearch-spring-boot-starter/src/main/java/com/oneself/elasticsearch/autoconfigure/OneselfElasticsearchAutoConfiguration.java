package com.oneself.elasticsearch.autoconfigure;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import co.elastic.clients.elasticsearch._helpers.bulk.BulkIngester;

import com.oneself.elasticsearch.core.ElasticsearchOps;

/**
 * Elasticsearch Starter 自动装配。
 */
@AutoConfiguration
@EnableConfigurationProperties(OneselfElasticsearchProperties.class)
@ConditionalOnClass(ElasticsearchClient.class)
@ConditionalOnProperty(prefix = "oneself.elasticsearch", name = "enabled", havingValue = "true", matchIfMissing = true)
public class OneselfElasticsearchAutoConfiguration {

    /**
     * 构建 Elasticsearch RestClient。
     */
    @Bean
    @ConditionalOnMissingBean(RestClient.class)
    public RestClient restClient(OneselfElasticsearchProperties properties) {
        HttpHost[] hosts = Arrays.stream(splitUris(properties.getUris()))
                .map(uri -> parseHost(uri, properties.getScheme()))
                .toList()
                .toArray(new HttpHost[0]);
        RestClientBuilder builder = RestClient.builder(hosts)
                .setRequestConfigCallback(requestConfigBuilder -> requestConfigBuilder
                        .setConnectTimeout((int) properties.getConnectTimeout().toMillis())
                        .setSocketTimeout((int) properties.getSocketTimeout().toMillis()));
        if (hasText(properties.getUsername()) && hasText(properties.getPassword())) {
            CredentialsProvider provider = new BasicCredentialsProvider();
            provider.setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials(properties.getUsername(),
                            properties.getPassword()));
            builder.setHttpClientConfigCallback(httpClientBuilder ->
                    httpClientBuilder.setDefaultCredentialsProvider(provider));
        }
        return builder.build();
    }

    /**
     * 构建 ElasticsearchClient。
     */
    @Bean
    @ConditionalOnMissingBean(ElasticsearchClient.class)
    public ElasticsearchClient elasticsearchClient(RestClient restClient) {
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        return new ElasticsearchClient(transport);
    }

    /**
     * 统一操作封装。
     */
    @Bean
    @ConditionalOnMissingBean
    public ElasticsearchOps elasticsearchOps(ElasticsearchClient client,
                                            OneselfElasticsearchProperties properties,
                                            ObjectProvider<BulkIngester<Object>> bulkIngester) {
        return new ElasticsearchOps(client, properties, bulkIngester.getIfAvailable());
    }

    /**
     * Bulk 写入器。
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "oneself.elasticsearch", name = "bulk-enabled", havingValue = "true", matchIfMissing = true)
    public BulkIngester<Object> bulkIngester(ElasticsearchClient client, OneselfElasticsearchProperties properties) {
        return BulkIngester.of(builder -> builder
                .client(client)
                .maxOperations(properties.getBulkMaxOperations())
                .flushInterval(properties.getBulkFlushInterval().toMillis(), TimeUnit.MILLISECONDS));
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private String[] splitUris(String uris) {
        if (uris == null || uris.isBlank()) {
            return new String[0];
        }
        return Arrays.stream(uris.split(","))
                .map(String::trim)
                .filter(item -> !item.isEmpty())
                .toArray(String[]::new);
    }

    private HttpHost parseHost(String host, String scheme) {
        String[] parts = host.split(":", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid elasticsearch uri, expected ip:port but got: " + host);
        }
        String hostname = parts[0].trim();
        int port = Integer.parseInt(parts[1].trim());
        String realScheme = hasText(scheme) ? scheme : "http";
        return new HttpHost(hostname, port, realScheme);
    }
}
