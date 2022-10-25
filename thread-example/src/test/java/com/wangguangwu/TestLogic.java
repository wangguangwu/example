package com.wangguangwu;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wangguangwu
 */
@SuppressWarnings("all")
class TestLogic {

    ThreadPoolExecutor executor;

    List<Integer> list = new ArrayList<>();

    List<Integer> arrayList = Arrays.asList(3, 10, 29, 88, 99);

    @BeforeEach
    public void init() {
        for (int i = 0; i < 100; i++) {
            list.add(i);
        }
        executor = new ThreadPoolExecutor(4,
                8,
                60,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(100));
    }

    @AfterEach
    public void after() {
        executor.shutdown();
    }

    AtomicInteger sizeA = new AtomicInteger();
    AtomicInteger sizeB = new AtomicInteger();

    @Test
    void test() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        CountDownLatch countDownLatch = new CountDownLatch(list.size());
        // 转化数据
        for (Integer data : list) {
            Runnable runnable = () -> {
                try {
                    // 案件处理
                    caseHandle(data);
                } finally {
                    sizeA.getAndAdd(1);
                    countDownLatch.countDown();
                }
            };
            try {
                executor.execute(runnable);
            } catch (Exception e) {
                sizeB.getAndAdd(1);
                countDownLatch.countDown();
            }
        }
        countDownLatch.await();
        stopWatch.stop();
        System.out.println(sizeA.get());
        System.out.println(sizeB.get());
        System.out.println("execute cost time: " + stopWatch.getTotalTimeMillis());
    }

    private void caseHandle(Integer data) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // igonre
        }
        if (arrayList.contains(data)) {
            throw new RuntimeException(data.toString());
        }
    }

}
