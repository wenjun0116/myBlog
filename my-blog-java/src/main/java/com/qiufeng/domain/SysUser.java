package com.qiufeng.domain;

import com.qiufeng.common.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysUser extends BaseModel {

    /** 用户ID */
    private Long userId;

    /** 用户名 */
    private String userName;

    /** 用户昵称 */
    private String nickName;

    /** 用户邮箱 */
    private String email;

    /** 用户手机号 */
    private Long phone;

    /** 用户性别：0表示男，1表示女，2表示未知 */
    private Integer sex;

    /** 用户头像地址 */
    private String avatar;

    /** 用户密码 */
    private String password;

    /** 账户状态：0表示正常，1表示禁用 */
    private Integer status;

    /** 账户删除标识：0表示未删除，1表示已删除 */
    private Integer delFlag;

    /** 用户登录Ip地址 */
    private String loginIp;

}
