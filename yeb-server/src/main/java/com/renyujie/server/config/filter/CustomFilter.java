package com.renyujie.server.config.filter;

import com.renyujie.server.pojo.Menu;
import com.renyujie.server.pojo.Role;
import com.renyujie.server.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;

/**
 * @author renyujie518
 * @version 1.0.0
 * @ClassName CustomFilter.java
 * @Description  权限控制
 * 根据请求url(在t_menu中有对应的url字段用于辅助判断菜单在那哪些url下是可以看到的)分析出请求所需角色
 * @createTime 2021年12月25日 22:31:00
 */
@Component
public class CustomFilter implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private IMenuService menuService;
    AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        //获取请求的url
        String requestUrl = ((FilterInvocation) o).getRequestUrl();
        //System.out.println("requestUrl = " + requestUrl);
        //获取所有的菜单所对应的角色
        List<Menu> menusWithRole = menuService.getMenusWithRole();
        for (Menu menu : menusWithRole) {
            //判断请求的url与菜单所对应的角色是否存在匹配
            // 菜单的角色信息已经在Menu对象中了  List<Role> roles  其中RolesName字段诸如ROLE_manager
            if (antPathMatcher.match(menu.getUrl(), requestUrl)) {
                //System.out.println("menuUrl = " + menu.getUrl());
                String[] RolesName2Array = menu.getRoles().stream().map(Role::getName).toArray(String[]::new);
                //System.out.println("RolesName2Array = " + RolesName2Array);
                return SecurityConfig.createList(RolesName2Array);
            }
        }
        //没匹配的url默认为登录即可访问
        return SecurityConfig.createList("ROLE_LOGIN");


    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }
}
