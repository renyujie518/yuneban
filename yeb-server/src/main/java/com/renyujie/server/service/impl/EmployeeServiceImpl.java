package com.renyujie.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.renyujie.server.mapper.EmployeeMapper;
import com.renyujie.server.pojo.Employee;
import com.renyujie.server.service.IEmployeeService;
import org.springframework.stereotype.Service;

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

}
