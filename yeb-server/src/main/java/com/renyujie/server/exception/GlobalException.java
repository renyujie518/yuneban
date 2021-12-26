package com.renyujie.server.exception;

import com.renyujie.server.pojo.RespBean;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @author renyujie518
 * @version 1.0.0
 * @ClassName GlobalException.java
 * @Description 全局异常
 * @createTime 2021年12月26日 15:17:00
 */
@RestControllerAdvice
public class GlobalException {
    public RespBean mySqlException(SQLException e) {
        if (e instanceof SQLIntegrityConstraintViolationException) {
            return RespBean.error("该数据有外键关联，操作失败！");
        }
        return RespBean.error("数据库异常，操作失败！");
    }
}
