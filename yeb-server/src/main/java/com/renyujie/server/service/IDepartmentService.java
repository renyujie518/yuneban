package com.renyujie.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.renyujie.server.pojo.Department;
import com.renyujie.server.pojo.RespBean;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author renyujie518
 * @since 2021-12-20
 */
public interface IDepartmentService extends IService<Department> {

    /**
     * @Description: 获取所有部门
     */
    List<Department> getAllDepartments();

    /**
     * @Description: 添加部门
     */
    RespBean addDep(Department department);
    /**
     * @Description: 删除部门
     */
    RespBean deleteDep(Integer id);
}
