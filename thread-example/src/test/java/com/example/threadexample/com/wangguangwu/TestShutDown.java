package com.example.threadexample.com.wangguangwu;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * shutdown 方法：
 * 将线程池状态设置为 SHUTDOWN。
 * 平滑的关闭 ExecutorService，当此方法被调用时，ExecutorService 停止接收新的任务并且等待已提交的任务（包含提交正在执行和提交未执行）执行完成。
 * 当所有提交任务执行完毕，线程池即被关闭。
 * <p>
 * awaitTermination 方法：
 * 接收 timeOut 和 TimeUnit 两个参数，用于设定超市时间及单位。
 * 当等待超过设定时间时，会监测 ExecutorService 是否已经关闭，若关闭则返回 true，否则返回 false。
 * 一般情况下与 shutdown 方法组合使用。
 * <p>
 * shutdownNow 方法：
 * 将线程池状态设置为 STOP。
 * 跟 shutdown 方法一样，先停止接收外部提交的任务，忽律队里等待的任务，尝试将正在跑的任务 interrupt 中断，返回未执行的任务列表。
 *
 * @author wangguangwu
 */
class TestShutDown {

    ExecutorService threadPool;

    @BeforeEach
    void before() {
        threadPool = Executors.newFixedThreadPool(2);
    }

    /**
     * 从输出可以看出，在调用 shutdown() 方法后，已添加的任务会继续执行，但是不会阻塞
     * 导致 main 线程先打印出 "线程池已关闭"
     */
    @Test
    void testShutDown1() {
        for (int i = 0; i < 5; i++) {
            // lambda 表达式中只能使用 final 变量，所以使用 String 类型
            String str = i + "";
            threadPool.execute(() -> System.out.println(str));
        }
        // 关闭线程池
        threadPool.shutdown();
        System.out.println("线程池已关闭");
        Assertions.assertTrue(threadPool.isShutdown());
    }

    /**
     * 调用 shutdown() 方法后继续往线程池中添加任务，会执行拒绝策略
     * 拒绝策略：http://www.wangguangwu.com/archives/xian-cheng-chi-de-si-zhong-ju-jue-ce-lve
     */
    @Test
    void testShutDown2() {
        for (int i = 0; i < 5; i++) {
            String str = i + "";
            threadPool.execute(() -> System.out.println(str));
        }
        threadPool.shutdown();
        // 调用 shutdown() 方法后继续添加任务
        threadPool.execute(() -> System.out.println("Hello World"));
        System.out.println("线程池已关闭");
        Assertions.assertTrue(threadPool.isShutdown());
    }

    /**
     * 为保证执行顺序，一般会把 shutdown() 和 awaitTermination() 方法组合使用
     */
    @Test
    void testShutDOwnAndAwait() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            String str = i + "";
            threadPool.execute(() -> System.out.println(str));
        }
        // shutdown() 和 awaitTermination() 方法组合使用
        threadPool.shutdown();
        boolean success = threadPool.awaitTermination(1, TimeUnit.HOURS);
        System.out.println("线程池已关闭");
        Assertions.assertTrue(success);
    }

    /**
     * 调用 shutdownNow() 方法后，线程池会立即关闭，未执行的任务会以列表的形式返回
     */
    @Test
    void testShutDownNow() {
        for (int i = 0; i < 5; i++) {
            String str = i + "";
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(str);
                }

                @Override
                public String toString() {
                    return "这是第[" + str + "]个任务";
                }
            });
        }
        // 调用 shutdownNow() 方法
        List<Runnable> tasks = threadPool.shutdownNow();
        System.out.println("线程池已关闭");
        // 打印未执行的任务
        tasks.forEach(System.out::println);
        Assertions.assertTrue(threadPool.isShutdown());
    }

}
