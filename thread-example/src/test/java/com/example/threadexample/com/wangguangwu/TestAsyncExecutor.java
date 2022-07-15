package com.example.threadexample.com.wangguangwu;

import com.example.threadexample.com.wangguangwu.task.AsyncTasks;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;

/**
 * @author wangguangwu
 */
@Slf4j
@SpringBootTest
class TestAsyncExecutor {

    @Resource
    private AsyncTasks asyncTasks;

    @Test
    void testAsync() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();

        // 线程池 1
        CompletableFuture<String> task1 = asyncTasks.doTaskOne("1");
        CompletableFuture<String> task2 = asyncTasks.doTaskOne("2");
        CompletableFuture<String> task3 = asyncTasks.doTaskOne("3");

        // 线程池 2
        CompletableFuture<String> task4 = asyncTasks.doTaskTwo("4");
        CompletableFuture<String> task5 = asyncTasks.doTaskTwo("5");
        CompletableFuture<String> task6 = asyncTasks.doTaskTwo("6");

        // 一起执行
        stopWatch.start();
        CompletableFuture.allOf(task1, task2, task3, task4, task5, task6)
                .join();
        stopWatch.stop();
        log.info("execute end cost time: {} ms", stopWatch.getTotalTimeMillis());
    }

}
