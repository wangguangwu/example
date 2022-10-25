package com.wangguangwu.threadpool;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wangguangwu
 */
public class BasicThreadPool extends Thread implements ThreadPool {

    /**
     * 初始化线程数量
     */
    private final int initSize;

    /**
     * 线程池最大线程数量
     */
    private final int maxSize;

    /**
     * 线程池核心线程数量
     */
    private final int coreSize;

    /**
     * 当前活跃的线程数量
     */
    private int activeCount;

    /**
     * 线程工厂
     */
    private final ThreadFactory threadFactory;

    /**
     * 线程队列
     */
    private final RunnableQueue runnableQueue;

    /**
     * 线程池是否关闭
     */
    private volatile boolean isShutdown = false;

    /**
     * 工作线程队列
     */
    private final Queue<ThreadTask> threadQueue = new ArrayDeque<>();

    /**
     * 默认拒绝策略
     */
    private static final DenyPolicy DEFAULT_DENY_POLICY = new DenyPolicy.AbortDenyPolicy();

    /**
     * 线程工厂
     */
    private static final ThreadFactory DEFAULT_THREAD_FACTORY = new DefaultThreadFactory();

    private final long keepAliveTime;

    private final TimeUnit timeUnit;

    public BasicThreadPool(int initSize, int coreSize, int maxSize,
                           int queueSize) {
        this(initSize, coreSize, maxSize, DEFAULT_THREAD_FACTORY,
                queueSize, DEFAULT_DENY_POLICY, 10, TimeUnit.SECONDS);
    }

    public BasicThreadPool(int initSize, int coreSize, int maxSize,
                           ThreadFactory threadFactory, int queueSize,
                           DenyPolicy denyPolicy, long keepAliveTime, TimeUnit timeUnit) {
        this.initSize = initSize;
        this.maxSize = maxSize;
        this.coreSize = coreSize;
        this.threadFactory = threadFactory;
        this.runnableQueue = new LinkedRunnableQueue(queueSize, denyPolicy, this);
        this.keepAliveTime = keepAliveTime;
        this.timeUnit = timeUnit;
        this.init();
    }

    /**
     * 初始化时，先创建 initSize 个线程
     */
    private void init() {
        start();
        for (int i = 0; i < initSize; i++) {
            newThread();
        }
    }

    @Override
    public void run() {
        while (!isShutdown && !isInterrupted()) {
            try {
                timeUnit.sleep(keepAliveTime);
            } catch (InterruptedException e) {
                isShutdown = false;
                Thread.currentThread().interrupt();
            }

            synchronized (this) {
                if (isShutdown) {
                    break;
                }
                // 当前的队列中有任务尚未处理，扩容到核心线程数
                if (runnableQueue.size() > 0 && activeCount < coreSize) {
                    for (int i = initSize; i < coreSize; i++) {
                        System.out.println("--create");
                        newThread();
                    }
                    continue;
                }
                // 当前的队列中有任务尚未处理，扩容到最大线程数
                if (runnableQueue.size() > 0 && activeCount < maxSize) {
                    for (int i = coreSize; i < maxSize; i++) {
                        newThread();
                    }
                }

                // 如果没有任务，需要回收线程，将线程数维持在核心线程数
                if (runnableQueue.size() == 0 && activeCount > coreSize) {
                    for (int i = coreSize; i < activeCount; i++) {
                        removeThread();
                    }
                }
            }
        }
    }

    @Override
    public void execute(Runnable runnable) {
        // 判断线程池是否销毁
        checkIsDestroy();
        this.runnableQueue.offer(runnable);
    }

    @Override
    public void shutdown() {
//        synchronized (this) {
//            if (isShutdown) {
//                return;
//            }
//            isShutdown = true;
//            threadQueue.forEach(threadTask -> {
//                threadTask.internalTask.stop();
//                threadTask.thread.interrupt();
//            });
//            this.interrupt();
//        }
        synchronized (this)
        {
            if (isShutdown) return;
            isShutdown = true;
            threadQueue.forEach(threadTask ->
            {
                threadTask.internalTask.stop();
                threadTask.thread.interrupt();
            });
            this.interrupt();
        }
    }

    @Override
    public int getInitSize() {
        checkIsDestroy();
        return this.initSize;
    }

    @Override
    public int getMaxSize() {
        checkIsDestroy();
        return this.maxSize;
    }

    @Override
    public int getCoreSize() {
        checkIsDestroy();
        return this.coreSize;
    }

    @Override
    public int getQueueSize() {
        checkIsDestroy();
        return runnableQueue.size();
    }

    @Override
    public int getActiveCount() {
        synchronized (this) {
            return this.activeCount;
        }
    }

    @Override
    public boolean isShutdown() {
        return this.isShutdown;
    }

    private void checkIsDestroy() {
        if (isShutdown) {
            throw new IllegalStateException("The thread pool is destroy");
        }
    }

    private void newThread() {
        // 创建任务线程，并且启动
        InternalTask internalTask = new InternalTask(runnableQueue);
        Thread thread = this.threadFactory.createThread(internalTask);
        ThreadTask threadTask = new ThreadTask(thread, internalTask);
        threadQueue.offer(threadTask);
        this.activeCount++;
        thread.start();
    }

    private void removeThread() {
        // 从线程池中移除某个线程
        ThreadTask threadTask = threadQueue.remove();
        threadTask.internalTask.stop();
        this.activeCount--;
    }

    private static class ThreadTask {

        Thread thread;

        InternalTask internalTask;

        public ThreadTask(Thread thread, InternalTask internalTask) {
            this.thread = thread;
            this.internalTask = internalTask;
        }
    }

    private static class DefaultThreadFactory implements ThreadFactory {

        private static final AtomicInteger GROUP_COUNTER = new AtomicInteger(1);

        private static final ThreadGroup group = new ThreadGroup("MyThreadGroup-" + GROUP_COUNTER.getAndIncrement());

        private static final AtomicInteger COUNTER = new AtomicInteger(0);

        @Override
        public Thread createThread(Runnable runnable) {
            return new Thread(group, runnable, "thread-pool-" + COUNTER.getAndIncrement());
        }
    }
}
