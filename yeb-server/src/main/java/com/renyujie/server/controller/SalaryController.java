package com.renyujie.server.controller;


import com.renyujie.server.pojo.RespBean;
import com.renyujie.server.pojo.Salary;
import com.renyujie.server.service.ISalaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author renyujie518
 * @since 2021-12-20
 */
@Api(tags = "SalaryController")
@RestController
@RequestMapping("/salary/sob")
public class SalaryController {
    @Resource
    private ISalaryService salaryService;

    @ApiOperation(value = "获取所有工资套账")
    @GetMapping("/")
    public List<Salary> getAllSalary() {
        return salaryService.list();
    }

    @ApiOperation(value = "添加工资套账")
    @PostMapping("/")
    public RespBean addSalary(@RequestBody Salary salary) {
        salary.setCreateDate(LocalDateTime.now());
        if (salaryService.save(salary)) {
            return RespBean.success("添加工资套账成功");
        }
        return RespBean.error("添加工资套账失败");
    }

    @ApiOperation(value = "删除工资套账")
    @DeleteMapping("/{id}")
    public RespBean deleteSalary(@PathVariable Integer id){
        if (salaryService.removeById(id)){
            return RespBean.success("删除工资套账成功");
        }
        return RespBean.error("删除工资套账失败");
    }

    @ApiOperation(value = "更新工资套账")
    @PutMapping("/")
    public RespBean updateSalary(@RequestBody Salary salary){
        if (salaryService.updateById(salary)){
            return RespBean.success("更新工资套账成功");
        }
        return RespBean.error("更新工资套账失败");
    }
}
