package com.renyujie.server.controller;


import com.renyujie.server.pojo.Menu;
import com.renyujie.server.service.IAdminService;
import com.renyujie.server.service.IMenuService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author renyujie518
 * @since 2021-12-20
 */
@RestController
@RequestMapping("/system/cfg")
//根据数据库，菜单的接口是放在"系统管理"的路径下，对应就是"/system/cfg"
public class MenuController {
    @Autowired
    private IMenuService menuService;


    @ApiOperation(value = "通过用户ID查询菜单列表")
    @GetMapping("/menu")
    public List<Menu> getMenusByAdminId(){
        //为了防止id被篡改  所以前端虽然是get请求，但是需要后端从全局对象中获取
        return menuService.getMenusByAdminId();
    }

}
