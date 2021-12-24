package com.renyujie.server.config.jwt;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author renyujie518
 * @version 1.0.0
 * @ClassName JwtTokenUtil.java
 * @Description wtToken的工具类
 * @createTime 2021年12月20日 19:54:00
 */

@Component
public class JwtTokenUtil {
    //荷载claim的名称
    private static final String CLAIM_KEY_USERNAME = "sub";
    //荷载的创建时间
    private static final String CLAIM_KEY_CREATED = "created";
    // jwt令牌的秘钥
    @Value("${jwt.secret}")
    private String secret;
    //jwt的失效时间
    @Value("${jwt.expiration}")
    private Long expiration;


    /**
     * @Description: 根据用户信息生成token
     */
    public String generateTokenByUserDetails(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateTokenByClaim(claims);

    }

    /**
     * @Description: 通过claims生成Token
     */
    private String generateTokenByClaim(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpiration())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();


    }

    /**
     * @Description: 生成token失效时间（当前时间+配置的时间）
     */
    private Date generateExpiration() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
        
    }

    /**
     * @Description: 从token中拿到用户名
     */
    public String getUserNameFromToken(String token) {
        String userName;
        try {
            Claims claims = getClaimFromToken(token);
            userName = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            //有异常的话userName置为空
            userName = null;
        }
        return userName;

    }
    /**
     * @Description: 从token中获取claim
     */

    private Claims getClaimFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return claims;

    }

    /**
     * @Description: 判断token是否过期  主要从token是否过期&userdital的用户名与token中的是否一致
     */
    public boolean validToken(String token, UserDetails userDetails) {
        String userName = getUserNameFromToken(token);
        return userName.equals(userDetails.getUsername()) && !isTokenExpired(token);

    }
    /**
     * @Description: token是否过期
     */
    private boolean isTokenExpired(String token) {
        Date expireDate = getExpiredDataFromToken(token);
        //token中带的失效时间在当前时间之前就是失效的
        return expireDate.before(new Date());


    }

    /**
     * @Description: 获取token的失效时间
     */
    private Date getExpiredDataFromToken(String token) {
        Claims claims = getClaimFromToken(token);
        return claims.getExpiration();

    }

    /**
     * @Description: 判断toke是否可以被刷新（已过期就可以被刷新）
     */
    public boolean canRefresh(String token) {
        return !isTokenExpired(token);

    }

    /**
     * @Description: 刷新token(对CLAIM_KEY_CREATED赋予当前时间)
     */
    public String refreshToken(String token) {
        Claims claims = getClaimFromToken(token);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateTokenByClaim(claims);
    }



}
