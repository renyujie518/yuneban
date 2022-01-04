package com.renyujie.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.renyujie.server.pojo.Employee;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author renyujie518
 * @since 2021-12-20
 */
public interface EmployeeMapper extends BaseMapper<Employee> {
    /**
     * @Description: 获取所有员工（分页）
     */

    IPage<Employee> getEmployeeByPage(Page<Employee> page, @Param("employee") Employee employee, @Param("beginDateScope") LocalDate[] beginDateScope);


    /**
     * @Description: 根据id获取员工 不传id就是获取所有员工 所以返回list 传入id list只会有一个
     */
    List<Employee> getEmployeeById(Integer id);
}
