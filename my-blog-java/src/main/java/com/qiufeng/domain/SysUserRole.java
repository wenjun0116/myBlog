package com.qiufeng.domain;

import com.qiufeng.common.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 每个用户对应的权限对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysUserRole extends BaseModel {

    /** 用户唯一标识 */
    private Long userId;

    /** 权限唯一标识 */
    private Long roleId;

}
