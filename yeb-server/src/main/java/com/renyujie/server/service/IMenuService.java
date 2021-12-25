package com.renyujie.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.renyujie.server.pojo.Menu;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author renyujie518
 * @since 2021-12-20
 */
public interface IMenuService extends IService<Menu> {
/**
 * @Description: 通过用户ID查询菜单列表
 */
    List<Menu> getMenusByAdminId();
    
    /**
     * @Description: 根据角色获取菜单列表
     */
    List<Menu> getMenusWithRole();
}
