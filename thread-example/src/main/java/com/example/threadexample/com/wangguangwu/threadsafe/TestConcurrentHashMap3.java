package com.example.threadexample.com.wangguangwu.threadsafe;

import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 使用 synchronized 修饰共享变量
 * <p>
 * 多次测试，结果都为 100
 *
 * @author wangguangwu
 */
public class TestConcurrentHashMap3 {

    private static final int TEST_TIMES = 10;

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < TEST_TIMES; i++) {
            System.out.println("测试结果：" + test());
        }
    }

    private static int test() throws InterruptedException {
        // 因为锁住了共享变量，此时不使用 ConcurrentHashMap，使用线程不安全的 HashMap 也可以实现一样的效果
        Map<String, Integer> map = new HashMap<>(100);
        ExecutorService threadPool = Executors.newCachedThreadPool();
        for (int i = 0; i < TEST_TIMES; i++) {
            threadPool.execute(new MyTask(map));
        }
        threadPool.shutdown();
        boolean success = threadPool.awaitTermination(1, TimeUnit.DAYS);
        Assert.isTrue(success, "线程池关闭失败");

        return map.get(MyTask.KEY);
    }

    static class MyTask implements Runnable {

        private static final String KEY = "key";

        private final Map<String, Integer> map;

        public MyTask(Map<String, Integer> map) {
            this.map = map;
        }

        @Override
        public void run() {
            for (int i = 0; i < TEST_TIMES; i++) {
                // 使用 synchronized 锁住共享变量
                synchronized (map) {
                    addUp();
                }
            }
        }

        private void addUp() {
            if (map.containsKey(KEY)) {
                map.put(KEY, map.get(KEY) + 1);
            } else {
                map.put(KEY, 1);
            }
        }
    }

}
