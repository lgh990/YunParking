package com.smart.iot.parking.rest;

import com.smart.iot.parking.biz.MqttMsgBiz;
import com.smart.iot.parking.entity.MqttMsg;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.common.rest.BaseController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("mqttMsg")
@CheckClientToken
@CheckUserToken
@Api(tags = "mqtt信息记录表")
public class MqttMsgController extends BaseController<MqttMsgBiz,MqttMsg,Integer> {

}
