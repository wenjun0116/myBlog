package com.qiufeng.service.user;

import com.qiufeng.domain.SysRoleMenu;

import java.util.List;

public interface SysRoleMenuService {

    /**
     * 删除权限对应的模块
     * @param key
     * @return
     */
    int deleteByPrimaryKey(SysRoleMenu key);

    /**
     * 新增权限模块
     * @param record
     * @return
     */
    int insertSelective(SysRoleMenu record);

    /**
     * 根据权限ID查询对应模块
     * @param roleId
     * @return 权限模块集合
     */
    List<SysRoleMenu> selectSysRoleMenuByRoleId(Long roleId);

}
