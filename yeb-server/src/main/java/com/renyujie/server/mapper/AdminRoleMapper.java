package com.renyujie.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.renyujie.server.pojo.AdminRole;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author renyujie518
 * @since 2021-12-20
 */
public interface AdminRoleMapper extends BaseMapper<AdminRole> {
    /**
     * @Description: 添加操作员角色
     */

    Integer addRole(@Param("adminId") Integer adminId, @Param("rids") Integer[] rids);
}
