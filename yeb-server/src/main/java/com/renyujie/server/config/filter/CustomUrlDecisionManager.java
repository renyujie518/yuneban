package com.renyujie.server.config.filter;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @author renyujie518
 * @version 1.0.0
 * @ClassName CustomUrlDecisionManager.java
 * @Description 权限控制
 * 判断用户角色（职位）
 * 根据登录用户去判断这个用户所属的角色（即职位 一对多 身兼数职）
 * @createTime 2021年12月25日 23:28:00
 */
@Component
public class CustomUrlDecisionManager implements AccessDecisionManager {
    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> configAttributes)
            throws AccessDeniedException, InsufficientAuthenticationException {

        for (ConfigAttribute configAttribute : configAttributes) {
            //访问当前url所需的角色
            String needRole = configAttribute.getAttribute();
            //判断角色是否为登录即可访问的角色,ROLE_LOGIN  是在CustomFilter中配置的登入后的默认角色
            if ("ROLE_LOGIN".equals(needRole)){
                //判断登录
                if (authentication instanceof AnonymousAuthenticationToken){
                    throw new AccessDeniedException("CustomUrlDecisionManager : 尚未登录,请登录!");
                }else {
                    return;
                }
            }
//            到这里说明该用户已经登入，但角色不是ROLE_LOGIN
            //判断角色是否为url所需角色（做匹配）
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                if (authority.getAuthority().equals(needRole)){
                    return;
                }
            }
        }
//        整体都不行说明到这里都没配到
        throw new AccessDeniedException("CustomUrlDecisionManager : 权限不足!");

    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return false;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }
}
