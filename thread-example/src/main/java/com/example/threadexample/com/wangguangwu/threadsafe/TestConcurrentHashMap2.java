package com.example.threadexample.com.wangguangwu.threadsafe;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 使用 synchronized 关键字修饰操作 ConcurrentHashMap 的方法
 * <p>
 * 测试结果依旧不是每次都为 100
 *
 * @author wangguangwu
 */
public class TestConcurrentHashMap2 {

    private static final int TEST_TIMES = 10;

    public static void main(String[] args) {
        for (int i = 0; i < TEST_TIMES; i++) {
            System.out.println("测试结果：" + test());
        }
    }

    private static int test() {
        Map<String, Integer> map = new ConcurrentHashMap<>(100);
        ExecutorService threadPool = Executors.newCachedThreadPool();
        for (int i = 0; i < TEST_TIMES; i++) {
            threadPool.execute(new MyTask(map));
        }
        threadPool.shutdown();
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
                addUp();
            }
        }

        /**
         * 使用 synchronized 关键字修饰操作 ConcurrentHashMap 的方法
         * <p>
         * 为什么使用 synchronized 关键字加锁还是无法保证线程安全
         * <p>
         * synchronized 关键字的本质是什么？
         * <p>
         * synchronized 关键字可以用来修饰方法和代码块，无论是哪一种，本质上都是锁定某一个对象。
         * 修饰方法时，锁上的是调用这个方法的对象，即 this；
         * 修饰代码块时，锁上的是代码块这整段代码对象。
         * <p>
         * 此时我们修饰的是方法，那么它锁定的对象就是调用者，即 MyTask 对象本身。
         * 在每一个线程中，MyTask 对象都是独立的，这就导致实际每个线程都是对自己的 MyTask 进行锁定，而不会干涉其他线程的 MyTask 对象。
         * In a words，此时上锁没有意义。
         */
        private synchronized void addUp() {
            if (map.containsKey(KEY)) {
                // 非原子性操作
                map.put(KEY, map.get(KEY) + 1);
            } else {
                map.put(KEY, 1);
            }
        }
    }

}
