package com.wangguangwu.cache1;

import lombok.Data;

import java.lang.ref.SoftReference;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 使用 map 在内存中维护一个简单的缓存
 *
 * @author wangguangwu
 */
public class CacheDemo {

    /**
     * 设计过期时间
     */
    private static final int CLEAN_UP_PERIOD_IN_SEC = 5;

    /**
     * 使用 ConcurrentHashMap 存储数据，保证线程安全
     */
    private final ConcurrentHashMap<String, SoftReference<Cache>> cacheMap = new ConcurrentHashMap<>();

    /**
     * 创建一个守护线程，定时清理缓存中的数据
     */
    public CacheDemo() {
        Thread cleanerThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep((long) CLEAN_UP_PERIOD_IN_SEC * 1000);
                    cacheMap.entrySet().removeIf(entry ->
                            Optional.ofNullable(entry.getValue())
                                    .map(SoftReference::get)
                                    .map(Cache::isExpired)
                                    .orElse(false));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        cleanerThread.setDaemon(true);
        cleanerThread.start();
    }

    public void add(String key, Object value, long periodInMills) {
        if (key == null) {
            return;
        }
        if (value == null) {
            cacheMap.remove(key);
        } else {
            long expiryTime = System.currentTimeMillis() + periodInMills;
            cacheMap.put(key, new SoftReference<>(new Cache(value, expiryTime)));
        }
    }

    public void remove(String key) {
        cacheMap.remove(key);
    }

    public Object get(String key) {
        return Optional.ofNullable(cacheMap.get(key))
                .map(SoftReference::get)
                .filter(cache -> !cache.isExpired())
                .map(Cache::getValue)
                .orElse(null);
    }

    public void clear() {
        cacheMap.clear();
    }

    public long size() {
        return cacheMap.entrySet()
                .stream()
                .filter(entry -> Optional.ofNullable(
                                entry.getValue())
                        .map(SoftReference::get)
                        .map(cache -> !cache.isExpired())
                        .orElse(false))
                .count();
    }


    /**
     * 缓存对象 cache
     */
    @Data
    private static class Cache {

        private Object value;

        private long expiryTime;

        private Cache(Object value, long expiryTime) {
            this.value = value;
            this.expiryTime = expiryTime;
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }

    }

}
