package com.wangguangwu.validateexample.controller;

import com.alibaba.fastjson.JSON;
import com.wangguangwu.validateexample.entity.User;
import com.wangguangwu.validateexample.groups.Create;
import com.wangguangwu.validateexample.groups.Update;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @PostMapping("/validateCreate")
    public String validateCreate(@RequestBody @Validated(Create.class) User user, BindingResult result) {
        return getString(user, getErrorMessage(result));
    }

    /**
     * 通过注解实现
     *
     * @param user   user
     * @param result error
     * @return result
     */
    @PostMapping("/validateUpdate")
    public String validateUpdate(@RequestBody @Validated(Update.class) User user, BindingResult result) {
        return getString(user, getErrorMessage(result));
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

}
