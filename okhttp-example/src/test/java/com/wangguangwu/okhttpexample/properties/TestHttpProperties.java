package com.wangguangwu.okhttpexample.properties;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author wangguangwu
 */
@SpringBootTest
class TestHttpProperties {

    @Resource
    private HttpProperties httpProperties;

    @Test
    void testGet() {
        String result = httpProperties.toString();
        Assertions.assertNotNull(result);
        System.out.println(result);
    }

}
