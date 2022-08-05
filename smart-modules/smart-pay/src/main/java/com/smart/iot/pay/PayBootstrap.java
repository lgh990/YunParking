package com.smart.iot.pay;

import com.yuncitys.smart.merge.EnableAceMerge;
import com.yuncitys.smart.parking.auth.client.EnableAceAuthClient;
import com.spring4all.swagger.EnableSwagger2Doc;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author smart
 * @version 2022/12/26.
 */
@EnableEurekaClient
// 开启事务
@EnableTransactionManagement
// 开启熔断监控
@EnableCircuitBreaker
// 开启服务鉴权
@EnableFeignClients({"com.yuncitys.smart.parking.auth.client.feign","com.smart.iot.*.feign"})
@MapperScan("com.smart.iot.*.mapper")
@SpringBootApplication(scanBasePackages = {"com.smart.iot.*"})
@EnableAceAuthClient
@EnableSwagger2Doc
@EnableAceMerge
public class PayBootstrap {

    public static void main(String[] args) throws MqttException {
        new SpringApplicationBuilder(PayBootstrap.class).web(true).run(args);
    }

}
