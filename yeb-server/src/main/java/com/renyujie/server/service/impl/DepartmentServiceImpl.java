package com.renyujie.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.renyujie.server.mapper.DepartmentMapper;
import com.renyujie.server.pojo.Department;
import com.renyujie.server.pojo.RespBean;
import com.renyujie.server.service.IDepartmentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author renyujie518
 * @since 2021-12-20
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {
    @Resource
    private DepartmentMapper departmentMapper;
/**
 * @Description: 获取所有部门
 */
    @Override
    public List<Department> getAllDepartments() {
        return departmentMapper.getAllDepartmentsByParentId(-1);

    }

    /**
     * @Description: 添加部门
     */
    @Override
    public RespBean addDep(Department department) {
    //    要添加肯定是要启用的
        department.setEnabled(true);
        departmentMapper.addDep(department);
        if (1 == department.getResult()){
            return RespBean.success("添加成功",department);
        }
        return RespBean.error("添加失败");
    }

    /**
     * @Description: 删除部门
     */
    @Override
    public RespBean deleteDep(Integer id) {
        //主要是接收result的出参 同时数据过程中的IN要求id
        Department department = new Department();
        department.setId(id);
        departmentMapper.deleteDep(department);
        //先子部门后员工
        if (-2 == department.getResult()){
            return RespBean.error("该部门下有子部门，删除失败！");
        }
        if (-1 == department.getResult()){
            return RespBean.error("该部门下有员工，删除失败！");
        }
        if (1 == department.getResult()){
            return RespBean.success("删除成功！");
        }
        return RespBean.error("删除失败");
    }
}
