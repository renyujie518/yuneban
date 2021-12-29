package com.renyujie.server.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author renyujie518
 * @since 2021-12-20
 */
@Data
@TableName("t_nation")
@ApiModel(value="Nation对象", description="")
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false,of = "name")
public class Nation implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "民族")
    @NonNull
    @Excel(name = "民族")
    private String name;


}
