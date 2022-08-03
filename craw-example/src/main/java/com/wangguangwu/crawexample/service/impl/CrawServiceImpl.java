package com.wangguangwu.crawexample.service.impl;

import com.wangguangwu.crawexample.service.CrawService;
import com.wangguangwu.crawexample.util.BossUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

/**
 * @author wangguangwu
 */
@Slf4j
@Service
public class CrawServiceImpl implements CrawService {

    @Override
    public void wholeProcess() {
        StopWatch stopWatch = new StopWatch();
        // 爬取数据
        stopWatch.start("爬取数据");
        crawData();
        stopWatch.stop();
        // 数据解析
        stopWatch.start("数据解析");
        parseData();
        stopWatch.stop();
        // 保存数据
        stopWatch.start("保存数据");
        saveData();
        stopWatch.stop();
        log.info("爬取数据总耗时: {}", stopWatch.prettyPrint());
    }

    private void crawData() {
        // 获取 Boss 直聘的 cookie

    }

    private void parseData() {
        throw new UnsupportedOperationException();
    }

    private void saveData() {
        throw new UnsupportedOperationException();
    }

}
