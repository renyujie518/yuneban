package com.renyujie.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.renyujie.server.pojo.Menu;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author renyujie518
 * @since 2021-12-20
 */
public interface MenuMapper extends BaseMapper<Menu> {
    /**
     * @Description: 通过用户ID查询菜单列表
     */

    List<Menu> getMenusByAdminId(Integer id);


    /**
     * @Description: 根据角色获取菜单列表
     */
    List<Menu> getMenusWithRole();
}
