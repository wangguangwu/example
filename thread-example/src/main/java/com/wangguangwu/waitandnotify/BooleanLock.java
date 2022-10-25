package com.wangguangwu.waitandnotify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

import static java.lang.Thread.currentThread;

/**
 * @author wangguangwu
 */
public class BooleanLock implements Lock {

    /**
     * 当前拥有锁的线程
     */
    private Thread currentThread;

    /**
     * 标识符
     * <p>
     * true：当前锁被某个线程占有；
     * false：当前锁没有被任何线程获得或者已经释放
     */
    private boolean locked = false;

    /**
     * 存储哪些线程在获取锁时进入了阻塞状态
     */
    private final List<Thread> blockedList = new ArrayList<>();

    @Override
    public void lock() throws InterruptedException {
        synchronized (this) {
            // 当前线程
            Thread thread = Thread.currentThread();
            while (locked) {
                try {
                    if (!blockedList.contains(thread)) {
                        // 如果当前锁被其他线程获得，当前线程将加入阻塞队列
                        blockedList.add(thread);
                    }
                    // 释放当前线程对于 this monitor 的所有权
                    this.wait();
                } catch (InterruptedException e) {
                    // 如果当前线程在 wait 时被中断，则从 blockedList 中将其删除
                    // 避免内存泄漏
                    blockedList.remove(thread);
                    // 抛出异常
                    throw e;
                }
            }
            // 获得锁成功
            // 将当前线程从阻塞列表中删除，即使当前线程不在阻塞列表中（锁从未被其他线程持有过）也没有关系
            blockedList.remove(thread);
            this.locked = true;
            this.currentThread = thread;
        }
    }

    @Override
    public void lock(long mills) throws InterruptedException, TimeoutException {
        synchronized (this) {
            if (mills <= 0) {
                // 超时时间设定为小于等于 0，说明要一直阻塞
                this.lock();
            } else {
                long remainingMills = mills;
                long endMills = System.currentTimeMillis() + mills;
                // 当前线程
                Thread thread = Thread.currentThread();
                while (locked) {
                    // 当前线程被其他线程唤醒或者在指定的 wait 时间到了之后还没有获得锁
                    if (remainingMills <= 0) {
                        throw new TimeoutException("can not get the lock during [" + mills + "] ms");
                    }
                    if (!blockedList.contains(thread)) {
                        blockedList.add(thread);
                    }
                    this.wait(remainingMills);
                    // wait 是可中断方法
                    // 重新计算要等待的毫秒数
                    remainingMills = endMills - System.currentTimeMillis();
                }
                blockedList.remove(thread);
                this.locked = true;
                this.currentThread = thread;
            }
        }
    }

    @Override
    public void unlock() {
        synchronized (this) {
            // 必须是当前持有锁的线程才能解锁
            if (currentThread == Thread.currentThread()) {
                this.locked = false;
                Optional.ofNullable(currentThread().getName())
                        .ifPresent(name -> System.out.println(name + " release the lock monitor."));
                // 唤醒其他在 wait set 中的线程
                this.notifyAll();
            }
        }
    }

    @Override
    public List<Thread> listBlockedThreads() {
        return Collections.unmodifiableList(blockedList);
    }
}
