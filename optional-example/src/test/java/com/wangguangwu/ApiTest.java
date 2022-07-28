package com.wangguangwu;

import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * @author wangguangwu
 */
public class ApiTest {

    @Test
    void test() {
        Map<String, String> pm = new HashMap<>();
        pm.put("key1", "value1");
        System.out.println(test1(pm));
        pm.clear();
        pm.put("key1", "");
        System.out.println(test2(pm));
        pm.clear();
        pm.put("key1", null);
        System.out.println(test3(pm));
        pm.clear();
        System.out.println(test4(pm));
    }

    public static String test1(Map<String, String> pm) {
        Optional<Map<String, String>> mapOptional = Optional.ofNullable(pm);

        return mapOptional.map(map -> map.get("key1"))
                .filter(value -> !"".equals(value))
                .flatMap(value -> {
                    //一大堆业务处理...
                    return Optional.of("test1成功执行");
                }).orElse("test1停车了");
    }

    public static String test2(Map<String, String> pm) {
        Optional<Map<String, String>> mapOptional = Optional.ofNullable(pm);
        return mapOptional.map(map -> map.get("key1"))
                .filter(value -> !"".equals(value))
                .flatMap(value -> {
                    //一大堆业务处理...
                    return Optional.of("test2成功执行");
                }).orElse("test2停车了");
    }


    public static String test3(Map<String, String> pm) {
        Optional<Map<String, String>> mapOptional = Optional.ofNullable(pm);
        return mapOptional.map(map -> map.get("key1"))
                .filter(value -> !"".equals(value))
                .flatMap(value -> {
                    //一大堆业务处理...
                    return Optional.of("test3成功执行");
                }).orElse("test3停车了");
    }

    public static String test4(Map<String, String> pm) {
        Optional<Map<String, String>> mapOptional = Optional.ofNullable(pm);
        return mapOptional.map(map -> map.get("key1"))
                .filter(value -> !"".equals(value))
                .flatMap(value -> {
                    //开始啪啪业务逻辑 发短信啊crud啊...
                    return Optional.of("test4成功执行");
                }).orElse("test4停车了");
    }


}
