package com.smart.iot.start.rest;

import com.smart.iot.parking.biz.OnerankdevBiz;
import com.smart.iot.parking.biz.ReservatRecordBiz;
import com.smart.iot.parking.constant.BaseConstants;
import com.smart.iot.parking.entity.Onerankdev;
import com.smart.iot.parking.entity.ReservatRecord;
import com.smart.iot.parking.mqtt.MqttClient;
import com.smart.iot.parking.rabbitmq.ExpirationMessagePostProcessor;
import com.smart.iot.parking.rabbitmq.QueueConfig;
import com.smart.iot.parking.vo.Devpackage;
import com.smart.iot.start.biz.EquipmentDataProcess;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.common.util.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
/**
 * Created by Administrator on 2018/8/14 0014.
 */
@RestController
@RequestMapping("test")
@CheckClientToken
@CheckUserToken
@Api(tags = "测试")
public class TestController {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    public EquipmentDataProcess equipmentDataProcess;
    @Autowired
    public OnerankdevBiz onerankdevBiz;
    @Autowired
    public ReservatRecordBiz reservatRecordBiz;
    @RequestMapping(value = "/rabbitmqTest",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("获取所有数据")
    public void all(){
        long timeout = 3000;
        rabbitTemplate.convertAndSend(QueueConfig.DELAY_QUEUE_PER_MESSAGE_TTL_NAME,
                (Object) ("Message From delay_queue_per_message_ttl with expiration " + timeout), new ExpirationMessagePostProcessor(timeout));

    }
    @RequestMapping(value = "/pushGeomagnetic",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("模拟地磁数据")
    public void pushGeomagnetic(@RequestBody Devpackage devpackage){
        Onerankdev onerankdevEx = new Onerankdev();
        onerankdevEx.setOnerankdevDevSn(devpackage.getDeviceId());
        Onerankdev onerankdev = onerankdevBiz.selectOne(onerankdevEx);
        devpackage.setFlowPackgeNum(onerankdev.getFlowNum() + 1);
        devpackage.setOccurrenceTime(DateUtil.getDateTime());
        equipmentDataProcess.comsumDevGeomagneticPackage(devpackage);
    }
    @RequestMapping(value = "/mqttPush",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("mqtt发送测试")
    public void mqttPush() throws MqttException {
        String topic = "iot-topic/app/62c5e4d9f1ca41ec99da9260af97b30a";
        String content = "{\"sgBatteryVol\":\"8146\",\"sgBatteryTemp\":\"29\",\"sgChargingState\":\"0\",\"sgPowerType\":\"1\",\"sgBatteryAlarmVol\":\"0\",\"sgBatteryAlarmTemp\":\"0\",\"sgHeatingState\":\"0\",\"sgOpenBoxState\":\"0\",\"sgPhotosensitiveVol\":\"2997\",\"sgTempHumidity\":\"33.12,51.6\",\"countTopicId\":24710,\"deviceId\":\"97d54750-7b4f-11e8-b61e-0daea62087c1\",\"lastActivityTime\":\"1535960318549\",\"lastConnectTime\":\"1535890611130\",\"msgType\":1}";
        MqttMessage message = new MqttMessage(content.getBytes());
        // 设置消息的服务质量
        message.setQos(1);
        // 发布消息
        MqttClient.mqttClient.publish(topic, message);

    }
    @RequestMapping(value = "/reservatRecordBiz",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("mqtt发送测试")
    public void reservatRecordBiz() throws MqttException {
        ReservatRecord reservatRecord = new ReservatRecord();
        reservatRecord.setBeginDate(DateUtil.getDateTime());
        reservatRecord.setUserId("11");
        reservatRecord.setEndDate("111");
        reservatRecord.setReservatState(BaseConstants.proceStatus.running);
        reservatRecord.setSpaceId("111");
        reservatRecord.setParkingId("111");
        reservatRecord.setPlaId("11");
        reservatRecordBiz.insertSelective(reservatRecord);

    }




}
