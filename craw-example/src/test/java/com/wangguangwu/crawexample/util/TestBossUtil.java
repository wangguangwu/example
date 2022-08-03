package com.wangguangwu.crawexample.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

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

}
