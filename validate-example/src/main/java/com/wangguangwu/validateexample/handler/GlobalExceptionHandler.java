package com.wangguangwu.validateexample.handler;


import com.wangguangwu.validateexample.entity.Result;
import com.wangguangwu.validateexample.enums.ResultEnum;
import com.wangguangwu.validateexample.exception.ValidateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 全局处理异常类
 *
 * @author wangguangwu
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理 JSR303 验证异常
     *
     * @param e  e
     * @return  result
     */
    @ResponseBody
    @ExceptionHandler(value = {MethodArgumentNotValidException.class, BindException.class, ValidateException.class})
    public Result handleValidException(Exception e) {
        BindingResult bindingResult = null;
        if (e instanceof MethodArgumentNotValidException) {
            bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
        } else if (e instanceof BindException) {
            bindingResult = ((BindException) e).getBindingResult();
        } else if (e instanceof ValidateException) {
            return Result.error(((ValidateException) e).getCode(), e.getMessage(), ((ValidateException) e).getData());
        }
        Map<String, String> errorMap = new HashMap<>(16);
        List<FieldError> fieldErrors = Objects.requireNonNull(bindingResult).getFieldErrors();
        fieldErrors.forEach(error -> errorMap.put(error.getField(), error.getDefaultMessage()));
        log.info("------参数校验失败:{}", errorMap);
        return Result.error(ResultEnum.ILLEGAL_PARAMETER.getCode(), ResultEnum.ILLEGAL_PARAMETER.getMsg(), errorMap);
    }

}


