package com.renyujie.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.renyujie.server.mapper.MailLogMapper;
import com.renyujie.server.pojo.MailLog;
import com.renyujie.server.service.IMailLogService;
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
public class MailLogServiceImpl extends ServiceImpl<MailLogMapper, MailLog> implements IMailLogService {

}
