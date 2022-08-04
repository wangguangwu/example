package com.wangguangwu.validateexample.entity;


import com.wangguangwu.validateexample.enums.ResultEnum;
import lombok.*;

import java.io.Serializable;

/**
 * @author wangguangwu
 */
@SuppressWarnings("unused")
@Data
@AllArgsConstructor
public class Result implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer code;

    private String msg;

    private transient Object data;

    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static Result success(String msg, Object data) {
        return new Result(ResultEnum.SUCCESS.getCode(), msg, data);
    }

    public static Result error(String msg) {
        return new Result(ResultEnum.FAIL.getCode(), msg);
    }

    public static Result error(String msg, Object data) {
        return new Result(ResultEnum.FAIL.getCode(), msg, data);
    }

    public static Result error(Integer code, String msg, Object data) {
        return new Result(code, msg, data);
    }
}


