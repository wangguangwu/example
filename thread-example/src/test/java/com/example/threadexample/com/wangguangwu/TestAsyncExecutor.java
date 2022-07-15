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

    /**
     * 分析执行情况
     * <p>
     * 1. 线程池 1 的 3 个任务，task1 和 task2 会先获得执行线程，然后 task3 没有分配执行线程所以进入缓冲数列
     * 2. 线程池 2 的 3 个任务，task4 和 task5 会先获得执行线程，然后 task6 没有分配执行线程所以进入缓冲数列
     * 3. task3 在 task1 和 task2 执行结束之后，获得线程开始执行
     * 4. task6 在 task4 和 task5 执行结束之后，获得线程开始执行
     */
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
        log.info("execute cost time: {} ms", stopWatch.getTotalTimeMillis());
    }

}
