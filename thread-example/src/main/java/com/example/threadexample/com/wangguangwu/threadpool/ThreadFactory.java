package com.example.threadexample.com.wangguangwu.threadpool;

/**
 * 创建线程的工厂
 *
 * @author wangguangwu
 */
@FunctionalInterface
public interface ThreadFactory {

    /**
     * 创建线程
     *
     * @param runnable 任务
     * @return thread
     */
    Thread createThread(Runnable runnable);

}
