package com.qiufeng.service.user.impl;

import com.qiufeng.domain.SysRole;
import com.qiufeng.domain.SysUser;
import com.qiufeng.mapper.user.SysUserMapper;
import com.qiufeng.service.user.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统用户业务实现层
 */
@Service
public class SysUserServiceImpl implements SysUserService {


    @Resource
    private SysUserMapper sysUserMapper;

    /**
     * 新增系统用户
     */
    @Override
    public void saveSysUser(SysUser user) {
        sysUserMapper.saveSysUser(user);
    }

    /**
     * 修改系统用户
     * @param user
     */
    @Override
    public void updateSysUser(SysUser user) {
        sysUserMapper.updateSysUser(user);
    }

    /**
     * 删除用户
     * @param userId
     */
    @Override
    public void deleteSysUser(Long userId) {
        sysUserMapper.deleteSysUser(userId);
    }

    /**
     * 根据用户名称查询用户
     * @param userName 用户名
     * @return
     */
    @Override
    public SysUser selectSysUserByUserName(String userName) {
        return sysUserMapper.selectSysUserByUserName(userName);
    }

    /**
     * 查询该账户是否存在
     * @param userName 用户名
     * @param password 密码
     * @return 返回0则该账户不存在，返回1则该账户存在
     */
    @Override
    public Integer selectSysUserIsExist(String userName, String password) {
        return sysUserMapper.selectSysUserIsExist(userName,password);
    }

    /**
     * 用户登录
     * @param userName 用户名
     * @param password 密码
     * @return
     */
    @Override
    public SysUser loginSysUser(String userName, String password) {
        return sysUserMapper.loginSysUser(userName,password);
    }

    /**
     * 查询所有系统用户
     * @return
     */
    @Override
    public List<SysRole> selectSysUserList() {
        return sysUserMapper.selectSysUserList();
    }

    /**
     * 修改用户密码
     * @param userId
     * @param password
     */
    @Override
    public void updatePwd(Long userId, String password) {
        sysUserMapper.updatePwd(userId,password);
    }

    /**
     * 根据userId查询用户
     * @param userId
     * @return
     */
    @Override
    public SysUser selectSysUserByUserId(Long userId) {
        return sysUserMapper.selectSysUserByUserId(userId);
    }

}
