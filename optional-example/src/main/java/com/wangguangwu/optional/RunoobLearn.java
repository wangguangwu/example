package com.wangguangwu.optional;

import java.util.Optional;

/**
 * 使用 Optional 避免 null 导致的 NullPointerException
 * <p>
 * Optional 实际上是个容器，可以保存类型 T 的值，或者仅仅保存 null
 *
 * @author wangguangwu
 */
@SuppressWarnings("all")
public class RunoobLearn {

    public static void main(String[] args) {
        Integer value1 = null;
        Integer value2 = new Integer(10);

        // Optional.ofNullable 允许传递为 null 参数
        Optional<Integer> a = Optional.ofNullable(value1);

        // Optional.of 如果传递的参数为 null，抛出异常 NullPointerException
        Optional<Integer> b = Optional.of(value2);
        System.out.println(sum(a, b));
    }

    public static Integer sum(Optional<Integer> a, Optional<Integer> b) {
        // Optional.isPresent 判断值是否存在
        System.out.println("第一个参数值存在：" + a.isPresent());
        System.out.println("第二个参数值存在：" + b.isPresent());

        // Optional.orElse 如果值存在，返回它，否则返回默认值
        Integer value1 = a.orElse(0);

        // Optional.get 获取值吗，值需要存在
        Integer value2 = b.get();
        return value1 + value2;
    }

}
