package com.renyujie.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.renyujie.server.mapper.DepartmentMapper;
import com.renyujie.server.pojo.Department;
import com.renyujie.server.service.IDepartmentService;
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
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

}
