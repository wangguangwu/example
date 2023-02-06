package com.wangguangwu;

import com.google.common.collect.ImmutableMap;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author wangguangwu
 */
public class SetTest {

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

    //==================================标签系统===========================================

    @Test
    public void productTag() throws ExecutionException, InterruptedException, TimeoutException {
        Map<Long, String> productDB = ImmutableMap.of(
                1L, "上衣",
                2L, "裤子",
                3L, "鞋子"
        );
        // 商品
        asyncCommands.sadd("tag_p_1", "上衣", "夏款", "长袖").get(1, TimeUnit.SECONDS);
        asyncCommands.sadd("tag_p_2", "裤子", "冬款", "潮流").get(1, TimeUnit.SECONDS);
        asyncCommands.sadd("tag_p_3", "鞋子", "折扣").get(1, TimeUnit.SECONDS);
        // 用户
        long userId = 1;
        asyncCommands.sadd("tag_u_" + userId, "冬款", "折扣").get(1, TimeUnit.SECONDS);

        for (Long productId : productDB.keySet()) {
            Set<String> result =
                    asyncCommands.sinter("tag_u_" + userId,
                            "tag_p_" + productId).get(1, TimeUnit.SECONDS);
            if (CollectionUtils.isEmpty(result)) {
                continue;
            }
            System.out.println("精选页推荐:" + productDB.get(productId)
                    + ",推荐原因:" + String.join(",", result));
        }
    }

    //==================================模拟黑白名单===========================================

    @Test
    public void blackSet() throws ExecutionException, InterruptedException, TimeoutException {
        long userId = 1;
        // 开启一个线程，模拟风控服务
        // 当当前的账号存在风险时，就会把当前用户的 userId 加到 blackUserIds 黑名单中
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(10));
                asyncCommands.sadd("blackUserIds", String.valueOf(userId))
                        .get(1, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                e.printStackTrace();
            }
        }).start();

        // 主代码
        for (int productId = 0; productId < 10; productId++) {
            try {
                boolean result = addProduct(userId, productId);
            } catch (Exception e) {
                faceCheck(userId);
            }
            // 模拟购物
            TimeUnit.SECONDS.sleep(1);
        }
    }

    //==================================私有方法=========================================

    /**
     * 添加商品
     */
    public boolean addProduct(long userId, long productId) throws ExecutionException, InterruptedException, TimeoutException {
        Boolean isBlack = asyncCommands
                .sismember("blackUserIds", String.valueOf(userId))
                .get(1, TimeUnit.SECONDS);
        if (isBlack) {
            System.out.println("帐号存在风险，请先去完成人脸验证...");
            throw new RuntimeException("risk user");
        }
        Boolean result = asyncCommands.hset("cart_" + userId, String.valueOf(productId), "1")
                .get(1, TimeUnit.SECONDS);
        if (result) {
            System.out.println("添加购物车成功,productId:" + productId);
            return true;
        }
        return false;
    }

    /**
     * 人脸识别
     */
    public void faceCheck(long userId) throws InterruptedException, ExecutionException, TimeoutException {
        System.out.println("face checking...");
        TimeUnit.SECONDS.sleep(10);
        System.out.println("face success...");
        asyncCommands.srem("blackUserIds", String.valueOf(userId))
                .get(1, TimeUnit.SECONDS);
    }
}
