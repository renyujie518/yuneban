package com.renyujie.server.utils;

import com.renyujie.server.pojo.Admin;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author renyujie518
 * @version 1.0.0
 * @ClassName AdminUtils.java
 * @Description 操作员工具类
 * @createTime 2021年12月27日 15:03:00
 */
public class AdminUtils {
    /**
     * @Description: 获取当前登录的操作员
     */
    public static Admin getCurrentAdmin(){
        return (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
