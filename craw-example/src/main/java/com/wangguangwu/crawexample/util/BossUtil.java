package com.wangguangwu.crawexample.util;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Boss 直聘中有个 zp_stoken 的 cookie
 * 需要每次获取
 *
 * @author wangguangwu
 */
public class BossUtil {

    private static final ChromeDriver CHROME_DRIVER;

    private static final String KEY = "__zp_stoken__";

    private static final int MAX_RETRY_TIMES = 3;

    static {
        // 获取环境变量
        Map<String, String> map = System.getenv();
        String driverPath = Optional.ofNullable(map.get("HOME")).orElse("/Users/wangguangwu")
                + File.separator + "Desktop/chromedriver";
        System.setProperty("webdriver.chrome.driver", driverPath);
        CHROME_DRIVER = new ChromeDriver();
    }

    public static String getZpStoken(String url) {
        return getZpStoken(url, 1);
    }

    private static String getZpStoken(String url, int retryTimes) {
        if (retryTimes > MAX_RETRY_TIMES) {
            return "";
        }
        // 访问 url
        CHROME_DRIVER.get(url);

        // 从所有响应的 cookie 中获取 zp_stoken
        List<String> cookies = CHROME_DRIVER.manage().getCookies()
                .stream()
                .filter(cookie -> KEY.equals(cookie.getName()))
                .map(Cookie::getValue)
                .collect(Collectors.toList());
        retryTimes += 1;
        return CollectionUtils.isEmpty(cookies) ?
                // 递归调用，确保能获取到值
                getZpStoken(url, retryTimes) : KEY + "=" + cookies.get(0) + ";";
    }

    private BossUtil() {
    }

}
