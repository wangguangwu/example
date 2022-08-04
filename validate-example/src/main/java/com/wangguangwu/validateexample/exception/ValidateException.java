package com.wangguangwu.validateexample.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wangguangwu
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class ValidateException extends RuntimeException {

    /**
     * 状态码
     */
    private final Integer code;


    /**
     * 错误提示
     */
    private final String message;


    /**
     * 错误详细信息
     */
    private final transient Object data;

}
