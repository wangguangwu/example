package com.wangguangwu.threadpool;

import java.util.LinkedList;

/**
 * 自定意队列
 *
 * @author wangguangwu
 */
public class LinkedRunnableQueue implements RunnableQueue {

    /**
     * 任务队列的上限
     */
    private final int limit;

    /**
     * 任务队列中满了之后，执行拒绝策略
     */
    private final DenyPolicy denyPolicy;

    /**
     * 线程池
     */
    private final ThreadPool threadPool;

    /**
     * 存放任务的队列
     */
    private final LinkedList<Runnable> runnableList = new LinkedList<>();

    public LinkedRunnableQueue(int limit, DenyPolicy denyPolicy, ThreadPool threadPool) {
        this.limit = limit;
        this.denyPolicy = denyPolicy;
        this.threadPool = threadPool;
    }

    /**
     * 如果队列数量达到了上限，则执行拒绝策略
     * 否则将 runnable 存放至队列中，同时唤醒 take 任务的线程
     *
     * @param runnable 任务
     */
    @Override
    public void offer(Runnable runnable) {
        synchronized (runnableList) {
            if (runnableList.size() >= limit) {
                // 执行拒绝策略
                denyPolicy.reject(runnable, threadPool);
                return;
            }
            // 将任务加到队列中
            runnableList.addLast(runnable);
            // 唤醒阻塞中的线程
            runnableList.notifyAll();
        }
    }

    /**
     * 线程不断从队列中获取 Runnable 任务
     * 当队列为空的时候工作线程会陷入阻塞，有可能在阻塞的过程中被中断
     * 获取中断信号后，将异常抛出以通知上游（InternalTask）
     *
     * @return runnable 任务
     * @throws InterruptedException 中断
     */
    @Override
    public Runnable take() throws InterruptedException {
        synchronized (runnableList) {
            while (runnableList.isEmpty()) {
                try {
                    // 如果任务队列中没有可执行任务，则将当前线程挂起
                    // 进入 runnableList 关联的 monitor waitset 中等待唤醒
                    runnableList.wait();
                } catch (InterruptedException e) {
                    System.out.println("LinkedRunnableQueue take error: " + e.getMessage());
                    // 被中断时将该异常抛出
                    throw e;
                }
            }
        }
        // 从任务队列头部中移除第一个任务
        return runnableList.removeFirst();
    }

    @Override
    public int size() {
        synchronized (runnableList) {
            // 返回当前任务队列中的任务数
            return runnableList.size();
        }
    }
}
