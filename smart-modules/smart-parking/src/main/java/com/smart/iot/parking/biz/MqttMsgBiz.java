package com.smart.iot.parking.biz;

import com.smart.iot.parking.entity.MqttMsg;
import com.smart.iot.parking.mapper.MqttMsgMapper;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import org.springframework.stereotype.Service;

/**
 * MQTT日志表
 *
 * @author Mr.AG
 * @email
 *@version 2022-08-23 11:36:21
 */
@Service
public class MqttMsgBiz extends BusinessBiz<MqttMsgMapper,MqttMsg> {


}
