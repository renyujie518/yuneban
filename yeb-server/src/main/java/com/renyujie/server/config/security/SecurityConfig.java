package com.renyujie.server.config.security;

import com.renyujie.server.config.jwt.JwtAuthenticationTokenFilter;
import com.renyujie.server.config.jwt.RestAuthorizationEntryPoint;
import com.renyujie.server.config.jwt.RestfulAccessDeniedHandler;
import com.renyujie.server.pojo.Admin;
import com.renyujie.server.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * @author renyujie518
 * @version 1.0.0
 * @ClassName SecurityConfig.java
 * @Description Security配置类
 * @createTime 2021年12月21日 21:52:00
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private IAdminService adminService;
    @Autowired
    private RestAuthorizationEntryPoint restAuthorizationEntryPoint;
    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;




    /**
     * @Description: 重写userDetailsService.loadUserByUsername(userName)方法
     * 实际上是在数据库中根据username返回用户信息（这样等同于登录）
     */
    @Override
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            //从数据库中获取user信息
            Admin admin = adminService.getAdminByName(username);
            if (admin != null) {
                return admin;
            }
            return null;
        };
    }

    /**
     * @Description: 重写configure是为让security认得重写的UserDetailsService
     */

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());

    }


    /**
     * @Description: 密码encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * @Description: 真正重写security配置的地方
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                //使用jwt,不需要csrf
                .disable()
                //基于token不需要session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //允许登录时访问的接口
                .authorizeRequests()
                .antMatchers("/login", "/logout")
                //上述接口生效
                .permitAll()
                //除上述接口，都要认证
                .anyRequest()
                .authenticated()
                .and()
                //没有用到缓存
                .headers()
                .cacheControl();
        //添加jwt登录授权过滤器
        http.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        //添加自定义未授权结果返回
        http.exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)
                .authenticationEntryPoint(restAuthorizationEntryPoint);
    }

    /**
     * @Description: 配置jwt登录授权过滤器的bean
     */
    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() {
        return new JwtAuthenticationTokenFilter();
    }
}
