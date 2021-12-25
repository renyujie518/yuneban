package com.renyujie.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.renyujie.server.pojo.Role;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author renyujie518
 * @since 2021-12-20
 */
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * @Description: 根据用户id查询该用户所对应的角色
     */
    List<Role> getRolesByAdminId(Integer adminId);
}
