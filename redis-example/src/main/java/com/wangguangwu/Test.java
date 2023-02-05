package com.wangguangwu;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangguangwu
 */
public class Test {

    public static void main(String[] args) {
        Map<Character, Integer> need = new HashMap<>();
        need.put('a', 1);
        need.put('a', need.getOrDefault('a', 0) + 1);
        System.out.println();
    }

}
