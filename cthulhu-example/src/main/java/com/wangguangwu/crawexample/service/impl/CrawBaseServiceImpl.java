package com.wangguangwu.crawexample.service.impl;

import com.wangguangwu.crawexample.entity.HttpOkHttp;
import com.wangguangwu.crawexample.service.CrawBaseService;
import com.wangguangwu.crawexample.util.BossUtil;
import org.springframework.stereotype.Service;

/**
 * @author wangguangwu
 */
@Service
public class CrawBaseServiceImpl implements CrawBaseService {

    @Override
    public String[] constructHeaders(String url) {
        // 获取 Boss 直聘的 cookie
        String zpStoken = BossUtil.getZpStoken(url);
        // 拼接 http 请求头
        String[] cookie = new String[]{"Cookie", zpStoken};
        String[] httpHeaders = HttpOkHttp.getHttpHeaders().toArray(new String[0]);
        String[] headers = new String[cookie.length + httpHeaders.length];
        System.arraycopy(cookie, 0, headers, 0, cookie.length);
        System.arraycopy(httpHeaders, 0, headers, cookie.length, httpHeaders.length);
        return headers;
    }

}
