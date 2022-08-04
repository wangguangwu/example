package com.wangguangwu.crawexample.util;

import com.alibaba.fastjson2.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangguangwu
 */
class TestBossUtil {

    @Test
    void testGetZpStoken() {
        String zpStoken1 = BossUtil.getZpStoken("https://www.zhipin.com/c101210100/?query=java&page=2&ka=page-1");
        Assertions.assertFalse(StringUtils.isBlank(zpStoken1), "获取 zpStoken 失败");
        System.out.println("Boss token\r\n" + zpStoken1);

        String zpStoken2 = BossUtil.getZpStoken("https://www.zhipin.com/c101210100/?query=java&page=2&ka=page-1");
        Assertions.assertFalse(StringUtils.isBlank(zpStoken2), "获取 zpStoken 失败");
        System.out.println("Boss token\r\n" + zpStoken2);
    }

    @Test
    void testJson() {
        List<User> list = new ArrayList<>();
        list.add(new User("hello", 1));
        list.add(new User("hello", 2));
        System.out.println(JSON.toJSONString(list));
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    static class User {

        String name;

        Integer age;
    }

}
