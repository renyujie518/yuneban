package com.renyujie.server.config.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author renyujie518
 * @version 1.0.0
 * @ClassName jwtAuthenticationTokenFilter.java
 * @Description jwt登录授权过滤器
 * @createTime 2021年12月21日 22:27:00
 */
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    //Authorization
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    //token开头 Bearer
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader(tokenHeader);
        //存在header
        if (authHeader != null && authHeader.startsWith(tokenHead)) {
            //取出token substring左开右闭 单独参数是begin的位置 所以刚好是把Bearer的标识头去了
            String authToken = authHeader.substring(tokenHead.length());
            String userName = jwtTokenUtil.getUserNameFromToken(authToken);
            //token中有用户名却未登录
            if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                //生成userDetails就算登录（注意 在这里是调用的重写的根据username返回用户信息的）
                UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
                //有效还要跟新用户对象
                if (jwtTokenUtil.validToken(authToken,userDetails)){
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        //放行
        filterChain.doFilter(request,response);
    }
}
