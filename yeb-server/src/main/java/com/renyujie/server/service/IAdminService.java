package com.renyujie.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.renyujie.server.pojo.Admin;
import com.renyujie.server.pojo.RespBean;

import javax.servlet.http.HttpServletRequest;

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
}
