package com.renyujie.server.controller;

import com.renyujie.server.pojo.Admin;
import com.renyujie.server.service.IAdminService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author renyujie518
 * @version 1.0.0
 * @ClassName ChatController.java
 * @Description 查询目前用户在和谁聊天
 * @createTime 2022年01月05日 22:27:00
 */
@RestController
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private IAdminService adminService;

    @ApiOperation(value = "获取所有操作员")
    @GetMapping("/admin")
    public List<Admin> getAllAdmin(String keywords) {
        return adminService.getAllAdmins(keywords);
    }

}
