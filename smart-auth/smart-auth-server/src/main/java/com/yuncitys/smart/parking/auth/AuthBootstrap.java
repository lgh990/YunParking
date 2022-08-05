package com.yuncitys.smart.parking.auth;


import com.spring4all.swagger.EnableSwagger2Doc;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.parking.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.parking.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * Created by Smart on 2017/6/2.
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@MapperScan("com.yuncitys.smart.parking.auth.module.*.mapper")
@ComponentScan({"com.yuncitys.ag.core","com.yuncitys.smart.parking.auth"})
@EnableAutoConfiguration
@EnableSwagger2Doc
@SessionAttributes("authorizationRequest")
@EnableResourceServer
@EnableAuthorizationServer
public class AuthBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(AuthBootstrap.class, args);
    }
}
