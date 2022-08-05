package com.smart.iot.charge.biz;

import com.smart.iot.charge.entity.ChargeByHour;
import com.smart.iot.charge.mapper.ChargeByHourMapper;
import org.springframework.stereotype.Service;

import com.yuncitys.smart.parking.common.biz.BusinessBiz;

/**
 * 收费规则表
 *
 * @author Mr.AG
 * @email
 *@version 2022-07-26 13:54:45
 */
@Service
public class ChargeByHourBiz extends BusinessBiz<ChargeByHourMapper,ChargeByHour> {
}
