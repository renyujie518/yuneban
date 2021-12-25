package com.renyujie.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author renyujie518
 * @version 1.0.0
 * @ClassName TestController.java
 * @Description 测试接口
 * @createTime 2021年12月23日 18:09:00
 */
@RestController
public class TestController {
    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }


    /**
     * @Description: 测试 在t_menu菜单表中，规定了用户角色为admin的话，可以查询到基本资料 用于验证"根据url判断角色"
     */
    @GetMapping("/employee/basic/hello")
    public String hello2(){
        return "/employee/basic/hello";
    }

    /**
     * @Description: 测试 在t_menu菜单表中，规定了用户角色为admin的话，不可以查询到高级资料 用于验证"根据url判断角色"
     */
    @GetMapping("/employee/advanced/hello")
    public String hello3(){
        return "/employee/advanced/hello";
    }
}
