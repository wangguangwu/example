package com.wangguangwu;

import cn.hutool.core.collection.CollectionUtil;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author wangguangwu
 */
public class HashTest {

    //=============================常量===============================

    private static RedisClient redisClient;

    private static StatefulRedisConnection<String, String> connection;

    private static RedisAsyncCommands<String, String> asyncCommands;

    private static Map<String, User> userDB = new HashMap<>();

    private static final String USER_CREATE_PREFIX = "wang_";

    //============================辅助方法=================================

    @Before
    public void before() {
        // 初始化 redis 连接
        redisClient = RedisClient.create("redis://127.0.0.1:6379/0");
        connection = redisClient.connect();
        asyncCommands = connection.async();
        // 初始化 DB
        userDB.put(USER_CREATE_PREFIX + "+8613912345678", new User(1L, "zhangsan", 25, "+8613912345678", "123456", "http://xxxx"));
        userDB.put(USER_CREATE_PREFIX + "+8613512345678", new User(2L, "lisi", 25, "+8613512345678", "abcde", "http://xxxx"));
        userDB.put(USER_CREATE_PREFIX + "+8618812345678", new User(3L, "wangwu", 25, "+8618812345678", "654321", "http://xxxx"));
        userDB.put(USER_CREATE_PREFIX + "+8618912345678", new User(4L, "zhaoliu", 25, "+8618912345678", "98765", "http://xxxx"));
    }

    @After
    public void after() {
        connection.close();
        redisClient.shutdown();
    }

    //============================缓存用户信息=================================

    @Test
    public void userCache() throws Exception {
        mockLogin("+8613912345678", "654321");
        mockLogin("+8613912345678", "123456");
    }

    //=============================保存购物车数据=================================

    @Test
    public void cartDao() throws ExecutionException, InterruptedException, TimeoutException {
        CartDao cartDao = new CartDao(asyncCommands);
        cartDao.add(1024, "83694");
        cartDao.add(1024, "1273979");
        cartDao.add(1024, "123323");
        cartDao.submitOrder(1024);
        cartDao.remove(1024, "123323");
        cartDao.submitOrder(1024);

        cartDao.incr(1024, "83694");
        cartDao.decr(1024, "1273979");
    }

    //============================私有方法=================================

    private static void mockLogin(String mobile, String password) throws Exception {
        // 根据手机号，查询缓存
        String key = USER_CREATE_PREFIX + mobile;
        Map<String, String> userCache =
                asyncCommands.hgetall(key)
                        .get(1, TimeUnit.SECONDS);
        User user;
        if (CollectionUtil.isEmpty(userCache)) {
            System.out.println("缓存miss，加载DB");
            user = userDB.get(key);
            if (user == null) {
                System.out.println("登录失败");
                return;
            }
            // user 转成 map
            Map<String, String> userMap = BeanUtils.describe(user);
            // 写入缓存
            Long result = asyncCommands.hset(key, userMap)
                    .get(1, TimeUnit.SECONDS);
            if (result == 1) {
                System.out.println("UserId:[" + user.getUserId() + "]已进入缓存");
            }
        } else {
            System.out.println("缓存 hit");
            user = new User();
            BeanUtils.populate(user, userCache);
        }
        if (password.equals(user.getPassword())) {
            System.out.println(user.getName() + ", 登录成功!");
        } else {
            System.out.println("登录失败");
        }
        System.out.println("===========================");
    }
}
