package com.yuncitys.smart.parking.app;

import com.yuncitys.smart.parking.auth.client.EnableAceAuthClient;
import com.spring4all.swagger.EnableSwagger2Doc;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.parking.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.parking.oauth2.config.annotation.web.configuration.EnableResourceServer;
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
@EnableFeignClients({"com.yuncitys.smart.parking.auth.client.feign","com.yuncitys.smart.parking.app.feign"})
@MapperScan("com.yuncitys.smart.parking.app")
@EnableAceAuthClient
@EnableSwagger2Doc
@EnableResourceServer
@EnableAuthorizationServer
public class AppBootstrap {
    public static void main(String[] args) {
        new SpringApplicationBuilder(AppBootstrap.class).web(true).run(args);    }
}
