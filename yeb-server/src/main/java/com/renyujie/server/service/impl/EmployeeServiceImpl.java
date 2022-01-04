package com.renyujie.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.renyujie.server.mapper.EmployeeMapper;
import com.renyujie.server.mapper.MailLogMapper;
import com.renyujie.server.pojo.*;
import com.renyujie.server.service.IEmployeeService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Resource
    private MailLogMapper mailLogMapper;

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
     * @Description: 根据id获取员工 不传id就是获取所有员工 所以返回list 传入id list只会有一个
     * @return
     */
    @Override
    public List<Employee> getEmployeeById(Integer id) {

        return employeeMapper.getEmployeeById(id);
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
     *
     * 附属功能：添加新员工后给员工发送一个欢迎邮件
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
            //插入成功后获取新employee信息并发送邮件
            List<Employee> employeeList = employeeMapper.getEmployeeById(employee.getId());
            //传入id list只会有一个  获取到刚刚插入的员工对象 再使用rabbitmq发送（具体原因是邮件中包含了员工表格）
            Employee emp = employeeList.get(0);
            /**1.发送消息前要先消息入库（详见文档-消息可靠性一节） t_mail_log**/
            //消息id  全局唯一
            String msgId = UUID.randomUUID().toString();
            //对应t_mail_log表生成的MailLog对象
            MailLog mailLog = new MailLog();
            mailLog.setMsgId(msgId);
            mailLog.setEid(emp.getId());
            //消息发送前的消息状态初始化为0 消息投递中
            mailLog.setStatus(MailConstants.MSG_DELIVERING);
            mailLog.setRouteKey(MailConstants.MAIL_ROUTING_KEY_NAME);
            mailLog.setExchange(MailConstants.MAIL_EXCHANGE_NAME);
            //重试次数初始设置为0
            mailLog.setCount(0);
            //重试时间 是当前时间往后加1min
            mailLog.setTryTime(LocalDateTime.now().plusMinutes(MailConstants.MSG_TIMEOUT));
            mailLog.setCreateTime(LocalDateTime.now());
            mailLog.setUpdateTime(LocalDateTime.now());
            //消息落库
            mailLogMapper.insert(mailLog);
            /**2.消息落库完正常发送消息
             * convertAndSend格式：
             * String exchange(交换机), String routingKey（路由键）, Object object（发送对象）, @Nullable CorrelationData correlationData（消息id）
             * **/
            rabbitTemplate.convertAndSend(
                    MailConstants.MAIL_EXCHANGE_NAME,
                    MailConstants.MAIL_ROUTING_KEY_NAME,
                    emp,
                    new CorrelationData(msgId));
            return RespBean.success("添加员工成功");
        }
        return RespBean.error("添加员工失败");
    }
}
