package com.qiufeng.service.user;

import com.qiufeng.domain.SysRole;

import java.util.List;

/**
 * 权限业务层
 */
public interface SysRoleService {

    /**
     * 新增系统权限
     * @param role
     */
    void saveSysRole(SysRole role);

    /**
     * 修改系统权限
     * @param role
     */
    void updateSysRole(SysRole role);

    /**
     * 删除系统权限
     * @param roleId
     */
    void deleteSysRole(Long roleId);

    /**
     * 根据权限名称查询
     * @param roleName 权限名
     * @return
     */
    SysRole selectSysRoleByRoleName(String roleName);

    /**
     * 根据权限ID查询
     * @param roleId 权限ID
     * @return
     */
    SysRole selectSysRoleByRoleId(Long roleId);

    /**
     * 根据权限键查询
     * @param key 权限键
     * @return
     */
    SysRole selectSysRoleByRoleKey(String key);

    /**
     * 查询所有权限
     * @return
     */
    List<SysRole> selectSysRoleList();

}
