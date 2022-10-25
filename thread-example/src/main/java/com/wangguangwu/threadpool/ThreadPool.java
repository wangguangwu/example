package com.wangguangwu.threadpool;

/**
 * 定义一个线程池应该具备的基本操作和方法
 *
 * @author wangguangwu
 */
public interface ThreadPool {

    /**
     * 提交任务到线程池
     *
     * @param runnable 任务
     */
    void execute(Runnable runnable);

    /**
     * 关闭线程池
     */
    void shutdown();

    /**
     * 获取线程池的初始化大小
     *
     * @return init
     */
    int getInitSize();

    /**
     * 获取线程池的最大线程数
     *
     * @return max
     */
    int getMaxSize();

    /**
     * 获取线程池的核心线程数量
     *
     * @return core
     */
    int getCoreSize();

    /**
     * 获取线程池中用于缓存任务队列的大小
     *
     * @return queue size
     */
    int getQueueSize();

    /**
     * 获取线程池中活跃线程的数量
     *
     * @return active threads count
     */
    int getActiveCount();

    /**
     * 查看线程池是否销毁
     *
     * @return isShutdown
     */
    boolean isShutdown();

}
