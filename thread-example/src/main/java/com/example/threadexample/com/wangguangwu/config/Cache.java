package com.example.threadexample.com.wangguangwu.config;

/**
 * Created by luderun on 2018/8/16.
 */
public class Cache {

    /**
     * 缓存ID
     * */
    private String key;
    /**
     * 缓存数据
     * */
    private Object value;
    /**
     * 更新时间
     * */
    private long timeOut;//
    /**
     * 是否终止
     * */
    private boolean expired;
    public Cache() {
        super();
    }

    public Cache(String key, Object value, long timeOut, boolean expired) {
        this.key = key;
        this.value = value;
        this.timeOut = timeOut;
        this.expired = expired;
    }

    public String getKey() {
        return key;
    }

    public long getTimeOut() {
        return timeOut;
    }

    public Object getValue() {
        return value;
    }

    public void setKey(String string) {
        key = string;
    }

    public void setTimeOut(long l) {
        timeOut = l;
    }

    public void setValue(Object object) {
        value = object;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean b) {
        expired = b;
    }
}
