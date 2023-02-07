package com.wangguangwu.map;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;
import com.wangguangwu.map.entity.Hello;
import com.wangguangwu.map.entity.User;
import org.junit.Assert;
import org.junit.Test;

/**
 * 它的键是 Class，值是这个 Class 对应的实例对象
 *
 * @author wangguangwu
 */
public class ClassToInstanceMapTest {

    @Test
    public void test() {
        ClassToInstanceMap<Object> instanceMap = MutableClassToInstanceMap.create();
        User user = new User("wang", 24);
        Hello hello = new Hello("gary", 24);

        instanceMap.putInstance(User.class, user);
        instanceMap.putInstance(Hello.class, hello);

        // 取出对象时，避免了强制类型转换
        User user1 = instanceMap.getInstance(User.class);
        Assert.assertEquals(user1, user);
    }
}
