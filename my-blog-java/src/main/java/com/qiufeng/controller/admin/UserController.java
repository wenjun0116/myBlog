package com.qiufeng.controller.admin;

import com.google.common.base.Charsets;
import com.qiufeng.common.constant.UserConstants;
import com.qiufeng.common.model.AjaxResult;
import com.qiufeng.common.utils.JwtUtils;
import com.qiufeng.common.utils.StringUtils;
import com.qiufeng.domain.SysMenu;
import com.qiufeng.domain.SysRoleMenu;
import com.qiufeng.domain.SysUser;
import com.qiufeng.domain.SysUserRole;
import com.qiufeng.service.user.*;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class UserController {

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysUserRoleService sysUserRoleService;

    @Resource
    private SysRoleMenuService sysRoleMenuService;

    @Resource
    private SysMenuService sysMenuService;

    @Resource
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/getInfo")
    public AjaxResult getInfo(HttpServletRequest request) throws UnsupportedEncodingException {
        //从cookie中获取token信息，再将token信息转换为用户信息
        Cookie[] cookies = request.getCookies();
        String token = null;
        String rememberMe = null;
        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals(JwtUtils.TOKEN_HEADER)) {
                token = URLDecoder.decode(cookies[i].getValue(), String.valueOf(Charsets.UTF_8));
                token = token.substring(JwtUtils.TOKEN_PREFIX.length(), token.length());
            }
            if (cookies[i].getName().equals("rememberMe")) {
                rememberMe = URLDecoder.decode(cookies[i].getValue(), String.valueOf(Charsets.UTF_8));
            }
        }
        if (token != null && rememberMe != null) {
            String userName = JwtUtils.getUserName(token);
            AjaxResult result = AjaxResult.success();
            SysUser user = sysUserService.selectSysUserByUserName(userName);
            result.put("user", user);
            result.put("roles", sysRoleService.selectSysRoleByRoleId(sysUserRoleService.selectSysUserRoleBySysUserId(user.getUserId()).getRoleId()));
            result.put("rememberMe", rememberMe);
            return result;
        }
        return AjaxResult.error("请登录！");
    }

    @GetMapping("/user/get")
    public AjaxResult getUserProfile(HttpServletRequest request) throws UnsupportedEncodingException {
        //获取用户信息，token信息转换为用户信息，后期将用户信息存入redis中
        //从cookie中获取token信息，再将token信息转换为用户信息
        Cookie[] cookies = request.getCookies();
        String token = null;
        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals(JwtUtils.TOKEN_HEADER)) {
                token = URLDecoder.decode(cookies[i].getValue(), String.valueOf(Charsets.UTF_8));
                token = token.substring(JwtUtils.TOKEN_PREFIX.length(), token.length());
            }
        }
        if (token != null) {
            String userName = JwtUtils.getUserName(token);
            SysUser user = sysUserService.selectSysUserByUserName(userName);
            return AjaxResult.success(user);
        }
        return AjaxResult.error("请登录！");
    }

    @PostMapping("/user/update")
    public AjaxResult updateUserProfile(@Validated @RequestBody SysUser user) {
        try {
            sysUserService.updateSysUser(user);
            return AjaxResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("修改出现异常！请检查后重试！");
        }
    }

    @PostMapping("/user/updatePwd")
    public AjaxResult updatePwd(String oldPassword, String newPassword, HttpServletRequest request) {
        try {
            //从cookie中获取token信息，再将token信息转换为用户信息
            Cookie[] cookies = request.getCookies();
            String token = null;
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals(JwtUtils.TOKEN_HEADER)) {
                    token = URLDecoder.decode(cookies[i].getValue(), String.valueOf(Charsets.UTF_8));
                    token = token.substring(JwtUtils.TOKEN_PREFIX.length(), token.length());
                }
            }
            if (token != null) {
                String userName = JwtUtils.getUserName(token);
                SysUser user = sysUserService.selectSysUserByUserName(userName);
                //判断输入的旧密码是否用户密码相等
                if (bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
                    //相等则修改密码
                    sysUserService.updatePwd(user.getUserId(),bCryptPasswordEncoder.encode(newPassword));
                    return AjaxResult.success();
                }
                return AjaxResult.error("输入的旧密码与登录密码不一致！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("修改出现异常！请检查后重试！");
        }
        return AjaxResult.error("请登录！");
    }

    /**
     * 获取路由信息
     * @return 路由信息
     */
    @GetMapping("/getRouters")
    public AjaxResult getRouters(HttpServletRequest request) {
        try {
            //从cookie中获取token信息，再将token信息转换为用户信息
            Cookie[] cookies = request.getCookies();
            String token = null;
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals(JwtUtils.TOKEN_HEADER)) {
                    token = URLDecoder.decode(cookies[i].getValue(), String.valueOf(Charsets.UTF_8));
                    token = token.substring(JwtUtils.TOKEN_PREFIX.length(), token.length());
                }
            }
            if (token != null) {
                String userName = JwtUtils.getUserName(token);
                SysUser user = sysUserService.selectSysUserByUserName(userName);
                SysUserRole sysUserRole = sysUserRoleService.selectSysUserRoleBySysUserId(user.getUserId());
                List<SysRoleMenu> sysRoleMenus = sysRoleMenuService.selectSysRoleMenuByRoleId(sysUserRole.getRoleId());
                List<SysMenu> menus = new ArrayList<>();
                for (SysRoleMenu roleMenu:sysRoleMenus) {
                    SysMenu menu = sysMenuService.selectByPrimaryKey(roleMenu.getMenuId());
                    if (menu.getParentId() == 0) {
                        menu.setChildren(sysMenuService.selectSysMenuChildren(menu.getMenuId()));
                        menus.add(menu);
                    }
                }
                return AjaxResult.success(sysMenuService.buildMenus(menus));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("系统异常，请稍后再试！");
        }
        return AjaxResult.success("请登录！");
    }

    @PostMapping("/logout")
    public AjaxResult logout() {
        return AjaxResult.success();
    }

}
