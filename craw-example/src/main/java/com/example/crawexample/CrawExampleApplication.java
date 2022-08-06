package com.example.crawexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author wangguangwu
 */
@EnableScheduling
@SpringBootApplication
public class CrawExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrawExampleApplication.class, args);
    }

}
