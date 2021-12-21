package com.renyujie.server.pojo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author renyujie518
 * @version 1.0.0
 * @ClassName AdminLogin.java
 * @Description 用户登录实体类（需要前端传来的就这些，类似VO）
 * @createTime 2021年12月21日 15:51:00
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Api(value = "AdminLogin对象")
public class AdminLogin {

    @ApiModelProperty(value = "用户名",required = true)
    private String username;
    @ApiModelProperty(value = "密码",required = true)
    private String password;
    @ApiModelProperty(value = "验证码",required = true)
    private String code;

}
