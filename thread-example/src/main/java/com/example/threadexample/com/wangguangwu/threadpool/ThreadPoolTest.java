package com.example.threadexample.com.wangguangwu.threadpool;

import java.util.concurrent.TimeUnit;

/**
 * @author wangguangwu
 */
public class ThreadPoolTest {

    public static void main(String[] args) throws InterruptedException {
        // 创建一个线程池
        ThreadPool threadPool = new BasicThreadPool(2, 4, 6, 1000);
        // 往线程池中放 20 个任务
        int times = 20;
        for (int i = 0; i < times; i++) {
            threadPool.execute(() -> {
                try {
                    TimeUnit.SECONDS.sleep(10);
                    System.out.println(Thread.currentThread().getName() + " is running and done.");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

//        while (true) {
//            System.out.println("getActiveCount:" + threadPool.getActiveCount());
//            System.out.println("getQueueSize:" + threadPool.getQueueSize());
//            System.out.println("getCoreSize:" + threadPool.getCoreSize());
//            System.out.println("getMaxSize:" + threadPool.getMaxSize());
//            System.out.println("======================================");
//            TimeUnit.SECONDS.sleep(5);
//        }

        TimeUnit.SECONDS.sleep(12);
        // 关闭线程池
        threadPool.shutdown();
        // 使 main 线程 join
        Thread.currentThread().join();
    }

}
