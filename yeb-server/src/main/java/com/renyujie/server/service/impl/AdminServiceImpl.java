package com.renyujie.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.renyujie.server.config.jwt.JwtTokenUtil;
import com.renyujie.server.mapper.AdminMapper;
import com.renyujie.server.mapper.AdminRoleMapper;
import com.renyujie.server.mapper.RoleMapper;
import com.renyujie.server.pojo.Admin;
import com.renyujie.server.pojo.AdminRole;
import com.renyujie.server.pojo.RespBean;
import com.renyujie.server.pojo.Role;
import com.renyujie.server.service.IAdminService;
import com.renyujie.server.utils.AdminUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
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
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private AdminRoleMapper adminRoleMapper;


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


    /**
     * @Description: 根据用户id查询该用户所对应的角色
     */
    @Override
    public List<Role> getRolesByAdminId(Integer adminId) {
        return roleMapper.getRolesByAdminId(adminId);
    }


    /**
     * @Description: 获取所有操作员
     * keywords前端传来（即搜索框的输入） 同时还有个基本要求是搜到的操作员应该是非本人之外的所有，所以还有currentAdminID
     */
    @Override
    public List<Admin> getAllAdmins(String keywords) {
        return adminMapper.getAllAdmin(AdminUtils.getCurrentAdmin().getId(), keywords);
    }


    /**
     * @Description: 更新操作员角色
     * 先删除该操作员下的所有角色（其实就是把这个adminId对应的操作员先删了） 再为这个操作员依次添加新角色rids
     */
    @Override
    public RespBean updateAdminRole(Integer adminId, Integer[] rids) {
        adminRoleMapper.delete(new QueryWrapper<AdminRole>().eq("adminId",adminId));
        Integer result = adminRoleMapper.addRole(adminId, rids);
        //如果操作次数 = 新角色rids 代表新角色一一添加成功
        if (rids.length == result){
            return RespBean.success("更新操作员角色成功");
        }
        return RespBean.error("更新操作员角色失败");

    }

    /**
     * @Description: 更新用户密码
     */
    @Override
    public RespBean updatePassword(String oldPass, String newPass, Integer adminId) {
        Admin admin = adminMapper.selectById(adminId);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(oldPass, admin.getPassword())) {
            //旧密码输入正确才允许跟新新密码
            admin.setPassword(encoder.encode(newPass));
            //再把新对象更新
            int res = adminMapper.updateById(admin);
            if (1 == res) {
                return RespBean.success("更新用户密码成功");
            }
        }
        return RespBean.error("更新用户密码失败");
    }
}
