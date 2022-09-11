package com.example.threadexample.com.wangguangwu.waitandnotify;

import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * 定义一个 lock 接口
 *
 * @author wangguangwu
 */
public interface Lock {

    /**
     * 除非获取到了锁，否则永远阻塞
     * 该方法可以被中断，中断时会抛出 InterruptedException
     *
     * @throws InterruptedException 被中断抛出
     */
    void lock() throws InterruptedException;

    /**
     * 除了可以被中断之外，还增加了对应的超时功能
     *
     * @param mills 超时时间
     * @throws InterruptedException 被中断抛出
     * @throws TimeoutException     超时抛出
     */
    void lock(long mills) throws InterruptedException, TimeoutException;

    /**
     * 释放锁
     */
    void unlock();

    /**
     * 获取当前哪些线程被阻塞
     *
     * @return list of blocked thread
     */
    List<Thread> listBlockedThreads();

}
