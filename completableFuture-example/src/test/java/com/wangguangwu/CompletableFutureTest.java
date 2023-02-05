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
        System.out.println("获取用户信息: " + userFuture.get());
        System.out.println("获取商品信息: " + goodsFuture.get());
        System.out.printf("耗时: %s ms \n", (System.currentTimeMillis() - startTime));
        // 模拟主程序耗时
        TimeUnit.MILLISECONDS.sleep(600);

        System.out.printf("总耗时: %s ms \n", (System.currentTimeMillis() - startTime));
    }

    @Test
    public void testCompletableFuture() throws InterruptedException, ExecutionException {
        long startTime = System.currentTimeMillis();

        // 用户服务
        CompletableFuture<String> userFuture = CompletableFuture.supplyAsync(
                () -> {
                    try {
                        TimeUnit.MILLISECONDS.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return "用户 A";
                }
        );

        // 商品服务
        CompletableFuture<String> goodsFuture = CompletableFuture.supplyAsync(
                () -> {
                    try {
                        TimeUnit.MILLISECONDS.sleep(400);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return "商品 A";
                }
        );

        // 模拟主程序耗时
        System.out.println("获取用户信息: " + userFuture.get());
        System.out.println("获取商品信息: " + goodsFuture.get());
        System.out.printf("耗时: %s ms \n", (System.currentTimeMillis() - startTime));
        TimeUnit.MILLISECONDS.sleep(600);
        System.out.printf("总耗时: %s ms \n", (System.currentTimeMillis() - startTime));
    }

    @Test
    public void testCompletableFutureGet() throws ExecutionException, InterruptedException {
        CompletableFuture<String> test1 = CompletableFuture.supplyAsync(
                () -> {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return "Hello World";
                }
        );

        // getNow() 方法测试
        System.out.println(test1.getNow("Hello China"));
        System.out.println("=========================================");

        // join() 方法测试
        CompletableFuture<Integer> test2 = CompletableFuture.supplyAsync(
                () -> 1 / 0
        );
        System.out.println(test2.join());
        System.out.println("=========================================");

        // get() 方法测试
        CompletableFuture<Integer> test3 = CompletableFuture.supplyAsync(
                () -> 1 / 0
        );
        System.out.println(test3.get());
    }

    @Test
    public void testCompletableThenRun() throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();

        CompletableFuture<Void> test1 = CompletableFuture.runAsync(
                () -> {
                    try {
                        // 执行任务 A
                        TimeUnit.MILLISECONDS.sleep(600);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        );

        // 做完第一个任务后，再做第二个任务，第二个任务也没有返回值
        CompletableFuture<Void> test2 = test1.thenRunAsync(
                () -> {
                    try {
                        // 执行任务 B
                        TimeUnit.MILLISECONDS.sleep(400);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        );
        // get() 方法测试
        System.out.println(test2.get());

        // 模拟主程序耗时
        TimeUnit.MILLISECONDS.sleep(600);
        System.out.printf("总耗时: %s ms\n", (System.currentTimeMillis() - startTime));
    }
}
