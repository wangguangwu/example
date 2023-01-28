package com.wangguangwu;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisClient;
import io.lettuce.core.SetArgs;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author wangguangwu
 */
public class RedisTest {

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

    //============================使用 Redis 缓存对象=================================

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

    //==========================使用 Redis 创建分布式锁=================================

    @Test
    public void distributedLock() throws InterruptedException {
        int threadNum = 1;
        CountDownLatch countDownLatch = new CountDownLatch(threadNum);
        Runnable runnable = () -> {
            try {
                countDownLatch.await();
                String threadName = Thread.currentThread().getName();
                while (true) {
                    // 获取锁
                    SetArgs setArgs = SetArgs.Builder.ex(5).nx();
                    String success = asyncCommands
                            .set("update-product", threadName, setArgs)
                            .get(1, TimeUnit.SECONDS);
                    // 加锁失败
                    if (!"OK".equals(success)) {
                        System.out.println(threadName + "加锁失败，自旋等待锁");
                        TimeUnit.MILLISECONDS.sleep(100);
                    } else {
                        System.out.println(threadName + "加锁成功");
                        break;
                    }
                }
                // 加锁成功
                System.out.println(threadName + "开始执行业务逻辑");
                TimeUnit.SECONDS.sleep(1);
                System.out.println(threadName + "业务逻辑执行结束");
                // 释放锁
                asyncCommands.del("update-product").get(1, TimeUnit.SECONDS);
                System.out.println(threadName + "释放锁");
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        Thread thread1 = new Thread(runnable);
        Thread thread2 = new Thread(runnable);
        Thread thread3 = new Thread(runnable);
        thread1.start();
        thread2.start();
        thread3.start();

        countDownLatch.countDown();

        TimeUnit.HOURS.sleep(1);
    }

    //==========================使用 Redis 进行限流=================================

    @Test
    public void limit() throws Exception{
        String prefix = "order-service";
        long maxQps = 10;
        long nowSeconds = System.currentTimeMillis() / 1000;
        for (int i = 0; i < 15; i++) {
            Long result = asyncCommands.incr(prefix + nowSeconds).get(1, TimeUnit.SECONDS);
            if (result > maxQps) {
                System.out.println("请求被限流");
            } else {
                System.out.println("请求被正常处理");
            }
        }
    }
}
