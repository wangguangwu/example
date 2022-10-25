package com.wangguangwu.threadpool;

/**
 * 通知任务提交者，任务队列已满，无法再接收新的任务
 * 类似于 juc 下的 RejectedExecutionException
 *
 * @author wangguangwu
 */
public class RunnableDenyException extends RuntimeException {

    public RunnableDenyException(String message) {
        super(message);
    }
}
