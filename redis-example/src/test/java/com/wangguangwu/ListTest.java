package com.wangguangwu;

import io.lettuce.core.KeyValue;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * 利用 List 先入先出的特性，做一个消息队列
 * <p>
 * Lettuce 客户端存在一个问题
 * <p>
 * 正常情况下，像执行 LPUSH、SET、GET 等非阻塞命令时，Lettuce 的 Connection 是可以在不同线程之间共享。
 * 如果执行 BRPOP 这种阻塞命令，就不能共享 Connection 了。
 *
 * @author wangguangwu
 */
public class ListTest {

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

    //============================消息队列=================================

    @Test
    public void MQProducer() {
        // 使用 LPUSH 命令从 List 的左侧写入元素，模拟生产者生产数据
        while (true) {
            try {
                TimeUnit.SECONDS.sleep(1);
                String element = "element" + System.currentTimeMillis();
                long size = asyncCommands
                        .lpush("mq-test", element)
                        .get(1, TimeUnit.SECONDS);
                System.out.println("Producer: 写入[" + element + "]，当前 MQ 长度[" + size + "]");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void MQConsumer() {
        // 使用 BRPOP 从队列的右侧拿出元素，模拟消费者消费数据
        while (true) {
            try {
                KeyValue<String, String> keyValue =
                        asyncCommands.brpop(1, "mq-test")
                                .get(2, TimeUnit.SECONDS);
                if (keyValue != null && keyValue.hasValue()) {
                    System.out.println("Consumer: 从[" + keyValue.getKey() + "]队列中消费到[" + keyValue.getValue() + "]元素");
                } else {
                    System.out.println("Consumer:没有监听到任何数据，继续监听");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void MQConsumer2() throws InterruptedException {
        Runnable consumerRunnable = () -> {
            while (true) {
                try {
                    KeyValue<String, String> keyValue =
                            asyncCommands.brpop(1, "mq-test")
                                    .get(2, TimeUnit.SECONDS);
                    if (keyValue != null && keyValue.hasValue()) {
                        System.out.println("Consumer: 从[" + keyValue.getKey() + "]队列中消费到[" + keyValue.getValue() + "]元素");
                    } else {
                        System.out.println("Consumer:没有监听到任何数据，继续监听");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        // 执行后，在 get 处会报错：TimeoutException
        // 两个线程共用一个 Connection 去执行 BRPOP 这种阻塞命令
        // BRPOP 命令是在 Redis 服务端阻塞的，会和 Connection 本地的 get() 超时时候相互影响
        // 所以，多线程场景下想要使用阻塞命令，需要为每个线程创建专属的 Connection 对象
        Thread consumer1 = new Thread(consumerRunnable);
        Thread consumer2 = new Thread(consumerRunnable);
        consumer1.start();
        consumer2.start();
        TimeUnit.HOURS.sleep(1);
    }

    //============================提醒功能=================================

    /*
     * 可以使用 Redis 的 List 实现消息队列，但存在消息丢失的可能性，除非这个消息是可以接受丢失的
     *
     * 一个 Consumer 在消费消息时，是直接通过 BRPOP 命令把消息从 List 中弹出来
     * 在消息弹出之后但未被 Consumer 处理之前，Consumer 可能会宕机，那么消息就丢失了
     */

    @Test
    public void likeList() throws Exception {
        long videoId = 1089;
        final String listName = "like-list-" + videoId;
        Thread createLike = new Thread(
                () -> {
                    for (int i = 0; i < 1000000; i++) {
                        try {
                            // 不断往队列中插入数据
                            asyncCommands
                                    .rpush(listName, String.valueOf(i))
                                    .get(1, TimeUnit.SECONDS);
                            TimeUnit.SECONDS.sleep(1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        createLike.start();

        Thread kz = new Thread(
                () -> {
                    while (true) {
                        try {
                            // 拉取前 10 个点赞
                            List<String> useIds = asyncCommands
                                    .lrange(listName, 0, 10)
                                    .get(1, TimeUnit.SECONDS);
                            // 每隔 3s 中拉取一次
                            TimeUnit.SECONDS.sleep(3);
                            System.out.println("小伙伴:[" + useIds + "]点赞了视频:[" + videoId + "]");
                            // 删除已经拉取到的点赞信息
                            String del = asyncCommands.ltrim(listName, useIds.size(), -1)
                                    .get(1, TimeUnit.SECONDS);
                            if ("OK".equals(del)) {
                                System.out.println("删除成功");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        kz.start();
        TimeUnit.HOURS.sleep(1);
    }

    //============================热点列表=================================

    @Test
    public void hotList() throws Exception {
        DateTimeFormatter dateTimeFormatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        asyncCommands.del("hot-list").get(1, TimeUnit.SECONDS);
        // 初始化热点列表
        for (int i = 0; i < 10; i++) {
            asyncCommands.rpush("hot-list",
                    "第[" + i + "]条热点新闻，更新时间:" + dateTimeFormatter.format(LocalDateTime.now()));
        }

        // 启动更新 hotList 的线程，模拟热点新闻的更新操作
        Thread changeHotList = new Thread(
                () -> {
                    while (true) {
                        try {
                            int index = ThreadLocalRandom.current().nextInt(0, 10);
                            // 更新数据
                            asyncCommands.lset("hot-list",
                                    index,
                                    "第[" + index + "]条热点新闻，更新时间:" + dateTimeFormatter.format(LocalDateTime.now()));
                            TimeUnit.SECONDS.sleep(1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        changeHotList.start();

        // 模拟客户端更新热点新闻的拉取操作
        while (true) {
            List<String> hotList = asyncCommands.lrange("hot-list", 0, -1)
                    .get(1, TimeUnit.SECONDS);
            TimeUnit.SECONDS.sleep(5);
            System.out.println("热点新闻:");
            for (String hot : hotList) {
                System.out.println(hot);
            }
            System.out.println("===================");
        }
    }
}
