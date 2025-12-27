package com.oneself.elasticsearch.autoconfigure;

import java.net.URI;
import java.util.Arrays;

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
import org.springframework.context.annotation.Bean;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;

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
        HttpHost[] hosts = Arrays.stream(properties.getUris())
                .map(URI::create)
                .map(uri -> new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme()))
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
    public ElasticsearchOps elasticsearchOps(ElasticsearchClient client, OneselfElasticsearchProperties properties) {
        return new ElasticsearchOps(client, properties);
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
