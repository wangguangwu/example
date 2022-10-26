package com.wangguangwu.readwritelock;

/**
 * 创建读锁和写锁
 *
 * @author wangguangwu
 */
public interface ReadWriteLock {

    /**
     * 创建读锁
     *
     * @return read lock
     */
    Lock readLock();

    /**
     * 创建写锁
     *
     * @return write lock
     */
    Lock writeLock();

    /**
     * 当前有多少线程正在执行写操作
     * <p>
     * 最多是 1 个
     *
     * @return the count of writing threads
     */
    int getWritingWriters();

    /**
     * 当前有多少线程正在等待获取写入锁
     *
     * @return the count of waiting to write threads
     */
    int getWaitingWriters();

    /**
     * 当前有多少线程正在进行读操作
     *
     * @return the count of reading threads
     */
    int getReadingReaders();

    /**
     * 工厂方法，创建 ReadWriteLock
     *
     * @return ReadWriteLock
     */
    static ReadWriteLock readWriteLock() {
        return new ReadWriteLockImpl();
    }

    /**
     * 工厂方法，创建 ReadWriteLock
     *
     * @param preferWriter preferWriter
     * @return ReadWriteLock
     */
    static ReadWriteLock readWriteLock(boolean preferWriter) {
        return new ReadWriteLockImpl(preferWriter);
    }
}
