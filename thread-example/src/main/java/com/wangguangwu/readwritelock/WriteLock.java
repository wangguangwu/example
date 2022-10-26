package com.wangguangwu.readwritelock;

/**
 * @author wangguangwu
 */
public class WriteLock implements Lock {

    private final ReadWriteLockImpl readWriteLock;

    public WriteLock(ReadWriteLockImpl readWriteLock) {
        this.readWriteLock = readWriteLock;
    }

    @Override
    public void lock() throws InterruptedException {
        // 同步锁
        synchronized (readWriteLock.getMutex()) {
            try {
                // 首先使等待写入锁的数字加一
                readWriteLock.incrementWaitingWriters();
                // 如果此时有其他线程正在进行读操作，或者写操作，那么当前线程将被挂起
                while (readWriteLock.getReadingReaders() > 0
                        || readWriteLock.getWritingWriters() > 0) {
                    // 挂起当前线程
                    readWriteLock.getMutex().wait();
                }
            } finally {
                // 成功获取到了写入锁，使得等待获取写入锁的计数器减一
                this.readWriteLock.decrementWaitingWriters();
            }
            // 将正在写入的线程数量加一
            readWriteLock.incrementWritingWriters();
        }
    }

    @Override
    public void unLock() {
        // 同步锁
        synchronized (readWriteLock.getMutex()) {
            // 将正在写入锁的线程数量减一
            readWriteLock.decrementWritingWriters();
            // 将偏好状态修改为 false，使得读锁最快地获取
            readWriteLock.changePrefer(false);
            // 唤醒其他在 Mutex monitor waitset 中的线程
            readWriteLock.getMutex().notifyAll();
        }
    }
}
