package com.wangguangwu.crawexample.service;

/**
 * @author wangguangwu
 */
public interface CrawBaseService {

    /**
     * 构建 http 请求头
     *
     * @param url url
     * @return headers
     */
    String[] constructHeaders(String url);

}
