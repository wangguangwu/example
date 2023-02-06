package com.wangguangwu;

import org.junit.Before;
import org.junit.Test;

import java.util.Objects;
import java.util.concurrent.*;

/**
 * @author wangguangwu
 */
public class CompletableFutureTest {

    public long startTime;

    @Before
    public void before() {
        startTime = System.currentTimeMillis();
    }

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


    /**
     * thenRun/thenRunAsync: 做完第一个任务后，再做第二个任务，第二个任务也没有返回值
     * <p>
     * 如果在执行第一个任务时，传入了一个自定义线程池：
     * <p>
     * 调用 thenRun 方法执行第二个任务时，则第二个任务和第一个任务共用同一个线程池
     * 调用 thenRunAsync 方法执行第二个任务时，则第一个任务是我传入的线程池，第二个任务传入的是 ForkJoin 线程池
     * <p>
     * 如果不同的任务传入了不同的线程池，都会使用传入的线程池
     */
    @Test
    public void testCompletableThenRun() throws InterruptedException, ExecutionException {
        long startTime = System.currentTimeMillis();

        // 没有传入线程池，两个任务共用同一个线程池
        CompletableFuture<Void> test1 = CompletableFuture.runAsync(
                () -> {
                    try {
                        // 执行任务 A
                        TimeUnit.MILLISECONDS.sleep(500);
                        System.out.println("Task A: " + Thread.currentThread().getName());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        );

        CompletableFuture<Void> test2 = test1.thenRun(
                () -> {
                    try {
                        TimeUnit.MILLISECONDS.sleep(400);
                        System.out.println("Task B: " + Thread.currentThread().getName());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        );

        TimeUnit.MILLISECONDS.sleep(500);
        System.out.println(test2.get());
        System.out.printf("总耗时: %s ms\n", (System.currentTimeMillis() - startTime));
    }

    @Test
    public void testCompletableRun2() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        long startTime = System.currentTimeMillis();

        // 传入线程池后，两个线程还是共用一个线程池
        CompletableFuture<Void> test1 = CompletableFuture.runAsync(
                () -> {
                    try {
                        TimeUnit.MILLISECONDS.sleep(500);
                        System.out.println("Task A: " + Thread.currentThread().getName());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                },
                // 传入线程池
                executorService
        );

        CompletableFuture<Void> test2 = test1.thenRun(
                () -> {
                    try {
                        TimeUnit.MILLISECONDS.sleep(400);
                        System.out.println("Task B: " + Thread.currentThread().getName());
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

    @Test
    public void testCompletableThenRunAsync() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        long startTime = System.currentTimeMillis();

        CompletableFuture<Void> test1 = CompletableFuture.runAsync(
                () -> {
                    try {
                        // 执行任务 A
                        TimeUnit.MILLISECONDS.sleep(600);
                        System.out.println("Task A: " + Thread.currentThread().getName());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                },
                // 执行第一个任务时传入了一个线程池
                // 此时两个任务用的不是一个线程池
                // 第一个任务是我传入的线程池，第二个任务使用的是 ForkJoin 线程池
                executorService
        );

        // 做完第一个任务后，再做第二个任务，第二个任务也没有返回值
        CompletableFuture<Void> test2 = test1.thenRunAsync(
                () -> {
                    try {
                        // 执行任务 B
                        TimeUnit.MILLISECONDS.sleep(400);
                        System.out.println("Task B: " + Thread.currentThread().getName());
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


    @Test
    public void testCompletableThenRunAsync2() throws ExecutionException, InterruptedException {
        ExecutorService executorService1 = Executors.newFixedThreadPool(5);
        ExecutorService executorService2 = Executors.newFixedThreadPool(5);

        long startTime = System.currentTimeMillis();

        CompletableFuture<Void> test1 = CompletableFuture.runAsync(
                () -> {
                    try {
                        // 执行任务 A
                        TimeUnit.MILLISECONDS.sleep(600);
                        System.out.println("Task A: " + Thread.currentThread().getName());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                },
                executorService1
        );

        // 做完第一个任务后，再做第二个任务，第二个任务也没有返回值
        CompletableFuture<Void> test2 = test1.thenRunAsync(
                () -> {
                    try {
                        // 执行任务 B
                        TimeUnit.MILLISECONDS.sleep(400);
                        System.out.println("Task B: " + Thread.currentThread().getName());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                },
                executorService2
        );
        // get() 方法测试
        System.out.println(test2.get());

        // 模拟主程序耗时
        TimeUnit.MILLISECONDS.sleep(600);
        System.out.printf("总耗时: %s ms\n", (System.currentTimeMillis() - startTime));
    }

    /**
     * thenAccept/thenAcceptAsync:
     * 第一个任务执行完成后，执行第二个回调方法任务，会将第一个方法的执行结果作为入参，传递到回调方法中，但是回调方法是没有返回值的
     */
    @Test
    public void testCompletableThenAccept() throws ExecutionException, InterruptedException {
        CompletableFuture<String> test1 = CompletableFuture.supplyAsync(
                () -> "dev"
        );
        CompletableFuture<Void> test2 = test1.thenAccept(
                (a) -> System.out.println("上一个任务的返回结果：" + a)
        );
        System.out.println(test2.get());
    }

    /**
     * thenApply/thenApplyAsync:
     * 第一个任务执行完成后，执行第二个回调方法任务，会将该任务的执行结果，作为入参，传送到回调方法中，并且回调方法是有返回值的。
     */
    @Test
    public void testCompletableThenApply() throws ExecutionException, InterruptedException {
        CompletableFuture<String> test1 = CompletableFuture.supplyAsync(
                () -> "dev"
        ).thenApply(
                (a) -> Objects.equals(a, "dev") ? "dev" : "prod"
        );
        System.out.println("当前环境为：" + test1.get());
    }

    /**
     * whenComplete 函数：当 CompletableFuture 的任务无论是正常完成还是出现异常都会被调用。
     * <p>
     * 正常完成：whenComplete 返回结果和上级任务一致，异常为 null
     * 出现异常：whenComplete 返回结果为 null，异常为上级任务的异常
     */
    @Test
    public void testCompletableWhenComplete() throws ExecutionException, InterruptedException {
        CompletableFuture<Double> future = CompletableFuture.supplyAsync(
                () -> {
                    if (Math.random() < 0.5) {
                        throw new RuntimeException("error");
                    }
                    System.out.println("success");
                    return 0.11;
                }
        ).whenComplete(
                ((aDouble, throwable) -> {
                    if (aDouble == null) {
                        System.out.println("whenComplete aDouble is null");
                    } else {
                        System.out.println("whenComplete aDouble is " + aDouble);
                    }
                    if (throwable == null) {
                        System.out.println("whenComplete throwable is null");
                    } else {
                        System.out.println("whenComplete throwable is " + throwable.getMessage());
                    }
                })
        );
        System.out.println("result: " + future.get());
    }

    @Test
    public void testWhenCompleteExceptionally() throws ExecutionException, InterruptedException {
        CompletableFuture<Double> future = CompletableFuture.supplyAsync(
                () -> {
                    if (Math.random() < 0.5) {
                        throw new RuntimeException("error");
                    }
                    return 0.11;
                }
        ).whenComplete(
                ((aDouble, throwable) -> {
                    if (aDouble == null) {
                        System.out.println("whenComplete aDouble is null");
                    } else {
                        System.out.println("whenComplete aDouble is " + aDouble);
                    }
                    if (throwable == null) {
                        System.out.println("whenComplete throwable is null");
                    } else {
                        System.out.println("whenComplete throwable is " + throwable.getMessage());
                    }
                })
        ).exceptionally(
                // 出现异常时，异常会被捕获
                (throwable -> {
                    System.out.println("exceptionally中异常：" + throwable.getMessage());
                    return 0.0;
                })
        );
        System.out.println("result: " + future.get());
    }

    /**
     * AND 组合关系
     * <p>
     * thenCombine/thenAcceptBoth/runAfterBoth: 当任务一和任务二都完成再执行任务三
     * <p>
     * difference
     * runAfterBoth: 不会把执行结果当作方法入参，且没有返回值
     * thenAcceptBoth: 会把两个任务的执行结果作为方法入参，传递到指定方法中，且无返回值
     * thenCombine: 会把两个任务的执行结果作为方法入参，传递到指定方法中，有返回值
     */
    @Test
    public void testCompletableThenCombine() throws ExecutionException, InterruptedException {
        // 线程池
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        // 异步任务 1
        CompletableFuture<Integer> task1 = CompletableFuture.supplyAsync(
                () -> {
                    System.out.println("Task 1: " + Thread.currentThread().getName());
                    return 2;
                }, executorService
        );
        // 异步任务 2
        CompletableFuture<Integer> task2 = CompletableFuture.supplyAsync(
                () -> {
                    System.out.println("Task 2: " + Thread.currentThread().getName());
                    return 1;
                }, executorService
        );
        // 任务组合
        CompletableFuture<Integer> task3 = task1.thenCombineAsync(task2, (f1, f2) -> {
            System.out.println("Task 3: " + Thread.currentThread().getName());
            return f1 + f2;
        }, executorService);

        Integer result = task3.get();
        System.out.println("result: " + result);
    }

    /**
     * OR 组合关系
     * <p>
     * applyToEither/acceptEither/runAfterEither: 两个任务，只要有一个任务完成，就执行任务三
     * <p>
     * runAfterEither：不会把执行结果当做方法入参，且没有返回值
     * acceptEither: 会将已经执行完成的任务，作为方法入参，传递到指定方法中，且无返回值
     * applyToEither：会将已经执行完成的任务，作为方法入参，传递到指定方法中，且有返回值
     */
    @Test
    public void testCompletableEitherAsync() {
        // 创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        // 异步任务 1
        CompletableFuture<Integer> task1 = CompletableFuture.supplyAsync(
                () -> {
                    System.out.println("Task 1: " + Thread.currentThread().getName());
                    return 1;
                }, executorService
        );
        // 异步任务 2
        CompletableFuture<Integer> task2 = CompletableFuture.supplyAsync(
                () -> {
                    System.out.println("Task 2: " + Thread.currentThread().getName());
                    try {
                        TimeUnit.MILLISECONDS.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return 2;
                }, executorService
        );
        // 任务组合
        // 核心线程数为 1 时，任务 3 会被直接丢弃
        task1.acceptEitherAsync(task2, (res) -> {
            System.out.println("Task 3: " + Thread.currentThread().getName());
            System.out.println("上一个任务的结果为：" + res);
        }, executorService);
    }

    /**
     * allOf: 等待所有任务完成
     */
    @Test
    public void testCompletableAllOf() throws ExecutionException, InterruptedException {
        // 创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        // 异步任务 1
        CompletableFuture<Integer> task1 = CompletableFuture.supplyAsync(
                () -> {
                    System.out.println("Task 1: " + Thread.currentThread().getName());
                    return 1;
                }, executorService
        );
        // 异步任务 2
        CompletableFuture<Integer> task2 = CompletableFuture.supplyAsync(
                () -> {
                    System.out.println("Task 2: " + Thread.currentThread().getName());
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return 2;
                }
        );
        // 异步任务 3
        CompletableFuture<Integer> task3 = CompletableFuture.supplyAsync(
                () -> {
                    System.out.println("Task 3: " + Thread.currentThread().getName());
                    try {
                        TimeUnit.SECONDS.sleep(7);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return 3;
                }
        );

        // 任务组合
        CompletableFuture<Void> allOf = CompletableFuture.allOf(task1, task2, task3);

        // 等待所有任务完成
        allOf.get();
        System.out.println("task1：" + task1.get());
        System.out.println("task2：" + task2.get());
        System.out.println("task3：" + task3.get());
    }

    /**
     * anyOf: 只要有一个任务完成
     */
    @Test
    public void testCompletableAnyOf() throws ExecutionException, InterruptedException {
        // 创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        // 开启异步任务1
        CompletableFuture<Integer> task = CompletableFuture.supplyAsync(() -> 2, executorService);

        // 开启异步任务2
        CompletableFuture<Integer> task2 = CompletableFuture.supplyAsync(() -> 3, executorService);

        // 开启异步任务3
        CompletableFuture<Integer> task3 = CompletableFuture.supplyAsync(() -> 4, executorService);

        // 任务组合
        CompletableFuture<Object> anyOf = CompletableFuture.anyOf(task, task2, task3);
        // 只要有一个有任务完成
        Object o = anyOf.get();
        System.out.println("完成的任务的结果：" + o);
    }
}
