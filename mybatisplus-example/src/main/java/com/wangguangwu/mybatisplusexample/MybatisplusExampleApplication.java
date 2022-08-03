package com.wangguangwu.mybatisplusexample;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author wangguangwu
 */
@MapperScan("com.wangguangwu.mybatisplusexample.mapper")
@SpringBootApplication
public class MybatisplusExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisplusExampleApplication.class, args);
    }

}
