package com.wangguangwu.map;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * java 中的 Map 中维护的是键值一对一的关系
 * 如果想要将一个键映射到多个值上，就只能把值的内容设为集合形式
 * <p>
 * Guava 中的 MultiMap 提供了将一个键映射到多个值的形式
 *
 * @author wangguangwu
 */
public class MultiMapTest {

    private static Map<String, List<Integer>> map;

    private static Multimap<String, Integer> multimap;

    @Before
    public void before() {
        map = new HashMap<>();
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        map.put("day", list);

        multimap = ArrayListMultimap.create();
        multimap.put("day", 1);
        multimap.put("day", 2);
        multimap.put("day", 8);
        multimap.put("month", 2);
    }

    @Test
    public void print() {
        System.out.println(map);
        System.out.println(multimap);
        Collection<Integer> day = multimap.get("day");
        // get 方法会返回一个非 null 的集合，但这个集合的内容可能是 空
        Collection<Integer> year = multimap.get("year");
        System.out.println(day);
        System.out.println(year);
    }

    @Test
    public void get() {
        ArrayListMultimap<String, Integer> listMultimap = ArrayListMultimap.create();
        listMultimap.put("day", 1);
        listMultimap.put("day", 2);
        listMultimap.put("day", 8);
        listMultimap.put("day", 22);
        listMultimap.put("month", 2);

        List<Integer> day = listMultimap.get("day");
        List<Integer> month = listMultimap.get("month");

        // 下标
        day.remove(0);
        day.remove(2);

        month.add(12);

        System.out.println(listMultimap);
    }

    @Test
    public void transfer2Map() {
        // 使用 asMap 方法
        // 可以将 MultiMap 转换为 Map<K, Collection> 的形式
        // 同时这个 Map 可以被看做一个关联的视图，在这个 Map 上的操作会作用于原始的 MultiMap
        Map<String, Collection<Integer>> asMap = multimap.asMap();
        for (String key : asMap.keySet()) {
            System.out.println(key + ":" + asMap.get(key));
        }
        // 会作用于原始的 map 上
        asMap.get("day").add(24);
        System.out.println(multimap);
    }
}
