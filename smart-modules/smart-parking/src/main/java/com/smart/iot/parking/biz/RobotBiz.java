package com.smart.iot.parking.biz;

import org.springframework.stereotype.Service;

import com.smart.iot.parking.entity.Robot;
import com.smart.iot.parking.mapper.RobotMapper;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;

/**
 *
 *
 * @author YUNCITYS
 * @email YUNCITYS@smart.iot.com
 * @version 2022-10-22 10:51:18
 */
@Service
public class RobotBiz extends BusinessBiz<RobotMapper,Robot> {
}
