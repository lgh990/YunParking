package com.smart.iot.parking.biz;

import com.smart.iot.parking.entity.DevGateway;
import com.smart.iot.parking.mapper.DevGatewayMapper;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import org.springframework.stereotype.Service;

/**
 * 网关表
 *
 * @author YUNCITYS
 * @email YUNCITYS@smart.iot.com
 * @version 2022-07-08 09:28:51
 */
@Service
public class DevGatewayBiz extends BusinessBiz<DevGatewayMapper, DevGateway> {
}
