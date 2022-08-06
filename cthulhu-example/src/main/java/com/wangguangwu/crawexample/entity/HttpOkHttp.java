package com.wangguangwu.crawexample.entity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author wangguangwu
 */
public final class HttpOkHttp {

    private static final String[] HTTP_HEADERS =
            new String[]{"User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.51 Safari/537.36", "Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8", "Accept-Language", "en-US,en;q=0.5", "Connection", "Keep-Alive", "Upgrade-Insecure-Requests", "1", "Sec-Fetch-Dest", "document", "Sec-Fetch-Mode", "navigate", "Sec-Fetch-Site", "none", "Sec-Fetch-User", "?1"};

    /**
     * public static 变量可以被修改
     * 数组又是可变字段，final 可以阻止重新分配变量，但是无法保证数组内部数据不会被改变
     *
     * @return list
     */
    public static List<String> getHttpHeaders() {
        return Collections.unmodifiableList(Arrays.asList(HTTP_HEADERS));
    }

    private HttpOkHttp() {
    }

}
