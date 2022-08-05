package com.smart.iot.start.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smart.iot.parking.biz.ReservatRecordBiz;
import com.smart.iot.parking.rabbitmq.QueueConfig;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;

@Configuration
@IntegrationComponentScan
@Slf4j
public class ProcessReceiver implements ChannelAwareMessageListener {
    public static CountDownLatch latch;
    private static Logger logger = LoggerFactory.getLogger(ProcessReceiver.class);

    public static final String FAIL_MESSAGE = "This message will fail";
    @Autowired
    public ReservatRecordBiz reservatRecordBiz;
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        String realMessage = new String(message.getBody(),"GBK");
        JSONObject jsonObject = (JSONObject) JSON.parse(realMessage);
        log.info("--------------------接收到延迟数据---------------"+realMessage);
        String msgType = String.valueOf(jsonObject.get("type"));
        JSONObject object = (JSONObject) jsonObject.get("object");
        if("reservatRecord".equals(msgType)) {
            reservatRecordBiz.reservatOverdueTask(object);
        }else if("order".equals(msgType))
        {
            reservatRecordBiz.ordersOverdueTask(object);
        }
/*
        Thread.sleep(5000);
*/
/*
        try {
            processMessage(message);
        }
        catch (Exception e) {
            // 如果发生了异常，则将该消息重定向到缓冲队列，会在一定延迟之后自动重做
            channel.basicPublish(QueueConfig.PER_QUEUE_TTL_EXCHANGE_NAME, QueueConfig.DELAY_QUEUE_PER_QUEUE_TTL_NAME, null,
                    "The failed message will auto retry after a certain delay".getBytes());
        }

        if (latch != null) {
            latch.countDown();
        }
*/
    }

    /**
     * 模拟消息处理。如果当消息内容为FAIL_MESSAGE的话，则需要抛出异常
     *
     * @param message
     * @throws Exception
     */
    public void processMessage(Message message) throws Exception {
        String realMessage = new String(message.getBody(),"GBK");
        JSONObject jsonObject = (JSONObject) JSON.parse(realMessage);
        logger.info("Received <" + realMessage + ">");
        if (Objects.equals(realMessage, FAIL_MESSAGE)) {
            throw new Exception("Some exception happened");
        }
    }
    /**
     * 定义delay_process_queue队列的Listener Container
     *
     * @param connectionFactory
     * @return
     */
    @Bean
    SimpleMessageListenerContainer processContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(QueueConfig.DELAY_PROCESS_QUEUE_NAME); // 监听delay_process_queue
        container.setMessageListener(new MessageListenerAdapter(this));
        return container;
    }
}
