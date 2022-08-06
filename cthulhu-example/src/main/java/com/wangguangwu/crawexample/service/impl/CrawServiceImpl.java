package com.wangguangwu.crawexample.service.impl;

import com.wangguangwu.crawexample.component.OkHttpCli;
import com.wangguangwu.crawexample.service.CrawBaseService;
import com.wangguangwu.crawexample.service.CrawService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;


/**
 * @author wangguangwu
 */
@Slf4j
@Service
public class CrawServiceImpl implements CrawService {

    @Resource
    private OkHttpCli okHttpCli;

    @Resource
    private CrawBaseService crawBaseService;

    @Override
    public void wholeProcess(String url) {
        StopWatch stopWatch = new StopWatch();
        // 爬取数据
        stopWatch.start("爬取数据");
        String html = crawData(url);
        stopWatch.stop();
        // 数据解析
        stopWatch.start("数据解析");
        parseHtml(html);
        stopWatch.stop();
        // 保存数据
        stopWatch.start("保存数据");
        saveData();
        stopWatch.stop();
        log.info("爬取数据总耗时: {}", stopWatch.prettyPrint());
    }

    /**
     * 爬取 boss 直聘数据
     *
     * @param url url
     */
    private String crawData(String url) {
        // 构建请求头
        String[] headers = crawBaseService.constructHeaders(url);
        // 访问 boss 直聘
        return okHttpCli.doGet(url, headers);
    }

    private void parseHtml(String html) {
        throw new UnsupportedOperationException();
    }

    private void saveData() {
        throw new UnsupportedOperationException();
    }

}
