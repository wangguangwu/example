package com.wangguangwu;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author wangguangwu
 */
@SuppressWarnings("all")
class TestOptional {

    @Test
    void testOf() {
        // this.value = Objects.requireNonNull(value);
        // 参数不能为 null
        Optional<Integer> optional1 = Optional.of(1);

        // 参数可以是 null
        Optional<Object> optional2 = Optional.ofNullable(null);

        // 参数可以是非 null
        Optional<Integer> optional3 = Optional.ofNullable(2);
    }

    @Test
    void testEmpty() {
        Optional<Object> optional1 = Optional.ofNullable(null);
        Optional<Object> optional2 = Optional.ofNullable(null);
        Assertions.assertSame(optional1, optional2);
        Assertions.assertSame(optional1, Optional.<Integer>empty());

        // 所有 null 包装成 Optional 对象
        Optional<Integer> o1 = Optional.empty();
        Optional<Integer> o2 = Optional.empty();
        Assertions.assertSame(o1, o2);
    }

    @Test
    void testIsPresent() {
        Optional<Integer> optional1 = Optional.ofNullable(1);
        Optional<Object> optional2 = Optional.ofNullable(null);

        // isPresent 判断值是否存在
        Assertions.assertTrue(optional1.isPresent());
        Assertions.assertFalse(optional2.isPresent());
    }

    @Test
    void testIsPresentNotNull() {
        Optional<Integer> optional1 = Optional.ofNullable(1);
        Optional<Object> optional2 = Optional.ofNullable(null);

        // 如果不是 null，调用 consumer
        optional1.ifPresent(integer -> System.out.println("value is " + integer));

        // 是 null，不调用 consumer
        optional2.ifPresent(integer -> System.out.println("value is " + integer));
    }

    @Test
    void testOrElse() {
        Optional<Integer> optional1 = Optional.ofNullable(1);
        Optional<Object> optional2 = Optional.ofNullable(null);

        // orElse：如果 Optional 对象保存的值不为 null，则返回原来的值，否则返回 value
        Assertions.assertEquals(1, optional1.orElse(1));
        Assertions.assertEquals(1000, optional2.orElse(1000));
    }

    @Test
    void testOrElseGet() {
        Optional<Integer> optional1 = Optional.ofNullable(1);
        Optional<Object> optional2 = Optional.ofNullable(null);

        // orElseGet：功能与 orElse 相同，只是 orElseGet 参数是一个对象
        Assertions.assertEquals(1, optional1.orElseGet(() -> {
            return 1000;
        }));
        Assertions.assertEquals(1000, optional2.orElseGet(() -> {
            return 1000;
        }));
    }

    @Test
    void testOrElseThrow() {
        Optional<Integer> optional1 = Optional.ofNullable(1);
        Optional<Integer> optional2 = Optional.ofNullable(null);

        // orElseThrow：值不存在则抛出异常，存在则什么都不做
        try {
            optional1.orElseThrow(() -> {
                throw new IllegalStateException();
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            optional2.orElseThrow(() -> {
                throw new IllegalStateException();
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Test
    void testFilter() {
        Optional<Integer> optional1 = Optional.ofNullable(1);
        Optional<Integer> optional2 = Optional.ofNullable(null);

        Optional<Integer> filter1 = optional1.filter(a -> a == null);
        Optional<Integer> filter2 = optional1.filter(a -> a == 1);
        Optional<Integer> filter3 = optional2.filter(a -> a == null);

        System.out.println(filter1.isPresent());
        System.out.println(filter2.isPresent());
        System.out.println(filter2.get().intValue() == 1);
        System.out.println(filter3.isPresent());
    }

    @Test
    void testMap() {
        Optional<Integer> optional1 = Optional.ofNullable(1);
        Optional<Object> optional2 = Optional.ofNullable(null);

        // map(Function)：对 Optional 中保存的值进行函数运算，并且返回新的 Optional（可以是任何类型）
        Optional<String> stringOptional1 = optional1.map(a -> "hello" + a);
        Optional<String> stringOptional2 = optional2.map(a -> "hello" + a);

        System.out.println(stringOptional1.get());
        System.out.println(stringOptional2.isPresent());

        String name = "hello world";
        Optional<Integer> integer = Optional.of(name)
                .map(String::length);
        System.out.println(integer.orElse(0));
    }

    @Test
    void testFlatMap() {
        Optional<Integer> optional1 = Optional.ofNullable(1);

        Optional<Optional<String>> stringOptional1 = optional1.map(a -> Optional.of("hello" + a));

        Optional<String> stringOptional2 = optional1.flatMap(a -> Optional.of("hello" + a));

        System.out.println(stringOptional1.get().get());
        System.out.println(stringOptional2.get());
    }

    @Test
    void testFilterAndPredicate() {
        String password1 = "123456";
        String password2 = "12345";

        Predicate<String> len5 = pwd -> pwd.length() > 5;
        Predicate<String> len10 = pwd -> pwd.length() < 10;

        boolean result1 = Optional.ofNullable(password1)
                .filter(len5.and(len10))
                .isPresent();
        boolean result2 = Optional.ofNullable(password2)
                .filter(len5.and(len10))
                .isPresent();
        System.out.println(result1);
        System.out.println(result2);
    }

    @Test
    void testMapAndFilter() {
        String password = "PassWord";

        Predicate<String> len6 = pwd -> pwd.length() > 6;
        Predicate<String> len10 = pwd -> pwd.length() < 10;
        Predicate<String> eq = pwd -> "password".equals(pwd);

        boolean result = Optional
                .ofNullable(password)
                .map(String::toLowerCase)
                .filter(len6.and(len10).and(eq))
                .isPresent();
        System.out.println(result);
    }

    @Test
    void testOptionalAndStream() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        Predicate<Integer> predicate = data -> data.compareTo(3) > 0;

        Optional.ofNullable(list)
                .orElse(null)
                .stream()
                .filter(predicate)
                .forEach(System.out::println);
    }

}
