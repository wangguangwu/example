package com.wangguangwu.crawexample.component;

import com.wangguangwu.crawexample.entity.HttpOkHttp;
import com.wangguangwu.crawexample.util.BossUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author wangguangwu
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestOkHttpCli {

    @Resource
    private OkHttpCli okHttpCli;

    @Test
    public void testAccessBaidu() {
        String result = okHttpCli.doGet("https://www.baidu.com");
        Assert.assertNotNull(result);
        System.out.println(result);
    }

    @Test
    public void testAccessBoss() {
        String url = "https://www.zhipin.com/job_detail/?query=java&city=101210100&industry=&position=";
        String zpStoken = BossUtil.getZpStoken(url);
        String[] cookie = new String[]{"Cookie", zpStoken};
        List<String> httpHeadersList = HttpOkHttp.getHttpHeaders();
        String[] httpHeaders = httpHeadersList.toArray(new String[0]);
        String[] headers = new String[cookie.length + httpHeaders.length];
        System.arraycopy(cookie, 0, headers, 0, cookie.length);
        System.arraycopy(httpHeaders, 0, headers, cookie.length, httpHeaders.length);
        String result = okHttpCli.doGet(url, headers);
        Assert.assertNotNull(result);
        System.out.println(result);
    }
}
