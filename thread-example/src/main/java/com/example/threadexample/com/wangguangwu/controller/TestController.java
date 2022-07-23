package com.example.threadexample.com.wangguangwu.controller;

import com.example.threadexample.com.wangguangwu.config.CacheManager;
import org.springframework.boot.autoconfigure.cache.CacheType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangguangwu
 */
@RequestMapping("/hello")
@RestController
public class TestController {

    @RequestMapping("/world")
    public String hello() throws InterruptedException {
        Thread.sleep(10);
        return "Hello World";
    }

    @RequestMapping("/world2")
    public String hello2() throws InterruptedException {
        Thread.sleep(10);
        return "Hello World";
    }

    @RequestMapping("/world3")
    public String hello3() throws InterruptedException {
        checkCache("Hello World");
        Thread.sleep(10);
        CacheManager.setSimpleFlag("Hello World", false);
        return "Hello World";
    }

    /**
     * 校验缓存
     *
     * @param cacheKey cacheKey
     */
    public static void checkCache(String cacheKey) {
        if (CacheManager.getSimpleFlag(cacheKey)) {
            return;
        }
        CacheManager.setSimpleFlag(cacheKey, true);
    }

}
