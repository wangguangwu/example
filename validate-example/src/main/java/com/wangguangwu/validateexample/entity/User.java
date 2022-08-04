package com.wangguangwu.validateexample.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wangguangwu.validateexample.anno.Gender;
import com.wangguangwu.validateexample.groups.Create;
import com.wangguangwu.validateexample.groups.Update;
import lombok.Data;
import org.hibernate.validator.constraints.Length;


import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author wangguangwu
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户 ID
     */
    @NotNull(message = "用户 id 不能为空", groups = Update.class)
    private Long userId;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Length(max = 20, message = "用户名不能超过 20 个字符", groups = {Create.class, Update.class})
    private String username;

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    @Length(min = 11, max = 11, message = "传入的电话号码长度有误，必须为 11 位", groups = Create.class)
    @Pattern(regexp = RegularConstants.PHONE_CHINA, message = "手机号格式有误")
    private String mobile;

    /**
     * 性别
     */
    @NotBlank(message = "性别不能为空")
    @Gender(groups = Create.class)
    private String gender;

    /**
     * 邮箱
     */
    @NotBlank(message = "联系邮箱不能为空")
    @Email(message = "邮箱格式不对")
    private String email;

    /**
     * 密码
     */
    private String password;

    /**
     * 账户余额
     */
    @Min(value = 0, message = "用户账户余额不能小于 0")
    private BigDecimal amount;

    /**
     * 创建时间
     */
    @Future(message = "时间必须是未来时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @Valid
    @NotNull
    private UserInfo userInfo;

}
