package com.wangguangwu.validateexample.controller;

import com.alibaba.fastjson.JSON;
import com.wangguangwu.validateexample.entity.User;
import com.wangguangwu.validateexample.enums.ResultEnum;
import com.wangguangwu.validateexample.exception.ValidateException;
import com.wangguangwu.validateexample.groups.Create;
import com.wangguangwu.validateexample.groups.Update;
import com.wangguangwu.validateexample.utils.ValidatorUtils;
import org.apache.commons.lang3.StringUtils;

import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author wangguangwu
 */
@RequestMapping("hello")
@RestController
public class ValidateController {

    /**
     * 通过注解实现
     *
     * @param user   user
     * @param result error
     * @return result
     */
    @PostMapping("/validateCreateByAnno")
    public String validateCreateByAnno(@RequestBody @Validated(Create.class) User user, BindingResult result) {
        return getString(user, getErrorMessage(result));
    }

    /**
     * 通过注解实现
     *
     * @param user   user
     * @param result error
     * @return result
     */
    @PostMapping("/validateUpdateByAnno")
    public String validateUpdateByAnno(@RequestBody @Validated(Update.class) User user, BindingResult result) {
        return getString(user, getErrorMessage(result));
    }

    @PostMapping("/validateCreateByLocal")
    public void validateCreateByLocal(@RequestBody User user) {
        validate(user, Create.class);
    }

    @PostMapping("/validateUpdateByLocal")
    public void validateUpdateByLocal(@RequestBody User user) {
        validate(user, Update.class);
    }


    private String getString(User user, String errorMessage) {
        System.out.println(JSON.toJSONString(user));
        return StringUtils.isBlank(errorMessage) ? "Hello World" : errorMessage;
    }

    /**
     * 整理错误数据
     *
     * @param result error
     * @return 汇总好的数据
     */
    private String getErrorMessage(BindingResult result) {
        StringBuilder errorMessage = new StringBuilder();
        result.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append(";"));
        return errorMessage.toString();
    }

    protected <T> void validate(T bean, Class<?>... groups) {
        Map<String, StringBuilder> errorMap = ValidatorUtils.validate(bean, groups);
        if (!CollectionUtils.isEmpty(errorMap)) {
            throw new ValidateException(ResultEnum.ILLEGAL_PARAMETER.getCode(), ResultEnum.ILLEGAL_PARAMETER.getMsg(), errorMap);
        }
    }

}
