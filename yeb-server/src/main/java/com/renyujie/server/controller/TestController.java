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
}
