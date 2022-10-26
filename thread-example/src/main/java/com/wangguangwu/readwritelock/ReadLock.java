package com.wangguangwu.readwritelock;

/**
 * @author wangguangwu
 */
public class ReadLock implements Lock {

    private final ReadWriteLockImpl readWriteLock;

    public ReadLock(ReadWriteLockImpl readWriteLock) {
        this.readWriteLock = readWriteLock;
    }

    @Override
    public void lock() throws InterruptedException {
        // 同步锁
        synchronized (readWriteLock.getMutex()) {
            // 有线程在进行写操作
            // 有写线程在等待并且偏向写锁的标识为 true
            while (readWriteLock.getWritingWriters() > 0
                    || (readWriteLock.getPreferWriter()
                    && readWriteLock.getWaitingWriters() > 0)) {
                // 挂起
                readWriteLock.getMutex().wait();
            }
            // 成功获取读锁，并且使 readingReaders 的数量增加
            readWriteLock.incrementReadingReaders();
        }
    }

    @Override
    public void unLock() {
        // 同步锁
        synchronized (readWriteLock.getMutex()) {
            // 释放锁的过程就是使当前 reading 的数量减一
            readWriteLock.decrementReadingReaders();
            // 将 preferWriter 设置为 true，可以使得 writer 线程获得更多的机会
            readWriteLock.changePrefer(true);
            // 通知唤醒与 Mutex 关联 monitor waitset 中的线程
            readWriteLock.getMutex().notifyAll();
        }
    }
}
