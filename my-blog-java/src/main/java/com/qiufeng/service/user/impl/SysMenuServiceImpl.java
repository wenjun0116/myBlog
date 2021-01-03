package com.qiufeng.service.user.impl;

import com.qiufeng.common.constant.UserConstants;
import com.qiufeng.common.utils.StringUtils;
import com.qiufeng.common.vo.MetaVo;
import com.qiufeng.common.vo.RouterVo;
import com.qiufeng.domain.SysMenu;
import com.qiufeng.mapper.user.SysMenuMapper;
import com.qiufeng.service.user.SysMenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 系统模块业务实现层
 */
@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Resource
    private SysMenuMapper menuMapper;

    /**
     * 删除模块
     * @param menuId
     * @return
     */
    @Override
    public int deleteByPrimaryKey(Long menuId) {
        return menuMapper.deleteByPrimaryKey(menuId);
    }

    /**
     *新增模块 不验证字段是否为空
     * @param record
     * @return
     */
    @Override
    public int insert(SysMenu record) {
        return menuMapper.insert(record);
    }

    /**
     * 新增模块 验证字段是否为空
     * @param record
     * @return
     */
    @Override
    public int insertSelective(SysMenu record) {
        return menuMapper.insertSelective(record);
    }

    /**
     * 根据ID查询模块
     * @param menuId
     * @return
     */
    @Override
    public SysMenu selectByPrimaryKey(Long menuId) {
        return menuMapper.selectByPrimaryKey(menuId);
    }

    /**
     * 修改模块 验证字段是否为空
     * @param record
     * @return
     */
    @Override
    public int updateByPrimaryKeySelective(SysMenu record) {
        return menuMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 修改模块 不验证字段是否为空
     * @param record
     * @return
     */
    @Override
    public int updateByPrimaryKey(SysMenu record) {
        return menuMapper.updateByPrimaryKey(record);
    }

    /**
     * 构建前端路由所需要的菜单
     * @param menus
     * @return
     */
    @Override
    public List<RouterVo> buildMenus(List<SysMenu> menus) {
        menus = getChildren(menus);
        LinkedList<RouterVo> routers = new LinkedList<>();
        for (SysMenu menu : menus) {
            if (menu.getPath() != null && !menu.getPath().equals("")) {
                RouterVo router = new RouterVo();
                router.setHidden("1".equals(menu.getVisible()));
                router.setName(getRouteName(menu));
                router.setPath(getRouterPath(menu));
                router.setComponent(getComponent(menu));
                router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon()));
                List<SysMenu> cMenus = menu.getChildren();
                if (!cMenus.isEmpty() && cMenus.size() > 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType()))
                {
                    router.setAlwaysShow(true);
                    router.setRedirect("noRedirect");
                    router.setChildren(buildMenus(cMenus));
                }
                else if (isMeunFrame(menu))
                {
                    ArrayList<RouterVo> childrenList = new ArrayList<>();
                    RouterVo children = new RouterVo();
                    children.setPath(menu.getPath());
                    children.setComponent(menu.getComponent());
                    children.setName(StringUtils.capitalize(menu.getPath()));
                    children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon()));
                    childrenList.add(children);
                    router.setChildren(childrenList);
                }
                routers.add(router);
            }
        }
        return routers;
    }

    /**
     * 根据父节点查询所有的子节点
     * @param parentId
     * @return
     */
    @Override
    public List<SysMenu> selectSysMenuChildren(Long parentId) {
        return menuMapper.selectSysMenuChildren(parentId);
    }

    /**
     * 获取路由名称
     *
     * @param menu 菜单信息
     * @return 路由名称
     */
    public String getRouteName(SysMenu menu)
    {
        String routerName = StringUtils.capitalize(menu.getPath());
        // 非外链并且是一级目录（类型为目录）
        if (isMeunFrame(menu))
        {
            routerName = StringUtils.EMPTY;
        }
        return routerName;
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(SysMenu menu)
    {
        String routerPath = menu.getPath();
        // 非外链并且是一级目录（类型为目录）
        if (menu.getParentId().intValue() == 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType())
                && UserConstants.NO_FRAME.equals(menu.getIsFrame().toString()))
        {
            routerPath = "/" + menu.getPath();
        }
        // 非外链并且是一级目录（类型为菜单）
        else if (isMeunFrame(menu))
        {
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    public String getComponent(SysMenu menu)
    {
        String component = UserConstants.LAYOUT;
        if (StringUtils.isNotEmpty(menu.getComponent()) && !isMeunFrame(menu))
        {
            component = menu.getComponent();
        }
        return component;
    }

    /**
     * 是否为菜单内部跳转
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isMeunFrame(SysMenu menu)
    {
        return menu.getParentId().intValue() == 0 && UserConstants.TYPE_MENU.equals(menu.getMenuType())
                && menu.getIsFrame().equals(UserConstants.NO_FRAME);
    }

    /**
     * 递归获取子孙节点
     * @return
     */
    public List<SysMenu> getChildren(List<SysMenu> menus) {
        for (SysMenu menu:menus) {
            List<SysMenu> childrenMenus = menuMapper.selectSysMenuChildren(menu.getMenuId());
            if (childrenMenus.size() != 0) {
                menu.setChildren(childrenMenus);
                getChildren(menu.getChildren());
            }
        }
        return menus;
    }

    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list 分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<SysMenu> getChildPerms(List<SysMenu> list, int parentId)
    {
        List<SysMenu> returnList = new ArrayList<SysMenu>();
        for (Iterator<SysMenu> iterator = list.iterator(); iterator.hasNext();)
        {
            SysMenu t = (SysMenu) iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId() == parentId)
            {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private void recursionFn(List<SysMenu> list, SysMenu t)
    {
        // 得到子节点列表
        List<SysMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenu tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu t)
    {
        List<SysMenu> tlist = new ArrayList<SysMenu>();
        Iterator<SysMenu> it = list.iterator();
        while (it.hasNext())
        {
            SysMenu n = (SysMenu) it.next();
            if (n.getParentId().longValue() == t.getMenuId().longValue())
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysMenu> list, SysMenu t)
    {
        return getChildList(list, t).size() > 0 ? true : false;
    }

}
