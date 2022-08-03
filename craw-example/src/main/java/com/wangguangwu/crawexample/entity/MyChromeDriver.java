package com.wangguangwu.crawexample.entity;

import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.util.Map;
import java.util.Optional;

/**
 * @author wangguangwu
 */
public class MyChromeDriver implements AutoCloseable {

    private ChromeDriver chromeDriver;

    public ChromeDriver initChromeDriver() {
        // 获取环境变量
        Map<String, String> map = System.getenv();
        String driverPath = Optional.ofNullable(map.get("HOME")).orElse("/Users/wangguangwu")
                + File.separator + "Desktop/chromedriver";
        System.setProperty("webdriver.chrome.driver", driverPath);
        chromeDriver = new ChromeDriver();
        return chromeDriver;
    }

    @Override
    public void close() {
        chromeDriver.quit();
    }

}
