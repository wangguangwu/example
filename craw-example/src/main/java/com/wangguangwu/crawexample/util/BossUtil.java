package com.wangguangwu.crawexample.util;

import com.wangguangwu.crawexample.entity.MyChromeDriver;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Boss 直聘中有个 zp_stoken 的 cookie
 * 需要每次获取
 *
 * @author wangguangwu
 */
public class BossUtil {

    private static final String KEY = "__zp_stoken__";

    private static final int MAX_RETRY_TIMES = 3;

    public static String getZpStoken(String url) {
        String zpStoken;
        ChromeDriver chromeDriver;
        try (MyChromeDriver myChromeDriver = new MyChromeDriver()) {
            chromeDriver = myChromeDriver.initChromeDriver();
            zpStoken = getZpStoken(url, 1, chromeDriver);
        }
        return zpStoken;
    }

    private static String getZpStoken(String url, int retryTimes, ChromeDriver chromeDriver) {
        if (retryTimes > MAX_RETRY_TIMES) {
            return "";
        }
        // 访问 url
        chromeDriver.get(url);

        // 从所有响应的 cookie 中获取 zp_stoken
        List<String> cookies = chromeDriver.manage().getCookies()
                .stream()
                .filter(cookie -> KEY.equals(cookie.getName()))
                .map(Cookie::getValue)
                .collect(Collectors.toList());
        retryTimes += 1;
        return CollectionUtils.isEmpty(cookies) ?
                // 递归调用，确保能获取到值
                getZpStoken(url, retryTimes, chromeDriver) : KEY + "=" + cookies.get(0) + ";";
    }

    private BossUtil() {
    }

}
