package com.wangguangwu.threadpool;

/**
 * 拒绝策略
 *
 * @author wangguangwu
 */
@FunctionalInterface
public interface DenyPolicy {


    /**
     * 拒绝策略
     *
     * @param runnable   任务
     * @param threadPool 线程池
     */
    void reject(Runnable runnable, ThreadPool threadPool);

    /**
     * 直接丢弃任务
     */
    class DiscardDenyPolicy implements DenyPolicy {
        @Override
        public void reject(Runnable runnable, ThreadPool threadPool) {
            // do nothing
        }
    }

    /**
     * 向任务提交者抛出异常
     */
    class AbortDenyPolicy implements DenyPolicy {
        @Override
        public void reject(Runnable runnable, ThreadPool threadPool) {
            throw new RunnableDenyException("The runnable " + runnable
                    + " will be abort.");
        }
    }

    /**
     * 在任务提交者所在的线程中执行任务
     */
    class RunnerDenyPolicy implements DenyPolicy {
        @Override
        public void reject(Runnable runnable, ThreadPool threadPool) {
            if (!threadPool.isShutdown()) {
                runnable.run();
            }
        }
    }
}
