package com.qiufeng.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUser {

    /** 唯一标识 */
    private String uuid;

    /** 用户名 */
    private String username;

    /** 密码 */
    private String password;

    /** 验证码 */
    private String code;

    /** 是否报错登录信息 */
    private Boolean rememberMe;

}
