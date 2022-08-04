package com.wangguangwu.validateexample.utils;

import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 不通过注解方式实现
 *
 * @author wangguangwu
 */
public final class ValidatorUtils {

    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * 使用指定分组
     *
     * @param bean   被校验的 bean
     * @param groups 分组
     * @param <T>    泛型
     * @return map
     */
    public static <T> Map<String, StringBuilder> validate(T bean, Class<?>... groups) {
        // 拿到 bean 中的所有属性
        Field[] fields = bean.getClass().getDeclaredFields();
        //
        int length = fields.length;
        Map<String, StringBuilder> errorMap = new HashMap<>(length);
        if (groups == null) {
            // 没有指定分组，就使用默认的分组
            groups = new Class[]{Default.class};
        }
        Set<ConstraintViolation<T>> set = VALIDATOR.validate(bean, groups);
        if (CollectionUtils.isEmpty(set)) {
            return Collections.emptyMap();
        }

        String property;
        for (ConstraintViolation<T> c : set) {
            // 循环获取错误信息
            property = c.getPropertyPath().toString();
            if (errorMap.get(property) != null) {
                errorMap.get(property).append(",").append(c.getMessage());
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append(c.getMessage());
                errorMap.put(property, sb);
            }
        }
        return errorMap;
    }

    /**
     * 使用指定分组
     *
     * @param bean     被校验的 bean
     * @param property 被校验的字段
     * @param groups   分组
     * @param <T>      泛型
     * @return map
     */
    public static <T> Map<String, StringBuilder> validateProperty(T bean, String property, Class<?>... groups) {
        // 拿到 bean 中所有的属性
        Field[] fields = bean.getClass().getDeclaredFields();
        int length = fields.length;
        Map<String, StringBuilder> errorPropertyMap = new HashMap<>(length);
        if (groups == null) {
            // 没有指定分组，就使用默认的分组
            groups = new Class[]{Default.class};
        }
        Set<ConstraintViolation<T>> set = VALIDATOR.validateProperty(bean, property, groups);
        if (CollectionUtils.isEmpty(set)) {
            return Collections.emptyMap();
        }
        for (ConstraintViolation<T> c : set) {
            // 循环获取错误信息
            if (errorPropertyMap.get(property) != null) {
                errorPropertyMap.get(property).append(",").append(c.getMessage());
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append(c.getMessage());
                errorPropertyMap.put(property, sb);
            }
        }
        return errorPropertyMap;
    }

    private ValidatorUtils() {
    }

}
