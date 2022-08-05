package com.smart.iot.parking.mqtt;

import com.smart.iot.parking.biz.PublishMsgBiz;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 测试客户端
 *
 * @author lxr
 * @create 2017-11-28 14:14
 **/
@Component
@Slf4j
public class MqttClient {

    public static String host;

    public static org.eclipse.paho.client.mqttv3.MqttClient mqttClient;


    @Value("${mqtt.host}")
    public void setHost(String host) {
        MqttClient.host = host;
    }
    public static String clientinid;
    @Value("${mqtt.clientinid}")
    public void setClientinid(String clientinid) {
        MqttClient.clientinid = clientinid;
    }
    public static String clientoutid;
    @Value("${mqtt.host}")
    public void setClientoutid(String clientoutid) {
        MqttClient.clientoutid = clientoutid;
    }
    public static String appTopic;
    @Value("${mqtt.appTopic}")
    public void setAppTopic(String appTopic) {
        MqttClient.appTopic = appTopic;
    }

    public static int qoslevel;
    @Value("${mqtt.qoslevel}")
    public void setQoslevel(int qoslevel) {
        MqttClient.qoslevel = qoslevel;
    }
    public static String username;
    @Value("${mqtt.username}")
    public void setUsername(String username) {
        MqttClient.username = username;
    }
    public static String password;
    @Value("${mqtt.password}")
    public void setPassword(String password) {
        MqttClient.password = password;
    }
    public static String topic;
    @Value("${mqtt.topic}")
    public void setTopic(String topic) {
        MqttClient.topic = topic;
    }
    @Autowired
    private PublishMsgBiz publishMsgBiz;

    public  static org.eclipse.paho.client.mqttv3.MqttClient connect() throws MqttException {
        MemoryPersistence persistence = new MemoryPersistence();
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(false);
        connOpts.setUserName(username);
        connOpts.setPassword(password.toCharArray());
        connOpts.setConnectionTimeout(10);
        connOpts.setAutomaticReconnect(true);
        connOpts.setKeepAliveInterval(20);
        connOpts.setMaxInflight(1000);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        SSLSocketFactory socketFactory = SecureSocketSslContextFactory.getClientContext().getSocketFactory();
//        connOpts.setSocketFactory(socketFactory);
//      String[] uris = {"tcp://10.100.124.206:1883","tcp://10.100.124.207:1883"};
//      connOpts.setServerURIs(uris);  //起到负载均衡和高可用的作用
        mqttClient = new org.eclipse.paho.client.mqttv3.MqttClient(host, clientinid, persistence);

        mqttClient.setCallback(new PushCallback());
        try {
            mqttClient.connect(connOpts);
            mqttClient.subscribe(topic);
        } catch (MqttException e) {
            e.printStackTrace();
            connect();
        }
        return mqttClient;
    }

    public static void mqttConnect() throws MqttException {
        connect();
    }

}


