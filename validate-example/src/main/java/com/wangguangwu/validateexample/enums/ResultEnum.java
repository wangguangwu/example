package com.wangguangwu.validateexample.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


/**
 * @author wangguangwu
 */
@SuppressWarnings("all")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ResultEnum {

    SUCCESS(200, "成功！"),
    FAIL(100, "失败！"),
    FILE_MAX(110, "上传文件过大！"),
    ILLEGAL_PARAMETER(400, "非法参数！"),
    NO_PERMISSION(403, "权限不足！"),
    NO_AUTH(401, "未登录！"),
    NOT_FOUND(404, "未找到该资源！"),
    NOT_REQUEST_METHOD(405, "该请求方式不支持！"),
    INTERNAL_SERVER_ERROR(500, "服务器异常请联系管理员！");

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误信息
     */
    private String msg;

}

