package com.qiufeng.service.user.impl;

import com.qiufeng.domain.SysRoleMenu;
import com.qiufeng.mapper.user.SysRoleMenuMapper;
import com.qiufeng.service.user.SysRoleMenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SysRoleMenuServiceImpl implements SysRoleMenuService {

    @Resource
    private SysRoleMenuMapper roleMenuMapper;

    /**
     * 删除权限对应的模块
     * @param key
     * @return
     */
    @Override
    public int deleteByPrimaryKey(SysRoleMenu key) {
        return roleMenuMapper.deleteByPrimaryKey(key);
    }

    /**
     * 新增权限模块
     * @param record
     * @return
     */
    @Override
    public int insertSelective(SysRoleMenu record) {
        return roleMenuMapper.insertSelective(record);
    }

    /**
     * 根据权限ID查询对应模块
     * @param roleId
     * @return 权限模块集合
     */
    @Override
    public List<SysRoleMenu> selectSysRoleMenuByRoleId(Long roleId) {
        return roleMenuMapper.selectSysRoleMenuByRoleId(roleId);
    }

}
