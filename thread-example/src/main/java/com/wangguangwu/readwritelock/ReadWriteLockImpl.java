package com.wangguangwu.readwritelock;

/**
 * @author wangguangwu
 */
public class ReadWriteLockImpl implements ReadWriteLock {

    /**
     * 共享资源
     */
    private final Object mutex = new Object();

    /**
     * 当前有多少个线程正在写入
     */
    private int writingWriters;

    /**
     * 当前有多少个线程正在等待写入
     */
    private int waitingWriters;

    /**
     * 当前有多少个线程正在 read
     */
    private int readingReaders;

    /**
     * read 和 write 的偏好设置
     */
    private boolean preferWriter;

    public ReadWriteLockImpl() {
        this(true);
    }

    public ReadWriteLockImpl(boolean preferWriter) {
        this.preferWriter = preferWriter;
    }

    @Override
    public Lock readLock() {
        return new ReadLock(this);
    }

    @Override
    public Lock writeLock() {
        return new WriteLock(this);
    }

    @Override
    public int getWritingWriters() {
        return this.writingWriters;
    }

    @Override
    public int getWaitingWriters() {
        return this.waitingWriters;
    }

    @Override
    public int getReadingReaders() {
        return this.readingReaders;
    }

    public Object getMutex() {
        return mutex;
    }

    boolean getPreferWriter() {
        return this.preferWriter;
    }

    void changePrefer(boolean preferWriter) {
        this.preferWriter = preferWriter;
    }

    void incrementWritingWriters() {
        this.writingWriters++;
    }

    void incrementWaitingWriters() {
        this.waitingWriters++;
    }

    void incrementReadingReaders() {
        this.readingReaders++;
    }

    void decrementWritingWriters() {
        this.writingWriters--;
    }

    void decrementWaitingWriters() {
        this.waitingWriters--;
    }

    void decrementReadingReaders() {
        this.readingReaders--;
    }
}
