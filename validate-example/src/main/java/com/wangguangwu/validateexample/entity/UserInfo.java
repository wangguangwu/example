package com.wangguangwu.validateexample.entity;

import com.wangguangwu.validateexample.groups.Create;
import com.wangguangwu.validateexample.groups.Update;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author wangguangwu
 */
@Data
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "人怎么可以没有爱好", groups = Create.class)
    private String hobby;

    @NotBlank(message = "写点备注", groups = Update.class)
    private String remark;

}
