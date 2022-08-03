package com.wangguangwu.mybatisplusexample.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangguangwu
 */
@Configuration
@ConfigurationProperties("spring.datasource")
@Data
public class MysqlProperties {

    private String url;

    private String username;

    private String password;

}
