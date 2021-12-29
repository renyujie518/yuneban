package com.renyujie.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.renyujie.server.mapper.EmployeeMapper;
import com.renyujie.server.pojo.Employee;
import com.renyujie.server.pojo.RespBean;
import com.renyujie.server.pojo.RespPageBean;
import com.renyujie.server.service.IEmployeeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author renyujie518
 * @since 2021-12-20
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {
    @Resource
    private EmployeeMapper employeeMapper;

    @Override
    public RespPageBean getEmployeeByPage(Integer currentPage,
                                      Integer size,
                                      Employee employee,
                                      LocalDate[] beginDateScope) {
    //    开启分页
        Page<Employee> page = new Page<>(currentPage, size);
        IPage<Employee> employeeIPage = employeeMapper.getEmployeeByPage(page, employee, beginDateScope);
        RespPageBean respPageBean = new RespPageBean(employeeIPage.getTotal(), employeeIPage.getRecords());
        return respPageBean;
    }

    /**
     * @Description: 获取最大工号
     * 首先利用max函数会得到"max(workID)"这一列 由于用的是selectmap,list中只有1个hashmap,再从map中get到键 这个键就是max(workID)
     * 由于在数据库中存的是char类型，所以转换后+1，再按照格式转化为string，直接放到RespBean的data中
     * %08d  一共8位  前面不足的0补齐
     */
    @Override
    public RespBean maxWorkId() {
        List<Map<String, Object>> resultList = employeeMapper.selectMaps(new QueryWrapper<Employee>().select("max(workID)"));
        return RespBean.success(null,
                String.format("%08d", Integer.parseInt(resultList.get(0).get("max(workID)").toString()) + 1));
    }

    /**
     * @Description: 添加员工
     * 这里没有直接用mapper的insert函数  因为表中的一项合同时间是通过"合同结束-开始时间"计算得到的,新添加员工的时候只会填入合同结束和开始时间
     */
    @Override
    public RespBean insertEmployee(Employee employee) {
        // 处理合同期限，保留2位小数
        LocalDate beginContract = employee.getBeginContract();
        LocalDate endContract = employee.getEndContract();
        //利用LocalDate的工具函数 计算两者的距离 单位为天
        long days = beginContract.until(endContract, ChronoUnit.DAYS);
        //合同时间精确到两位
        DecimalFormat decimalFormat = new DecimalFormat("##.00");
        employee.setContractTerm(Double.parseDouble(decimalFormat.format(days / 365.00)));
        if (employeeMapper.insert(employee) == 1) {
            return RespBean.success("添加员工成功");
        }
        return RespBean.error("添加员工失败");

    }
}
