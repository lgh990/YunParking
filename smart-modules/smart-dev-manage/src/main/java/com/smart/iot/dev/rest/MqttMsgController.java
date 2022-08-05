package com.smart.iot.dev.rest;

import com.yuncitys.smart.parking.common.msg.TableResultPageResponse;
import com.yuncitys.smart.parking.common.rest.BaseController;
import com.smart.iot.dev.biz.MqttMsgBiz;
import com.smart.iot.dev.entity.MqttMsg;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import io.swagger.annotations.Api;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;

import java.util.Map;


@RestController
@RequestMapping("mqttMsg")
@CheckClientToken
@CheckUserToken
@Api(tags = "MQTT日志表")
public class MqttMsgController extends BaseController<MqttMsgBiz,MqttMsg,Integer> {

    @ApiOperation("mqtt日志查询")
    @RequestMapping("mqttMsgQuery")
    public TableResultPageResponse<Object> mqttMsgQuery(@RequestParam Map params){
        return baseBiz.mqttMsgQuery(params);
    }

}
