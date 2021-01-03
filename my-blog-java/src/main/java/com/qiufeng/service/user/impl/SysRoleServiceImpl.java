package com.qiufeng.service.user.impl;

import com.qiufeng.domain.SysRole;
import com.qiufeng.mapper.user.SysRoleMapper;
import com.qiufeng.service.user.SysRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户权限业务实现层
 */
@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;

    /**
     * 新增系统权限
     * @param role
     */
    @Override
    public void saveSysRole(SysRole role) {
        sysRoleMapper.saveSysRole(role);
    }

    /**
     * 修改系统权限
     * @param role
     */
    @Override
    public void updateSysRole(SysRole role) {
        sysRoleMapper.updateSysRole(role);
    }

    /**
     * 删除系统权限
     * @param roleId
     */
    @Override
    public void deleteSysRole(Long roleId) {
        sysRoleMapper.deleteSysRole(roleId);
    }

    /**
     * 根据权限名称查询
     * @param roleName 权限名
     * @return
     */
    @Override
    public SysRole selectSysRoleByRoleName(String roleName) {
        return sysRoleMapper.selectSysRoleByRoleName(roleName);
    }

    /**
     * 根据权限ID查询
     * @param roleId 权限ID
     * @return
     */
    @Override
    public SysRole selectSysRoleByRoleId(Long roleId) {
        return sysRoleMapper.selectSysRoleByRoleId(roleId);
    }

    /**
     * 根据权限键查询
     * @param key 权限键
     * @return
     */
    @Override
    public SysRole selectSysRoleByRoleKey(String key) {
        return sysRoleMapper.selectSysRoleByRoleKey(key);
    }

    /**
     * 查询所有权限
     * @return
     */
    @Override
    public List<SysRole> selectSysRoleList() {
        return sysRoleMapper.selectSysRoleList();
    }

}
