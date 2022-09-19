package com.example.threadexample.com.wangguangwu.threadpool;

/**
 * 从队列中取出任务，执行
 *
 * @author wangguangwu
 */
public class InternalTask implements Runnable {

    private final RunnableQueue runnableQueue;

    private volatile boolean running = true;

    public InternalTask(RunnableQueue runnableQueue) {
        this.runnableQueue = runnableQueue;
    }

    @Override
    public void run() {
        while (running
                && !Thread.currentThread().isInterrupted()) {
            try {
                Runnable task = runnableQueue.take();
                task.run();
            } catch (InterruptedException e) {
                running = false;
                System.out.println(Thread.currentThread().getName() + " = " + running);
                break;
            }
        }
    }

    /**
     * 停止当前任务
     */
    public void stop() {
        this.running = false;
    }
}
