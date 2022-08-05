package com.smart.iot.parking.biz;

import org.springframework.stereotype.Service;

import com.smart.iot.parking.entity.RobotData;
import com.smart.iot.parking.mapper.RobotDataMapper;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;

/**
 *
 *
 * @author YUNCITYS
 * @email YUNCITYS@smart.iot.com
 * @version 2022-10-22 10:51:18
 */
@Service
public class RobotDataBiz extends BusinessBiz<RobotDataMapper,RobotData> {
}
