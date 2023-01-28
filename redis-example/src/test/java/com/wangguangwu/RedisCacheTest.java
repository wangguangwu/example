package com.wangguangwu;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * 使用 Redis 缓存对象
 *
 * @author wangguangwu
 */
public class RedisCacheTest {

    //=============================常量===============================

    private static RedisClient redisClient;

    private static StatefulRedisConnection<String, String> connection;

    private static RedisAsyncCommands<String, String> asyncCommands;

    //============================辅助方法=================================

    @Before
    public void before() {
        redisClient = RedisClient.create("redis://127.0.0.1:6379/0");
        connection = redisClient.connect();
        asyncCommands = connection.async();
    }

    @After
    public void after() {
        connection.close();
        redisClient.shutdown();
    }

    //============================测试方法=================================

    @Test
    public void cacheProduct() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        Product product = new Product();
        product.setName("杯子");
        product.setPrice(100d);
        product.setDesc("这是一个锤子");
        String json = objectMapper.writeValueAsString(product);

        asyncCommands.set("product", json).get(1, TimeUnit.SECONDS);
    }

    @Test
    public void getProduct() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = asyncCommands.get("product").get(1, TimeUnit.SECONDS);
        Product product = objectMapper.readValue(json, new TypeReference<Product>() {
        });
        System.out.println(product);
    }
}
