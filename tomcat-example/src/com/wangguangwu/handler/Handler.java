package com.wangguangwu.handler;

import java.io.*;
import java.net.Socket;

/**
 * 请求处理程序
 *
 * @author wangguangwu
 */
public class Handler implements Runnable {

    private final Socket socket;
    private static String WEB_ROOT = "/Users/wangguangwu/workSpace/cthulhu-web";

    public Handler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (// 浏览器到服务端的输入流
             InputStream inputStream = socket.getInputStream();
             // 服务端到浏览器的输出流
             OutputStream outputStream = socket.getOutputStream();
             // 包装后的输入缓冲字符流
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
             // 包装后的输出缓冲字符流
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream))
        ) {
            // 请求
            StringBuffer request = new StringBuffer();
            String msg;
            while ((msg = reader.readLine()) != null && msg.length() > 0) {
                request.append(msg);
                request.append("\r\n");
            }
            System.out.println(request);
            String[] msgS = request.toString().split("\r");
            // .ico 是浏览器页面的图标文件
            // 是浏览器默认会向服务器发送的请求
            if (msgS[1].endsWith(".ico")) {
                writer.write("HTTP/1.1 200 OK");
                writer.println("Content-Type: text/html;charset=UTF-8");
            }
        } catch (IOException e) {
//            e.printStackTrace();
        }
    }

}
