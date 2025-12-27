package com.oneself.logging.autoconfigure;

import com.oneself.logging.core.AccessLogFilter;
import jakarta.servlet.Filter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

/**
 * 日志 Starter 自动装配。
 */
@AutoConfiguration
@EnableConfigurationProperties(OneselfLoggingProperties.class)
public class OneselfLoggingAutoConfiguration {

    /**
     * 访问日志过滤器注册。
     */
    @Bean
    @ConditionalOnClass(Filter.class)
    @ConditionalOnMissingBean(name = "oneselfAccessLogFilter")
    @ConditionalOnProperty(prefix = "oneself.logging", name = "access-enabled", havingValue = "true", matchIfMissing = true)
    public FilterRegistrationBean<AccessLogFilter> oneselfAccessLogFilter(OneselfLoggingProperties properties) {
        FilterRegistrationBean<AccessLogFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new AccessLogFilter(properties));
        registration.setName("oneselfAccessLogFilter");
        registration.setOrder(Ordered.LOWEST_PRECEDENCE);
        return registration;
    }
}
