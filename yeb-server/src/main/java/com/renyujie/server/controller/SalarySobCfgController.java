package com.renyujie.server.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.renyujie.server.pojo.Employee;
import com.renyujie.server.pojo.RespBean;
import com.renyujie.server.pojo.RespPageBean;
import com.renyujie.server.pojo.Salary;
import com.renyujie.server.service.IEmployeeService;
import com.renyujie.server.service.ISalaryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author renyujie518
 * @version 1.0.0
 * @ClassName SalarySobCfgController.java
 * @Description 员工套账
 * @createTime 2022年01月05日 20:46:00
 */
@RequestMapping("/salary/sobcfg")
@RestController
public class SalarySobCfgController {
    @Autowired
    private ISalaryService salaryService;
    @Autowired
    private IEmployeeService employeeService;

    @ApiOperation(value = "获取所有员工套账")
    @GetMapping("/")
    public RespPageBean getEmployeeWithSalary(@RequestParam(defaultValue = "1") Integer currentPage,
                                              @RequestParam(defaultValue = "10") Integer size){
        return employeeService.getEmployeeWithSalary(currentPage,size);
    }

    @ApiOperation(value = "获取所有工资套账")
    @GetMapping("/salaries")
    public List<Salary> getAllSalary(){
        return salaryService.list();
    }

    @ApiOperation(value = "更新员工套账")
    @PutMapping("/")
    public RespBean updateEmployeeSalary(Integer eid, Integer sid){
        if (employeeService.update(new UpdateWrapper<Employee>().set("salaryId",sid).eq("id",eid))){
            return RespBean.success("更新员工套账成功");
        }
        return RespBean.error("更新员工套账失败");
    }

}
