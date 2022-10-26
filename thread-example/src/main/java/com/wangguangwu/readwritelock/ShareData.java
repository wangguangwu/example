package com.wangguangwu.readwritelock;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author wangguangwu
 */
public class ShareData {

    /**
     * 定义共享资源
     */
    private final List<Character> container = new ArrayList<>();

    /**
     * 构建 ReadWriteLock
     */
    private final ReadWriteLock readWriteLock = ReadWriteLock.readWriteLock();

    /**
     * 创建读取锁
     */
    private final Lock readLock = readWriteLock.readLock();

    /**
     * 创建写入锁
     */
    private final Lock writeLock = readWriteLock.writeLock();

    private final int length;

    public ShareData(int length) {
        this.length = length;
        // 避免读取数据为空
        for (int i = 0; i < length; i++) {
            container.add(i, 'c');
        }
    }

    public char[] read() throws InterruptedException {
        // 加读锁
        readLock.lock();
        try {
            char[] newBuffer = new char[length];
            for (int i = 0; i < length; i++) {
                newBuffer[i] = container.get(i);
            }
            slowly();
            return newBuffer;
        } finally {
            // 解读锁
            readLock.unLock();
        }
    }

    public void write(char c) throws InterruptedException {
        // 加写锁
        writeLock.lock();
        try {
            for (int i = 0; i < length; i++) {
                this.container.add(i, c);
            }
            slowly();
        } finally {
            writeLock.unLock();
        }
    }

    /**
     * 模拟耗时
     */
    private void slowly() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            // ignore
        }
    }
}
