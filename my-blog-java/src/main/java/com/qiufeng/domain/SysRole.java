package com.qiufeng.domain;

import com.qiufeng.common.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 权限对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysRole extends BaseModel {

    /** 权限唯一标识 */
    private Long roleId;

    /** 权限名称 */
    private String roleName;

    /** 权限键 */
    private String roleKey;

    /** 权限范围：0表示全部权限，1表示自定义权限 */
    private Integer dataScope;

    /** 权限状态：0表示正常，1表示禁用 */
    private Integer status;

    /** 删除标识：0表示未删除，1表示已删除 */
    private Integer delFlag;

}
