package com.yuncitys.smart.parking.gate;

import com.yuncitys.smart.parking.auth.client.EnableAceAuthClient;
import com.spring4all.swagger.EnableSwagger2Doc;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author smart
 * @version 2022/12/26.
 */
@EnableEurekaClient
@SpringBootApplication
// 开启事务
@EnableTransactionManagement
// 开启熔断监控
@EnableCircuitBreaker
// 开启服务鉴权
@EnableFeignClients({"com.yuncitys.smart.parking.auth.client.feign"})
@MapperScan("com.yuncitys.smart.parking.gate.mapper")
@EnableAceAuthClient
@EnableSwagger2Doc
@ComponentScan({"com.yuncitys.smart.parking.gate"})
public class Demo2Bootstrap {
    public static void main(String[] args) {
        new SpringApplicationBuilder(Demo2Bootstrap.class).web(true).run(args);    }
}
