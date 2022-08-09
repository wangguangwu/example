package com.example.threadexample.com.wangguangwu.daemon;

import java.util.concurrent.TimeUnit;

/**
 * 守护线程 demo
 * <p>
 * JVM 程序什么时候会退出？
 * <p>
 * The Java Virtual Machines exists when the only threads running all daemon threads.
 * 即 当 JVM 中不存在任何一个正在运行的非守护线程时，则 JVM 进程退出。
 * <p>
 * <p>
 * 主线程退出后，因为非守护线程还在运行，所以 JVM 进程没有退出
 *
 * @author wangguangwu
 */
public class DaemonDemo1 {

    public static void main(String[] args) throws InterruptedException {
        // new 一个非守护线程
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    // 线程睡眠 1 s
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println("Hello World");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // 启动线程
        thread.start();

        TimeUnit.SECONDS.sleep(3);

        // 主线程即将退出
        System.out.println("Main ready to exit");
    }

}


