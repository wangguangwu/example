package com.wangguangwu.validateexample.controller;

import com.wangguangwu.validateexample.anno.Gender;
import org.apache.commons.lang3.StringUtils;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 校验性别
 *
 * @author wangguangwu
 */
public class GenderValid implements ConstraintValidator<Gender, String> {

    @Override
    public void initialize(Gender constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(value)) {
            return false;
        }
        return "男".equals(value) || "女".equals(value);
    }

}
