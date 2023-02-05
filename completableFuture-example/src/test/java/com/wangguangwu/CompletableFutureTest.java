package com.wangguangwu;

import org.junit.Test;

import java.util.concurrent.*;

/**
 * @author wangguangwu
 */
public class CompletableFutureTest {

    @Test
    public void testFuture() throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        // Future 提供了异步执行任务的能力，但对于结果的获取并不方便
        Future<String> future = executorService.submit(
                () -> {
                    TimeUnit.SECONDS.sleep(2);
                    return "hello";
                }
        );
        // 需要使用 Future.get() 的方式阻塞调用线程
        // 也可以使用轮询方式判断 Future.isDone()，即任务是否结束，再获取结果
        System.out.println("future is done: " + future.isDone());
        System.out.println(future.get());
        System.out.println("end");
        System.out.println("future is done: " + future.isDone());
    }

    @Test
    public void testCountDownLatch() throws InterruptedException, ExecutionException {
        // Future 无法解决多个异步任务之间需要相互依赖的场景，即主线程无法等待子任务执行执行完毕之后再进行执行
        // 引入 CountDownLatch
        // 定义两个 Future，第一个通过用户 id 获取用户信息，第二个通过商品 id 获取商品信息
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        CountDownLatch countDownLatch = new CountDownLatch(2);
        long startTime = System.currentTimeMillis();
        // 用户 Future
        Future<String> userFuture = executorService.submit(
                () -> {
                    TimeUnit.MILLISECONDS.sleep(500);
                    countDownLatch.countDown();
                    return "用户 A";
                }
        );
        // 商品 Future
        Future<String> goodsFuture = executorService.submit(
                () -> {
                    TimeUnit.MILLISECONDS.sleep(400);
                    countDownLatch.countDown();
                    return "商品 A";
                }
        );
        countDownLatch.await();
        // 模拟主程序耗时
        TimeUnit.MILLISECONDS.sleep(300);
        System.out.println("获取用户信息: " + userFuture.get());
        System.out.println("获取商品信息: " + goodsFuture.get());
        System.out.println("总耗时: " + (System.currentTimeMillis() - startTime));
    }

    @Test
    public void testCompletableFuture() {
        long startTime = System.currentTimeMillis();

        // 获取用
    }
}
