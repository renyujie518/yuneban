package com.renyujie.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.renyujie.server.pojo.MenuRole;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author renyujie518
 * @since 2021-12-20
 */
public interface MenuRoleMapper extends BaseMapper<MenuRole> {

    /**
     * @Description: 更新角色下所属的菜单
     * 传来的新的mids[]一一与rid这个角色关联起来
     */
    Integer insertMenus2Role(@Param("rid") Integer rid, @Param("mids") Integer[] mids);
}
