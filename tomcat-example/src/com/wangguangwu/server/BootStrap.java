package com.wangguangwu.server;

import com.wangguangwu.handler.Handler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author wangguangwu
 */
public class BootStrap {

    public static void main(String[] args) {
        BootStrap bootStrap = new BootStrap();
        bootStrap.await();
    }

    private void await() {
        try {
            ServerSocket serverSocket = new ServerSocket(8080, 1);
            while (true) {
                Socket socket = serverSocket.accept();
                Thread thread = new Thread(new Handler(socket));
                System.out.println("收到新请求");
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
