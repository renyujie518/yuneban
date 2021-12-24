package com.renyujie.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.renyujie.server.config.jwt.JwtTokenUtil;
import com.renyujie.server.mapper.AdminMapper;
import com.renyujie.server.pojo.Admin;
import com.renyujie.server.pojo.RespBean;
import com.renyujie.server.service.IAdminService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author renyujie518
 * @since 2021-12-20
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Value("$jwt.tokenHead")
    private String tokenHead;
    @Resource
    private AdminMapper adminMapper;


    /**
     * @Description: 登陆之后返回token
     * 传入的参数是前端传来的用户名与密码
     */
    @Override
    public RespBean login(String username, String password, String code, HttpServletRequest request) {

        //获取userDetails
        //实际上登录用的就是userDetailsService.loadUserByUsername
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (null == userDetails || passwordEncoder.matches(password, userDetails.getPassword())) {
            return RespBean.error("用户名或者密码不正确");
        }
        if (!userDetails.isEnabled()) {
            return RespBean.error("账号被禁用，请联系管理员");
        }
        //验证验证码 因为之前在/captcha接口中返给前端的text是放在session中的
        String captcha = (String) request.getSession().getAttribute("captcha");
        if (StringUtils.isEmpty(code) || !captcha.equals(code)) {
            return RespBean.error("验证码填写错误");
        }
        //更新security登录用户对象(围绕该用户建立安全上下文，放details，密码一般不放,放权限列表)
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,
                null,
                userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);


        //到这里说明登录成功，再根据userDetails拿到token返给前端
        String token = jwtTokenUtil.generateTokenByUserDetails(userDetails);
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return RespBean.success("登录成功", tokenMap);

    }

    /**
     * @Description: 通过username获取用户信息
     */
    @Override
    public Admin getAdminByName(String username) {
        Admin admin = adminMapper.selectOne(new QueryWrapper<Admin>().eq("username", username)
                .eq("enabled", true));
        if (admin == null) {
            return null;
        }
        return admin;
    }
}
