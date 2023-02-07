package com.wangguangwu.map;

import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;
import org.junit.Test;

/**
 * 范围 Map
 * 描述了一种从区间到特定值的映射关系
 *
 * @author wangguangwu
 */
public class RangeMapTest {

    @Test
    public void test() {
        RangeMap<Integer, String> rangeMap = TreeRangeMap.create();
        rangeMap.put(Range.closedOpen(0, 60), "L1");
        rangeMap.put(Range.closedOpen(60, 90), "L2");
        rangeMap.put(Range.closedOpen(90, 100), "L3");

        System.out.println(rangeMap.get(59));
        System.out.println(rangeMap.get(60));
        System.out.println(rangeMap.get(90));
        System.out.println(rangeMap.get(91));
    }
}
