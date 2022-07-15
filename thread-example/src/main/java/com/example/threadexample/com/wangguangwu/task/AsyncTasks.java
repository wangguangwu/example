package com.example.threadexample.com.wangguangwu.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

/**
 * 异步任务
 *
 * @author wangguangwu
 */
@Slf4j
@Component
public class AsyncTasks {

    private static final Random RANDOM = new Random();

    @Async("taskExecutor1")
    public CompletableFuture<String> doTaskOne(String taskNo) throws InterruptedException {
        return getFuture(taskNo);
    }

    @Async("taskExecutor2")
    public CompletableFuture<String> doTaskTwo(String taskNo) throws InterruptedException {
        return getFuture(taskNo);
    }

    private CompletableFuture<String> getFuture(String taskNo) throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        log.info("[{}]任务开始执行", taskNo);
        stopWatch.start();
        Thread.sleep(RANDOM.nextInt(10000));
        stopWatch.stop();
        log.info("[{}]任务执行结束，{} ms", taskNo, stopWatch.getTotalTimeMillis());
        return CompletableFuture.completedFuture("execute end");
    }


}
