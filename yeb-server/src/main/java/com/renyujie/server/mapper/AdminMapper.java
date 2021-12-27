package com.renyujie.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.renyujie.server.pojo.Admin;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author renyujie518
 * @since 2021-12-20
 */
public interface AdminMapper extends BaseMapper<Admin> {

    /**
     * @Description: 获取所有操作员
     */
    List<Admin> getAllAdmin(@Param("currentAdminId") Integer currentAdminId, @Param("keywords") String keywords);
}
