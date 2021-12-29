package com.renyujie.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.renyujie.server.pojo.Employee;
import com.renyujie.server.pojo.RespBean;
import com.renyujie.server.pojo.RespPageBean;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author renyujie518
 * @since 2021-12-20
 */
public interface IEmployeeService extends IService<Employee> {
    /**
     * @Description: 获取所有员（分页）
     */

    RespPageBean getEmployeeByPage(Integer currentPage, Integer size, Employee employee, LocalDate[] beginDateScope);

    /**
     * @Description: 根据id获取员工
     */
    List<Employee> getEmployeeById(Integer id);



    /**
     * @Description: 获取最大工号
     */
    RespBean maxWorkId();

    
    /**
     * @Description: 添加员工
     */
    RespBean insertEmployee(Employee employee);


}
