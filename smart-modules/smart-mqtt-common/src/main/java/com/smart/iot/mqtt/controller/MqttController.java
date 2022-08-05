package com.smart.iot.mqtt.controller;

import com.smart.iot.mqtt.config.MqttGateway;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class MqttController {

    @Autowired
    private MqttGateway mqttGateway;

    @Autowired
    MqttPahoMessageDrivenChannelAdapter adapter;

    @RequestMapping("/send")
    public String sendMqtt(@RequestParam("message") String message) {
        String sendData = message;
        log.info("send message {} ", sendData);
        // 推送消息
        mqttGateway.sendToMqtt(sendData, "iot/experTopic/app/1/23/232/23");
        return "OK";
    }

    @RequestMapping("/subscribe")
    public String subscribe(@RequestParam("topic") String topic) {
        String defaultTopic = "iot/experTopic/app/#";
        if (StringUtils.isNotBlank(topic)) {
            defaultTopic = topic;
        }
        log.info("subscribe topic {} ", defaultTopic);
        // 防止重复订阅，先移除再订阅
        adapter.removeTopic(defaultTopic);
        adapter.addTopic(defaultTopic);
        return "OK";
    }

}
