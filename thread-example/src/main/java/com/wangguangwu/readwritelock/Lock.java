package com.wangguangwu.readwritelock;

/**
 * 定义锁的基本操作
 * <p>
 * 加锁和解锁
 *
 * @author wangguangwu
 */
public interface Lock {

    /**
     * 获取显式锁
     *
     * @throws InterruptedException 获取锁的过程中中断
     */
    void lock() throws InterruptedException;

    /**
     * 解锁
     */
    void unLock();

}
