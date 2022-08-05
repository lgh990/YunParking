package com.smart.iot.dev.mapper;

import com.smart.iot.dev.entity.DevGateway;
import com.yuncitys.smart.parking.common.mapper.CommonMapper;

import java.util.List;
import java.util.Map;

/**
 * 网关表
 *
 * @author yuncitys
 * @email yuncitys@smart.iot.com
 * @version 2022-07-08 09:28:51
 */
public interface DevGatewayMapper extends CommonMapper<DevGateway> {
    Integer queryDevGateWayCount(Map params);

    List queryDevGateWayList(Map params);

    Map queryEveryMonthCountByYear(Map params);

    List queryEveryDayCountByMouth(Map params);

}
