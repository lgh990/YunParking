package com.smart.iot.dev.mapper;

import com.smart.iot.dev.entity.MqttMsg;
import com.yuncitys.smart.parking.common.mapper.CommonMapper;

import java.util.List;
import java.util.Map;

/**
 * MQTT日志表
 *
 * @author yuncitys
 * @email yuncitys@smart.iot.com
 * @version 2022-07-08 09:28:51
 */
public interface MqttMsgMapper extends CommonMapper<MqttMsg> {

    List<Object> mqttMsgQuery(Map params);

    Integer mqttMsgCount(Map params);

}
