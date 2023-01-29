package com.wangguangwu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangguangwu
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {

    private Long userId;

    private String name;

    private int age;

    private String phoneNum;

    private String password;

    private String headImageUrl;

}
