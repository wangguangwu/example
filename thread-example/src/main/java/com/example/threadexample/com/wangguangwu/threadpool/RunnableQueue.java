package com.example.threadexample.com.wangguangwu.threadpool;

/**
 * 存放提交的 runnable
 *
 * @author wangguangwu
 */
public interface RunnableQueue {

    /**
     * 提交任务到队列中
     *
     * @param runnable 任务
     */
    void offer(Runnable runnable);


    /**
     * 从队列中获取任务
     *
     * @return 任务
     * @throws InterruptedException 中断
     */
    Runnable take() throws InterruptedException;

    /**
     * 获取任务队列中任务的数量
     *
     * @return queue size
     */
    int size();

}
