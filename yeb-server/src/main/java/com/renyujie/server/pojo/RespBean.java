package com.renyujie.server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author renyujie518
 * @version 1.0.0
 * @ClassName RespBean.java
 * @Description 统一返回对象
 * @createTime 2021年12月21日 15:34:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespBean {
    private long code;
    private String message;
    private Object obj;

    public static RespBean success(String message){
        return new RespBean(200,message,null);
    }

    public static RespBean success(String message,Object obj){
        return new RespBean(200,message,obj);
    }

    public static RespBean error(String message){
        return new RespBean(500,message,null);
    }

    public static RespBean error(String message,Object obj){
        return new RespBean(500,message,obj);
    }
}
