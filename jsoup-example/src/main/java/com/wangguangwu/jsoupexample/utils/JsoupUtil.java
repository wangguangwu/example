package com.wangguangwu.jsoupexample.utils;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;

/**
 * @author wangguangwu
 */
@Slf4j
public final class JsoupUtil {

    public static Document jsoupHtml(String html) {
        return Jsoup.parse(html);
    }

    public static Document jsoupFile(String FileLocation, String url) {
        File file = new File(FileLocation);
        Document document = null;
        try {
            document = Jsoup.parse(file, "UTF-8", url);
        } catch (IOException e) {
            log.error("获取数据出错：{}", e.getMessage());
        }
        return document;
    }

    /**
     * 从 url 中加载一个 Document
     *
     * @param url url
     * @return Document
     */
    public static Document jsoupUrl(String url) {
        // 建立连接
        Connection connection = Jsoup.connect(url);
        // 获取网页数据
        Document document = null;
        try {
            document = connection.get();
        } catch (IOException e) {
            log.error("获取数据出错：{}", e.getMessage());
        }
        return document;
    }

    private JsoupUtil() {
    }
}
