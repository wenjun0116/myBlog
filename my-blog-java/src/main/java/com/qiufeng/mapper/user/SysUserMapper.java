package com.qiufeng.mapper.user;

import com.qiufeng.domain.SysRole;
import com.qiufeng.domain.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统用户操控层
 */
public interface SysUserMapper {

    /**
     * 新增系统用户
     */
    void saveSysUser(SysUser user);

    /**
     * 修改系统用户
     * @param user
     */
    void updateSysUser(SysUser user);

    /**
     * 删除用户
     * @param userId
     */
    void deleteSysUser(Long userId);

    /**
     * 根据用户名称查询用户
     * @param userName 用户名
     * @return
     */
    SysUser selectSysUserByUserName(String userName);

    /**
     * 查询该账户是否存在
     * @param userName 用户名
     * @param password 密码
     * @return 返回0则该账户不存在，返回1则该账户存在
     */
    Integer selectSysUserIsExist(@Param("userName") String userName,@Param("password") String password);

    /**
     * 用户登录
     * @param userName 用户名
     * @param password 密码
     * @return
     */
    SysUser loginSysUser(@Param("userName") String userName,@Param("password") String password);

    /**
     * 查询所有系统用户
     * @return
     */
    List<SysRole> selectSysUserList();

    /**
     * 修改用户密码
     * @param userId
     * @param password
     */
    void updatePwd(@Param("userId") Long userId,@Param("password") String password);

    /**
     * 根据userId查询用户
     * @param userId
     * @return
     */
    SysUser selectSysUserByUserId(Long userId);

}
