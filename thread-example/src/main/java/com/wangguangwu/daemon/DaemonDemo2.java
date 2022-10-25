package com.wangguangwu.daemon;

import java.util.concurrent.TimeUnit;

/**
 * JVM 中没有非守护线程运行，JVM 就正常退出
 * <p>
 * In a word，守护线程拥有自动结束自己生命周期的特性，而非守护线程不具有这个特性。
 *
 * @author wangguangwu
 */
public class DaemonDemo2 {

    public static void main(String[] args) throws InterruptedException {
        // 设置一个钩子线程，在 JVM 退出时输出日志
        Runtime.getRuntime()
                .addShutdownHook(new Thread(() -> System.out.println("JVM exists")));

        // 在主线程中 new 一个线程
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    // 睡眠 1 s
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println("Hello World");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // 将该线程设置为守护线程
        thread.setDaemon(true);

        // 启动线程
        thread.start();

        TimeUnit.SECONDS.sleep(3);

        // 主线程退出
        System.out.println("Main ready to exit");
    }

}
