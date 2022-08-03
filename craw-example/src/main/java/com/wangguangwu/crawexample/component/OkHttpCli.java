package com.wangguangwu.crawexample.component;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author wangguangwu
 */
@SuppressWarnings("unused")
@Slf4j
@Component
public class OkHttpCli {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static final MediaType XML = MediaType.parse("application/xml; charset=utf-8");

    private static final String POST_LOG = "do post request and url[{}]";

    @Resource
    private OkHttpClient okHttpClient;

    /**
     * get 请求
     *
     * @param url 请求url地址
     * @return string
     */
    public String doGet(String url) {
        return doGet(url, null, null);
    }

    /**
     * get 请求
     *
     * @param url    请求url地址
     * @param params 请求参数 map
     * @return string
     */
    public String doGet(String url, Map<String, String> params) {
        return doGet(url, params, null);
    }

    /**
     * get 请求
     *
     * @param url     请求url地址
     * @param headers 请求头字段 {k1, v1 k2, v2, ...}
     * @return string
     */
    public String doGet(String url, String[] headers) {
        return doGet(url, null, headers);
    }

    /**
     * get 请求
     *
     * @param url     请求url地址
     * @param params  请求参数 map
     * @param headers 请求头字段 {k1, v1 k2, v2, ...}
     * @return string
     */
    public String doGet(String url, Map<String, String> params, String[] headers) {
        // 获取查询字符串
        String queryString = getQueryString(params);
        Request.Builder builder = new Request.Builder().url(url + queryString);
        // 添加请求头
        Request request = addReqHeader(builder, headers);
        log.info("do get request and url[{}]", queryString);
        return execute(request);
    }

    /**
     * 获取查询字符串
     *
     * @param params 请求参数 map
     * @return 查询字符串
     */
    private String getQueryString(Map<String, String> params) {
        if (CollectionUtils.isEmpty(params)) {
            return "";
        }
        StringBuilder queryString = new StringBuilder();
        boolean firstFlag = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (firstFlag) {
                queryString.append("?").append(entry.getKey()).append("=").append(entry.getValue());
                firstFlag = false;
            } else {
                queryString.append("&").append(entry.getKey()).append("=").append(params.get(entry.getValue()));
            }
        }
        return queryString.toString();
    }

    /**
     * 添加请求头
     *
     * @param builder Request.Builder
     * @param headers 请求头
     * @return Request
     */
    private Request addReqHeader(Request.Builder builder, String[] headers) {
        if (headers == null || headers.length == 0) {
            return builder.build();
        }
        int multiple = 2;
        Assert.isTrue(headers.length % multiple == 0, "headerS's length[" + headers.length + "] is error.");
        for (int i = 0; i < headers.length; i += multiple) {
            builder.addHeader(headers[i], headers[i + 1]);
        }
        return builder.build();
    }

    /**
     * post 请求
     *
     * @param url    请求url地址
     * @param params 请求参数 map
     * @return string
     */
    public String doPost(String url, Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        if (!CollectionUtils.isEmpty(params)) {
            params.forEach(builder::add);
        }
        Request request = new Request.Builder().url(url).post(builder.build()).build();
        log.info(POST_LOG, url);
        return execute(request);
    }

    /**
     * post 请求, 请求数据为 json 的字符串
     *
     * @param url  请求 url 地址
     * @param json 请求数据，json 字符串
     * @return string
     */
    public String doPostJson(String url, String json) {
        log.info(POST_LOG, url);
        return executePost(url, json, JSON);
    }

    /**
     * post 请求, 请求数据为 xml 的字符串
     *
     * @param url 请求 url 地址
     * @param xml 请求数据， xml 字符串
     * @return string
     */
    public String doPostXml(String url, String xml) {
        log.info(POST_LOG, url);
        return executePost(url, xml, XML);
    }

    /**
     * 请求 url 对应的服务
     *
     * @param url         url
     * @param data        数据
     * @param contentType 类型
     * @return string
     */
    private String executePost(String url, String data, MediaType contentType) {
        RequestBody requestBody = RequestBody.create(data, contentType);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        return execute(request);
    }

    /**
     * 请求 url 对应的服务
     *
     * @param request request
     * @return string
     */
    private String execute(Request request) {
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                return body == null ? "" : body.string();
            }
        } catch (Exception e) {
            log.error("response error: {}", ExceptionUtils.getStackTrace(e));
        }
        return "";
    }

}
