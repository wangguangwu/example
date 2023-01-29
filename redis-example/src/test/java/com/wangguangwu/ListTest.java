package com.wangguangwu;

import io.lettuce.core.KeyValue;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
}
