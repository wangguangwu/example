package com.wangguangwu;

import com.google.common.collect.Lists;
import io.lettuce.core.Limit;
import io.lettuce.core.Range;
import io.lettuce.core.RedisClient;
import io.lettuce.core.ScoredValue;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.lang.Float.NEGATIVE_INFINITY;
import static java.lang.Float.POSITIVE_INFINITY;

/**
 * @author wangguangwu
 */
public class ZSetTest {

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

    //==============================积分排名=================================

    @Test
    public void sortedSetRank() {
        List<String> players = Lists.newArrayList("zhangsan", "lisi", "wangwu", "zhaoli", "sunqi");

        // 更新数据线程
        new Thread(() -> {
            while (true) {
                int randIndex = ThreadLocalRandom.current().nextInt(100);
                int result = ThreadLocalRandom.current().nextInt(5);
                int i = randIndex % players.size();
                String player = players.get(i);
                int point = (result - 1) > 0 ? 1 : 0;
                System.out.println("对战结果：" + player + "，" + point);
                try {
                    asyncCommands.zaddincr("NationalRank", point, player)
                            .get(1, TimeUnit.SECONDS);
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException | ExecutionException | TimeoutException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        while (true) {
            try {
                List<ScoredValue<String>> nationalRank = asyncCommands.zrevrangebyscoreWithScores("NationalRank",
                        Range.create(NEGATIVE_INFINITY, POSITIVE_INFINITY),
                        Limit.create(0, 3)
                ).get(1, TimeUnit.SECONDS);
                System.out.println("rank:");
                for (int i = 0; i < nationalRank.size(); i++) {
                    ScoredValue scoredValue = nationalRank.get(i);
                    System.out.println("第" + (i + 1) + "名：" + scoredValue.getValue() + "：" + scoredValue.getScore());
                }
                System.out.println("=======================");
                TimeUnit.SECONDS.sleep(5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //===============================延迟消息=====================================

    @Test
    public void delayMessage() {
        // 写入数据
        new Thread(() -> {
            while (true) {
                int randDelayTime = ThreadLocalRandom.current().nextInt(10);
                long current = System.currentTimeMillis() / 1000;
                long startTime = current + randDelayTime;
                try {
                    asyncCommands.zadd("messageCenter", startTime, "task_" + startTime).get(1, TimeUnit.SECONDS);
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException | ExecutionException | TimeoutException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // 模拟延迟消息系统，定时从 Sorted Set 中弹出消息，并发送出去
        while (true) {
            try {
                TimeUnit.SECONDS.sleep(5);
                long current = System.currentTimeMillis() / 1000;
                Long zCount = asyncCommands.zcount("messageCenter",
                        Range.create(0, current)).get(1, TimeUnit.SECONDS);
                if (zCount <= 0) {
                    System.out.println("没有到期时间");
                    continue;
                }
                List<ScoredValue<String>> tasks =
                        asyncCommands.zpopmin("messageCenter", zCount).get(1, TimeUnit.SECONDS);
                for (ScoredValue<String> task : tasks) {
                    System.out.println("发送消息：" + task.getValue() + "，" + Double.valueOf(task.getScore()).longValue());
                }
                System.out.println("==========================");
            } catch (InterruptedException | TimeoutException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
