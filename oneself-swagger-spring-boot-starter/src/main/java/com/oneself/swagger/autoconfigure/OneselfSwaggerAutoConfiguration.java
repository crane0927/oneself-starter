package com.oneself.swagger.autoconfigure;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * Swagger Starter 自动装配。
 */
@AutoConfiguration
@EnableConfigurationProperties(OneselfSwaggerProperties.class)
@ConditionalOnClass(OpenAPI.class)
@ConditionalOnProperty(prefix = "oneself.swagger", name = "enabled", havingValue = "true", matchIfMissing = true)
public class OneselfSwaggerAutoConfiguration {

    /**
     * OpenAPI 基础信息。
     */
    @Bean
    @ConditionalOnMissingBean
    public OpenAPI oneselfOpenApi(OneselfSwaggerProperties properties) {
        Info info = new Info();
        if (hasText(properties.getTitle())) {
            info.setTitle(properties.getTitle());
        }
        if (hasText(properties.getDescription())) {
            info.setDescription(properties.getDescription());
        }
        if (hasText(properties.getVersion())) {
            info.setVersion(properties.getVersion());
        }
        if (hasText(properties.getTermsOfService())) {
            info.setTermsOfService(properties.getTermsOfService());
        }
        Contact contact = buildContact(properties.getContact());
        if (contact != null) {
            info.setContact(contact);
        }
        License license = buildLicense(properties.getLicense());
        if (license != null) {
            info.setLicense(license);
        }
        return new OpenAPI().info(info);
    }

    /**
     * 按包或路径分组的 API 文档。
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "oneself.swagger", name = "group-enabled", havingValue = "true", matchIfMissing = true)
    public GroupedOpenApi groupedOpenApi(OneselfSwaggerProperties properties) {
        String groupName = hasText(properties.getGroupName()) ? properties.getGroupName() : "default";
        GroupedOpenApi.Builder builder = GroupedOpenApi.builder().group(groupName);
        String[] packagesToScan = toArray(properties.getBasePackages());
        if (packagesToScan.length > 0) {
            builder.packagesToScan(packagesToScan);
        }
        String[] pathsToMatch = toArray(properties.getPathsToMatch());
        if (pathsToMatch.length > 0) {
            builder.pathsToMatch(pathsToMatch);
        }
        String[] pathsToExclude = toArray(properties.getPathsToExclude());
        if (pathsToExclude.length > 0) {
            builder.pathsToExclude(pathsToExclude);
        }
        return builder.build();
    }

    private Contact buildContact(OneselfSwaggerProperties.ContactProperties properties) {
        if (properties == null) {
            return null;
        }
        boolean hasName = hasText(properties.getName());
        boolean hasUrl = hasText(properties.getUrl());
        boolean hasEmail = hasText(properties.getEmail());
        if (!hasName && !hasUrl && !hasEmail) {
            return null;
        }
        Contact contact = new Contact();
        if (hasName) {
            contact.setName(properties.getName());
        }
        if (hasUrl) {
            contact.setUrl(properties.getUrl());
        }
        if (hasEmail) {
            contact.setEmail(properties.getEmail());
        }
        return contact;
    }

    private License buildLicense(OneselfSwaggerProperties.LicenseProperties properties) {
        if (properties == null) {
            return null;
        }
        boolean hasName = hasText(properties.getName());
        boolean hasUrl = hasText(properties.getUrl());
        if (!hasName && !hasUrl) {
            return null;
        }
        License license = new License();
        if (hasName) {
            license.setName(properties.getName());
        }
        if (hasUrl) {
            license.setUrl(properties.getUrl());
        }
        return license;
    }

    private String[] toArray(List<String> values) {
        if (values == null || values.isEmpty()) {
            return new String[0];
        }
        return values.stream()
                .filter(this::hasText)
                .toArray(String[]::new);
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
