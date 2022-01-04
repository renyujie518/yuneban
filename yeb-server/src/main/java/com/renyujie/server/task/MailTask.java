package com.renyujie.server.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.renyujie.server.pojo.Employee;
import com.renyujie.server.pojo.MailConstants;
import com.renyujie.server.pojo.MailLog;
import com.renyujie.server.service.IEmployeeService;
import com.renyujie.server.service.IMailLogService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author renyujie518
 * @version 1.0.0
 * @ClassName MailTask.java
 * @Description 邮件发送时若是消息失败回调开启定时任务重试
 * @createTime 2022年01月04日 22:56:00
 */
@Component
public class MailTask {
    @Autowired
    private IMailLogService mailLogService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private IEmployeeService employeeService;

    /**
     * 定时任务
     * 10秒一次
     */
    @Scheduled(cron = "0/10 * * * * ?")
    public void mailTask(){
        //首先从t_mail_log表中获取消息信息，状态为0(投递中)且重试时间小于当前时间的才需要重新发送
        List<MailLog> list = mailLogService.list(new QueryWrapper<MailLog>().eq("status", 0)
                .lt("tryTime", LocalDateTime.now()));

        list.forEach(mailLog -> {
            //重试次数超过3次，更新为投递失败 状态为2，不再重试发送
            if (MailConstants.MAX_TRY_COUNT <= mailLog.getCount()){
                mailLogService.update(new UpdateWrapper<MailLog>().set("status",MailConstants.MSG_FAILURE).eq("msgId",mailLog.getMsgId()));
            }
            //判断数据库中的消息一次   更新重试次数+1，更新时间，重试时间
            mailLogService.update(new UpdateWrapper<MailLog>()
                    .set("count",mailLog.getCount()+1)
                    .set("updateTime",LocalDateTime.now())
                    .set("tryTime",LocalDateTime.now().plusMinutes(MailConstants.MSG_TIMEOUT))
                    .eq("msgId",mailLog.getMsgId()));

            //发送体payload中要包含员工信息
            //（和EmployeeServiceImpl.insertEmployee中的添加员工时发送消息一样  要获取emp信息  具体原因是邮件中包含了员工表格）
            Employee employee = employeeService.getEmployeeById(mailLog.getEid()).get(0);

            System.out.println("MailTask: employee = " + employee );
            System.out.println("MailTask: msgId: " + mailLog.getMsgId());

            //重新发送消息
            /**
              convertAndSend格式：
             * String exchange(交换机), String routingKey（路由键）, Object object（发送对象）, @Nullable CorrelationData correlationData（消息id）**/
            rabbitTemplate.convertAndSend(
                    MailConstants.MAIL_EXCHANGE_NAME,
                    MailConstants.MAIL_ROUTING_KEY_NAME,
                    employee,
                    new CorrelationData(mailLog.getMsgId()));
        });
    }

}
