package com.smart.iot.parking.biz;

import com.smart.iot.parking.entity.ParkingAdvisory;
import com.smart.iot.parking.mapper.ParkingAdvisoryMapper;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import org.springframework.stereotype.Service;

/**
 * 停车咨询表
 *
 * @author Mr.AG
 * @email
 *@version 2022-09-06 16:53:03
 */
@Service
public class ParkingAdvisoryBiz extends BusinessBiz<ParkingAdvisoryMapper,ParkingAdvisory> {
}
