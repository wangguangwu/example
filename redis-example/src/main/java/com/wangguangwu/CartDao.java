package com.wangguangwu;

import io.lettuce.core.api.async.RedisAsyncCommands;
import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author wangguangwu
 */
@AllArgsConstructor
public class CartDao {

    private static final String CART_PREFIX = "cart_";

    private RedisAsyncCommands<String, String> asyncCommands;

    public void add(long userId, String productId) throws ExecutionException, InterruptedException, TimeoutException {
        Boolean result = asyncCommands.hset(CART_PREFIX + userId, productId, "1")
                .get(1, TimeUnit.SECONDS);
        if (Boolean.TRUE.equals(result)) {
            System.out.println("添加购物车成功,productId:" + productId);
        }
    }

    public void remove(long userId, String productId) throws ExecutionException, InterruptedException, TimeoutException {
        Long result = asyncCommands.hdel(CART_PREFIX + userId, productId)
                .get(1, TimeUnit.SECONDS);
        if (result == 1) {
            System.out.println("商品删除成功，productId:" + productId);
        }
    }

    public void submitOrder(long userId) throws ExecutionException, InterruptedException, TimeoutException {
        Map<String, String> cardInfo = asyncCommands.hgetall(CART_PREFIX + userId)
                .get(1, TimeUnit.SECONDS);
        System.out.println("用户:" + userId + ", 提交订单:");
        for (Map.Entry<String, String> entry : cardInfo.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
    }

    public void incr(long userId, String productId) throws ExecutionException, InterruptedException, TimeoutException {
        Long result = asyncCommands.hincrby(CART_PREFIX + userId, productId, 1)
                .get(1, TimeUnit.SECONDS);
        System.out.println("商品数量加1成功，剩余数量为:" + result);
    }

    public void decr(long userId, String productId) throws ExecutionException, InterruptedException, TimeoutException {
        String count = asyncCommands.hget(CART_PREFIX + userId, productId)
                .get(1, TimeUnit.SECONDS);
        if (Long.parseLong(count) - 1 <= 0) {
            // 删除商品
            remove(userId, productId);
            return;
        }
        Long result = asyncCommands.hincrby(CART_PREFIX + userId, productId, -1)
                .get(1, TimeUnit.SECONDS);
        System.out.println("商品数量减1成功，剩余数量为:" + result);
    }
}
