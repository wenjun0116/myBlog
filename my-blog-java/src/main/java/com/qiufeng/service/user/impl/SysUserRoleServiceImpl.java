package com.qiufeng.service.user.impl;

import com.qiufeng.domain.SysUserRole;
import com.qiufeng.mapper.user.SysUserRoleMapper;
import com.qiufeng.service.user.SysUserRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户权限实现层
 */
@Service
public class SysUserRoleServiceImpl implements SysUserRoleService {

    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    /**
     * 新增用户权限
     * @param userRole
     */
    @Override
    public void saveSysUserRole(SysUserRole userRole) {
        sysUserRoleMapper.saveSysUserRole(userRole);
    }

    /**
     * 删除用户权限
     * @param userId
     */
    @Override
    public void deleteSysUserRole(Long userId) {
        sysUserRoleMapper.deleteSysUserRole(userId);
    }

    /**
     * 根据用户ID
     * @param userId
     * @return
     */
    @Override
    public SysUserRole selectSysUserRoleBySysUserId(Long userId) {
        return sysUserRoleMapper.selectSysUserRoleBySysUserId(userId);
    }

    /**
     * 根据权限Id查询所有拥有此权限的用户
     * @param roleId
     * @return
     */
    @Override
    public List<SysUserRole> selectSysUserRoleBySysRoleId(Long roleId) {
        return selectSysUserRoleBySysRoleId(roleId);
    }

}
