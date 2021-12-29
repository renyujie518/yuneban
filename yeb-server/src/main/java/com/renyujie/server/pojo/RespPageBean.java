package com.renyujie.server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author renyujie518
 * @version 1.0.0
 * @ClassName RespPageBean.java
 * @Description 分页结果同一封装
 * @createTime 2021年12月28日 20:55:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespPageBean {
    //返回条数
    private Long total;
    //数据
    private List<?> data;
}
