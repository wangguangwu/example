package com.wangguangwu.readwritelock;

import static java.lang.Thread.currentThread;

/**
 * @author wangguangwu
 */
public class Test {

    private static final String content = "Thisistheexampleforreadwritelock";

    public static void main(String[] args) {
        // 定义共享数据
        final ShareData shareData = new ShareData(50);
        // 创建 2 个线程进行写操作
        int writeCount = 2;
        for (int i = 0; i < writeCount; i++) {
            new Thread(() -> {
                for (int j = 0; j < content.length(); j++) {
                    try {
                        char c = content.charAt(j);
                        shareData.write(c);
                        System.out.println(Thread.currentThread().getName() + "[write]" + c);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        // 创建 10 个线程进行数据读操作
        int readCount = 10;
        for (int i = 0; i < readCount; i++) {
            new Thread(() -> {
                while (true) {
                    try {
                        System.out.println(currentThread() + "[read]" + new String(shareData.read()));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
