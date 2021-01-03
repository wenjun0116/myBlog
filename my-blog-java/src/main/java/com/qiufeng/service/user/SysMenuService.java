package com.qiufeng.service.user;

import com.qiufeng.common.vo.RouterVo;
import com.qiufeng.domain.SysMenu;

import java.util.List;

/**
 * 系统模块业务层
 */
public interface SysMenuService {

    /**
     * 删除模块
     * @param menuId
     * @return
     */
    int deleteByPrimaryKey(Long menuId);

    /**
     *新增模块 不验证字段是否为空
     * @param record
     * @return
     */
    int insert(SysMenu record);

    /**
     * 新增模块 验证字段是否为空
     * @param record
     * @return
     */
    int insertSelective(SysMenu record);

    /**
     * 根据ID查询模块
     * @param menuId
     * @return
     */
    SysMenu selectByPrimaryKey(Long menuId);

    /**
     * 修改模块 验证字段是否为空
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(SysMenu record);

    /**
     * 修改模块 不验证字段是否为空
     * @param record
     * @return
     */
    int updateByPrimaryKey(SysMenu record);

    /**
     * 构建前端路由所需要的菜单
     * @param menus
     * @return
     */
    public List<RouterVo> buildMenus(List<SysMenu> menus);

    /**
     * 根据父节点查询所有的子节点
     * @param parentId
     * @return
     */
    List<SysMenu> selectSysMenuChildren(Long parentId);

}
