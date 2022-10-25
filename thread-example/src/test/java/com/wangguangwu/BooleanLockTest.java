package com.wangguangwu;

import com.wangguangwu.waitandnotify.BooleanLock;
import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.IntStream;

/**
 * @author wangguangwu
 */
public class BooleanLockTest {

    /**
     * 定义 BooleanLock
     */
    private final BooleanLock lock = new BooleanLock();

    @Test
    public void testSyncMethod() throws InterruptedException {
        BooleanLockTest lockTest = new BooleanLockTest();
        IntStream.range(0, 10)
                .mapToObj(i -> new Thread(lockTest::syncMethod))
                .forEach(Thread::start);
        // 单元测试中需要设置
        // 让主线程进行等待
        Thread.currentThread().join(100000);
    }

    @Test
    public void testInterrupt() throws InterruptedException {
        BooleanLockTest lockTest = new BooleanLockTest();
        new Thread(lockTest::syncMethod, "T1").start();
        TimeUnit.MILLISECONDS.sleep(2);
        Thread t2 = new Thread(lockTest::syncMethod, "T2");
        t2.start();
        TimeUnit.MILLISECONDS.sleep(10);
        // 在启动线程 t2 10 ms 后，主动将其中断
        // t2 线程会收到中断信号
        t2.interrupt();
        // 单元测试中需要设置
        // 让主线程进行等待
        Thread.currentThread().join(1000);
    }

    @Test
    public void testSyncAndInterrupt() throws InterruptedException {
        BooleanLockTest lockTest = new BooleanLockTest();
        new Thread(lockTest::syncMethodTimeoutAble, "T1").start();
        TimeUnit.MILLISECONDS.sleep(2);
        Thread t2 = new Thread(lockTest::syncMethodTimeoutAble, "T2");
        t2.start();
        // 单元测试中需要设置
        // 让主线程进行等待
        Thread.currentThread().join(10000);
    }

    public void syncMethod() {
        // 使用 try...finally 写法确保 lock 每次都能被正确释放
        try {
            // 加锁
            lock.lock();
            int randomInt = ThreadLocalRandom.current().nextInt(10);
            System.out.println(Thread.currentThread().getName() + " get the lock.");
            TimeUnit.SECONDS.sleep(randomInt);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } finally {
            // 释放锁
            lock.unlock();
        }
    }

    public void syncMethodTimeoutAble() {
        try {
            lock.lock(1000);
            System.out.println(Thread.currentThread().getName() + " get the lock.");
            int randomInt = ThreadLocalRandom.current().nextInt(10);
            TimeUnit.SECONDS.sleep(randomInt);
        } catch (InterruptedException | TimeoutException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

}
