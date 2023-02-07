package com.wangguangwu.map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

/**
 * 在 java 中，如果想要根据 value 查找对应的 key，需要遍历整个 Map
 * <p>
 * Guava 中的 BiMap 提供了一种 key 和 value 双向关联的数据结构
 * 因为是双向关联，所以 BiMap 中的 key 和 value 可以认为处于等价地位，所以 value 也是不允许重复的
 *
 * @author wangguangwu
 */
public class BiMapTest {

    private static HashBiMap<String, String> biMap;

    @Before
    public void before() {
        biMap = HashBiMap.create();
        biMap.put("wang", "guangwu");
        biMap.put("tony", "stark");
        biMap.put("lionel", "messi");
    }

    @Test
    public void get() {
        // 使用 key 获取 value
        System.out.println(biMap.get("tony"));

        // 使用 value 获取 key
        BiMap<String, String> inverse = biMap.inverse();
        System.out.println(inverse.get("messi"));

        System.out.println("=================================================");

        Set<Map.Entry<String, String>> biMapCells = biMap.entrySet();
        biMapCells.forEach(cell -> System.out.println(cell.getKey() + ":" + cell.getValue()));

        System.out.println("=================================================");

        Set<Map.Entry<String, String>> inverseCells = inverse.entrySet();
        inverseCells.forEach(cell -> System.out.println(cell.getKey() + ":" + cell.getValue()));
    }
}
