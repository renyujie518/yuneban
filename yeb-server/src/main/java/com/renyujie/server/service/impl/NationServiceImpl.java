package com.renyujie.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.renyujie.server.mapper.NationMapper;
import com.renyujie.server.pojo.Nation;
import com.renyujie.server.service.INationService;
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
public class NationServiceImpl extends ServiceImpl<NationMapper, Nation> implements INationService {

}
