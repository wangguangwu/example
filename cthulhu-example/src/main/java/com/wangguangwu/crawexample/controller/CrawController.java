package com.wangguangwu.crawexample.controller;

import com.wangguangwu.crawexample.service.CrawService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author wangguangwu
 */
@Slf4j
@RequestMapping("/hello")
@ResponseBody
public class CrawController {

    @Resource
    private CrawService crawService;

    @GetMapping("/craw")
    public void testCraw(String url) {
        StopWatch stopWatch = new StopWatch();
        log.info("调用爬虫接口");
        stopWatch.start();
        crawService.wholeProcess(url);
        stopWatch.stop();
        log.info("爬虫接口运行总耗时: {} ms", stopWatch.getTotalTimeMillis());
    }

}
