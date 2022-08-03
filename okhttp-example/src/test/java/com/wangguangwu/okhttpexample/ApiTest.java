package com.wangguangwu.okhttpexample;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangguangwu
 */
class ApiTest {

    @Test
    void testMapIsEmpty() {
        Map<String, String> map = new HashMap<>();
        System.out.println(CollectionUtils.isEmpty(map));
        map.put(null, null);
        System.out.println(CollectionUtils.isEmpty(map));
        map.clear();
        System.out.println("===============================");
        map.put(null, "hello");
        System.out.println(CollectionUtils.isEmpty(map));
        System.out.println(CollectionUtils.isEmpty(map.entrySet()));
        System.out.println(CollectionUtils.isEmpty(map.keySet()));
        System.out.println(map.isEmpty());
        map.entrySet().forEach(System.out::println);
        map.keySet().forEach(System.out::println);
        map.clear();
        System.out.println("===============================");
        map.put("hello", null);
        System.out.println(CollectionUtils.isEmpty(map));
        System.out.println(CollectionUtils.isEmpty(map.entrySet()));
        System.out.println(CollectionUtils.isEmpty(map.keySet()));
        System.out.println(map.isEmpty());
        map.entrySet().forEach(System.out::println);
        map.keySet().forEach(System.out::println);
        Assertions.assertNotNull(map);
    }

}
