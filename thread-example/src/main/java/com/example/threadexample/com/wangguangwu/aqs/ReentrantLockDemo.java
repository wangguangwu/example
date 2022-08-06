package com.example.threadexample.com.wangguangwu.aqs;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wangguangwu
 */
public class ReentrantLockDemo {

    /**
     * 独占式锁
     * 同时也是可重入锁
     * <p>
     * 可重入锁：可以对这个锁的对象多次执行 lock() 加锁和 unlock() 释放锁 ，即可以对一个锁加多次。
     */
    private static final ReentrantLock LOCK = new ReentrantLock();

    public static void main(String[] args) {

        int times = 10;
        for (int i = 0; i < times; i++) {
            Thread thread = new Thread(() -> {
                LOCK.lock();
                try {
                    Thread.sleep(3000);
                    System.out.println("Hello world");
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                    System.out.println("Hello error");
                    Thread.currentThread().interrupt();
                } finally {
                    LOCK.unlock();
                }
            });
            thread.start();
        }
        System.out.println("Hello wangguangwu");
    }

}
