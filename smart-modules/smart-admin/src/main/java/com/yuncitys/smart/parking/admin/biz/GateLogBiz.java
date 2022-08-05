package com.yuncitys.smart.parking.admin.biz;

import com.yuncitys.smart.parking.admin.entity.GateLog;
import com.yuncitys.smart.parking.admin.mapper.GateLogMapper;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ${DESCRIPTION}
 *
 * @author smart
 * @version 2022-07-01 14:36
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class GateLogBiz extends BusinessBiz<GateLogMapper,GateLog> {

    @Override
    public void insertSelective(GateLog entity) {
        mapper.insertSelective(entity);
    }
}
