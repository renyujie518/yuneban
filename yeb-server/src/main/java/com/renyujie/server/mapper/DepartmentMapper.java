package com.renyujie.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.renyujie.server.pojo.Department;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author renyujie518
 * @since 2021-12-20
 */
public interface DepartmentMapper extends BaseMapper<Department> {
    /**
     * @Description: 获取所有部门
     */
    List<Department> getAllDepartmentsByParentId(int parentId);

    /**
     * @Description: 添加部门（注意 传给mapper的是个department对象，mybatis会自动get属性并做转换然后传参）
     */
    void addDep(Department department);

    /**
     * @Description: 删除部门（注意 传给mapper的是个department对象，mybatis会自动get属性并做转换然后传参）
     */
    void deleteDep(Department department);
}
