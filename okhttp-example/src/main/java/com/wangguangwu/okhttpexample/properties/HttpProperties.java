package com.wangguangwu.okhttpexample.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Http 配置文件
 *
 * @author wangguangwu
 */
@Configuration
@ConfigurationProperties(prefix = "ok.http")
@Data
public class HttpProperties {

    private Integer connnectTimeout;

    private Integer readTimeout;

    private Integer writeTimeout;

    private Integer maxIdleConnections;

    private Long keepAliveDuration;

}
