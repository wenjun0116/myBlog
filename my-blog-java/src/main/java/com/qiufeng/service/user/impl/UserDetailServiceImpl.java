package com.qiufeng.service.user.impl;

import com.qiufeng.domain.SysRole;
import com.qiufeng.domain.SysUser;
import com.qiufeng.service.user.SysRoleService;
import com.qiufeng.service.user.SysUserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//自定义验证类UserDetailsService 实现Security框架UserDetailsService的接口
public class UserDetailServiceImpl implements UserDetailsService {
    //修改UserDetailsServiceImpl.java，添加属性和setter方法，修改loadUserByUsername方法
    @Resource
    private SysUserService userService;

    @Resource
    private SysRoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String userName){
        UserDetails userDetails = null;
        try {
            SysUser user = userService.selectSysUserByUserName(userName);
            userDetails = new org.springframework.security.core.userdetails.User(user.getUserName(),
                    user.getPassword(),true,true,true,true,getAuthorities());
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("认证错误=======================>");
            return null;
        }
        return userDetails;
    }

    /**
     * 获取用户所有权限
     * @return
     */
    private Collection<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
        List<SysRole> sysRoles = roleService.selectSysRoleList();
        for (SysRole sysRole:sysRoles) {
            authList.add(new SimpleGrantedAuthority(sysRole.getRoleKey()));
        }
        return authList;
    }
}
