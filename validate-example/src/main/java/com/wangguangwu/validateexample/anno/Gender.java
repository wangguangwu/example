package com.wangguangwu.validateexample.anno;

import com.wangguangwu.validateexample.controller.GenderValid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 自定义注解
 * 校验性别
 *
 * @author wangguangwu
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = GenderValid.class)
public @interface Gender {

    String message() default "性别只能为'男'或'女'";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
