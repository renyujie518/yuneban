package com.renyujie.server.controller;

import com.renyujie.server.pojo.Admin;
import com.renyujie.server.pojo.AdminLogin;
import com.renyujie.server.pojo.RespBean;
import com.renyujie.server.service.IAdminService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * @author renyujie518
 * @version 1.0.0
 * @ClassName AdminLoginController.java
 * @Description 登录
 * @createTime 2021年12月21日 17:12:00
 */
@RestController
public class AdminLoginController {

    @Autowired
    private IAdminService adminService;


    /**
     * @Description: 用户登录并返回token
     */
    @ApiOperation(value = "登录之后返回token给前端")
    @PostMapping("/login")
    public RespBean login(@RequestBody AdminLogin adminLogin, HttpServletRequest request) {
        return adminService.login(adminLogin.getUsername(), adminLogin.getPassword(), adminLogin.getCode(), request);
    }


    /**
     * @Description: 登出
     * 这里的登出逻辑只是返给前端200code,原因是登出删token的逻辑在前端处理
     */
    @ApiOperation(value = "登出")
    @PostMapping("/logout")
    public RespBean logout(){
        return RespBean.success("注销成功");
    }



    /**
     * @Description: 获取当前登录用户信息
     */
    @ApiOperation(value = "获取当前登录用户信息")
    @GetMapping("/admin/info")
    public Admin getAdminInfo(Principal principal) {
        if (principal == null) {
            return null;
        }
//        为什么可以直接从principal中获取，原因就是在AdminServiceImpl.login下做了"更新安全上下文"的操作
        String username = principal.getName();
        Admin admin = adminService.getAdminByName(username);
        //密码不必给前端给
        admin.setPassword(null);
        //把该用户所属的角色（职位）也给前端
        admin.setRoles(adminService.getRolesByAdminId(admin.getId()));
        return admin;

    }




}
