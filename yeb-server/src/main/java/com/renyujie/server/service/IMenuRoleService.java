package com.renyujie.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.renyujie.server.pojo.MenuRole;
import com.renyujie.server.pojo.RespBean;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author renyujie518
 * @since 2021-12-20
 */
public interface IMenuRoleService extends IService<MenuRole> {

    /**
     * @Description: 更新角色下所属的菜单
     */
    RespBean updateMenuRole(Integer rid, Integer[] mids);
}
