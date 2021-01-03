package com.qiufeng.service.user;

import com.qiufeng.domain.SysUserRole;

import java.util.List;

/**
 * 用户权限业务层
 */
public interface SysUserRoleService {

    /**
     * 新增用户权限
     * @param userRole
     */
    void saveSysUserRole(SysUserRole userRole);

    /**
     * 删除用户权限
     * @param userId
     */
    void deleteSysUserRole(Long userId);

    /**
     * 根据用户ID
     * @param userId
     * @return
     */
    SysUserRole selectSysUserRoleBySysUserId(Long userId);

    /**
     * 根据权限Id查询所有拥有此权限的用户
     * @param roleId
     * @return
     */
    List<SysUserRole> selectSysUserRoleBySysRoleId(Long roleId);

}
