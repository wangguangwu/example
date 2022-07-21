package com.wangguangwu.cache1;

import org.junit.Test;

/**
 * @author wangguangwu
 */
public class TestCacheDemo {

    @Test
    public void testCacheMap() throws InterruptedException {
        CacheDemo cacheDemo = new CacheDemo();
        cacheDemo.add("Hello1", "world", 5 * 1000);
        cacheDemo.add("Hello2", "china", 5 * 1000);
        cacheDemo.add("Hello3", "wangguangwu", 5 * 1000);
        System.out.println("从缓存中取出值:" + cacheDemo.get("Hello1"));
        System.out.println("size:" + cacheDemo.size());
        Thread.sleep(5000L);
        System.out.println("5秒钟过后");
        System.out.println("从缓存中取出值:" + cacheDemo.get("Hello1"));
        System.out.println("size:" + cacheDemo.size());
    }

}
