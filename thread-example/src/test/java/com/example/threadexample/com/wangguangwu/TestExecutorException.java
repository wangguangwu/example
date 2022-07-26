package com.example.threadexample.com.wangguangwu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

/**
 * 一个线程池中的线程抛出了未经捕获的运行时异常，线程池会怎么处理这个线程呢
 *
 * @author wangguangwu
 */
@SuppressWarnings("all")
class TestExecutorException {

    ThreadPoolExecutor executorService;

    @BeforeEach
    public void init() {
        executorService = new ThreadPoolExecutor(2,
                2,
                30,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10));
    }

    @Test
    void testExecute() {
        // 会打印出堆栈异常
        executorService.execute(() -> sayHi("execute"));
    }

    @Test
    void testSubmitNotPrint() {
        // 不会打印出堆栈异常
        executorService.submit(() -> sayHi("submit"));
    }

    @Test
    void testSubmitPrint() {
        // 打印出堆栈异常
        Future<?> future = executorService.submit(() -> sayHi("submit"));
        try {
            // 直接报错
            Object o = future.get();
            System.out.println("o:" + o);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testSubmitPack() {
        // 包裹住异常信息
        Future<?> future = executorService.submit(() -> {
            try {
                sayHi("submit");
            } catch (Exception e) {
                System.out.println("sayHi Exception");
                e.printStackTrace();
            }
        });

        try {
            // 此时 get 并没有获取到异常
            Object o = future.get();
            System.out.println("o:" + o);
        } catch (Exception e) {
            System.out.println("future.get Exception");
            e.printStackTrace();
        }
    }


    //===========================================私有方法==================================================

    private void sayHi(String name) {
        System.out.printf("[Thread-name]: %1$s，执行方式：[%2$s]\n", Thread.currentThread().getName(), name);
        throw new RuntimeException("execute error");
    }

}
