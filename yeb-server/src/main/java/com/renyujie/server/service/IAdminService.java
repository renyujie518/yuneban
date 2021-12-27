package com.renyujie.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.renyujie.server.pojo.Admin;
import com.renyujie.server.pojo.RespBean;
import com.renyujie.server.pojo.Role;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author renyujie518
 * @since 2021-12-20
 */
public interface IAdminService extends IService<Admin> {
    /**
     * @Description: 登陆之后返回token
     */
    RespBean login(String username, String password, String code, HttpServletRequest request);

    /**
     * @Description: 从数据库中通过username获取用户信息
     */
    Admin getAdminByName(String username);

    /**
     * @Description: 根据用户id查询该用户所对应的角色
     */
    List<Role> getRolesByAdminId(Integer adminId);

    /**
     * @Description: 获取所有操作员
     */
    List<Admin> getAllAdmins(String keywords);

    /**
     * @Description: 更新操作员角色
     */
    RespBean updateAdminRole(Integer adminId, Integer[] rids);
}
