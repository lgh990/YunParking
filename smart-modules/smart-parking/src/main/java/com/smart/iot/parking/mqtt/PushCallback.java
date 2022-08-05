package com.smart.iot.parking.mqtt;

import com.smart.iot.parking.srever.DevBusinessService;
import com.smart.iot.parking.utils.PublicUtil;
import com.yuncitys.smart.parking.common.util.DateUtil;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;

/**
 * Created by Administrator on 2018/6/21 0021.
 */
@Component     //申明为spring组件
public class PushCallback implements MqttCallback {

    @Autowired
    private MqttClient mqttClient;
    @Autowired
    private DevBusinessService devBusinessService;
    private static final Logger logger = Logger.getLogger(PushCallback.class);
    private static PushCallback PushCallback ;  //  关键点1   静态初使化 一个工具类  这样是为了在spring初使化之前
    @PostConstruct     //关键二   通过@PostConstruct 和 @PreDestroy 方法 实现初始化和销毁bean之前进行的操作
    public void init() {
        PushCallback = this;
        PushCallback.devBusinessService=this.devBusinessService;
    }

    @Override
    public void connectionLost(Throwable cause) {
        try {
            MqttClient.mqttConnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void messageArrived(String topic, org.eclipse.paho.client.mqttv3.MqttMessage message) {
        String msg = new String(message.getPayload());

        logger.info("mqtt接受时间:"+ DateUtil.format(new Date(),null)+" ==="+topic + " " + msg);
        try {
            PushCallback.devBusinessService.dealRxData(topic, msg);
        }catch (Exception e)
        {
            logger.info(PublicUtil.getStackMsg(e));
        }
    }


    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
//       System.out.println("deliveryComplete---------" + token.isComplete());
    }

}
