package com.example.threadexample.com.wangguangwu.threadsafe;

import org.springframework.util.Assert;

import java.util.Map;
import java.util.concurrent.*;

/**
 * ConcurrentHashMap and synchronized and thread safe
 * <p>
 * 从测试结果可以看出，不是每次执行结果都为 100
 * <p>
 * ConcurrentHashMap 的线程安全指的是：
 * 它的每个方法单独调用（即原子操作）都是线程安全的，但代码总体的互斥性并不受控制
 *
 * @author wangguangwu
 */
public class TestConcurrentHashMap1 {

    private static final int TEST_TIMES = 10;

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < TEST_TIMES; i++) {
            System.out.println("测试结果：" + test());
        }
    }

    private static int test() throws InterruptedException {
        Map<String, Integer> map = new ConcurrentHashMap<>(100);
        ExecutorService threadPool = Executors.newCachedThreadPool();
        for (int i = 0; i < TEST_TIMES; i++) {
            threadPool.execute(new MyTask(map));
        }
        // 销毁 ExecutorService
        // 它会让 ExecutorService 停止接收新的任务，并且等到现有任务全部执行完毕后再销毁
        // 在调用 shutdown() 方法后，已经添加的任务会继续执行，但是不会阻塞，所以 main 线程会先执行
        threadPool.shutdown();
        boolean success = threadPool.awaitTermination(1, TimeUnit.HOURS);
        Assert.isTrue(success, "线程池关闭失败");

        return map.get(MyTask.KEY);
    }

    static class MyTask implements Runnable {

        public static final String KEY = "key";

        private final Map<String, Integer> map;

        public MyTask(Map<String, Integer> map) {
            this.map = map;
        }

        @Override
        public void run() {
            for (int i = 0; i < TEST_TIMES; i++) {
                this.addUp();
            }
        }

        private void addUp() {
            if (!map.containsKey(KEY)) {
                map.put(KEY, 1);
            } else {
                // 这一行代码其实分为 3 步
                // 1. map.get(key)
                // 2. +1
                // 3. map.put(value)
                // 其中第 1 步 和第 3 步，因为使用了 ConcurrentHashMap，单独来说都是线程安全的
                // 但在上面的代码中，map 本身是一个共享变量，当线程 A 执行 map.get 时，其他线程可能正在执行 map.put
                // 这样，当线程 A 执行到 map.put 时，此时线程 A 中的值就已经是脏数据了，然后脏数据覆盖了真值，导致线程不安全
                // In a word，ConcurrentHashMap 保证调用 get 方法时获取到的是此时的真值，但无法保证在调用 put 方法时，当前的获取到的值依旧是真值
                map.put(KEY, map.get(KEY) + 1);
            }
        }
    }

}

