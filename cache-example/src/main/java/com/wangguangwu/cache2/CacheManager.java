package com.wangguangwu.cache2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangguangwu
 */
public class CacheManager {

    private static Map<String, Object> cacheMap = new ConcurrentHashMap<>();

    /**
     * 单实例构造方法
     */
    private CacheManager() {
        super();
    }

    public static boolean getSimpleFlag(String key) {
        try {
            return (Boolean) cacheMap.get(key);
        } catch (NullPointerException e) {
            return false;
        }
    }


    /**
     * 自定义缓存
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    private static class Cache {

        /**
         * 缓存 ID
         */
        private String key;

        /**
         * 缓存数据
         */
        private Object value;

        /**
         * 更新时间
         */
        private long timeOut;

        /**
         * 是否终止
         */
        private boolean expired;

    }

}
