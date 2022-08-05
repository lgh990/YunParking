package com.smart.iot.pay.biz;

import com.smart.iot.pay.entity.PayLog;
import com.smart.iot.pay.mapper.PayLogMapper;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 支付日志biz
 */
@Service
public class PayLogBiz extends BusinessBiz<PayLogMapper,PayLog> {

    @Autowired
    private PayLogMapper payLogMapper;

    public List<PayLog> queryPayLogs(PayLog payLog){
        return payLogMapper.select(payLog);
    }

    public Integer addPayLog(PayLog payLog){
        return payLogMapper.insert(payLog);
    }

}
