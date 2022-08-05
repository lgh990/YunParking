package com.yuncitys.smart.parking.gate;


import com.yuncitys.smart.gate.ratelimit.EnableAceGateRateLimit;
import com.yuncitys.smart.gate.ratelimit.config.IUserPrincipal;
import com.yuncitys.smart.parking.auth.client.EnableAceAuthClient;
import com.yuncitys.smart.parking.gate.config.UserPrincipal;
import com.yuncitys.smart.parking.gate.utils.DBLog;
import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by Smart on 2017/6/2.
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients({"com.yuncitys.smart.parking.auth.client.feign","com.yuncitys.smart.parking.gate.feign"})
@EnableZuulProxy
@EnableScheduling
@EnableAceAuthClient
@EnableAceGateRateLimit
@EnableSwagger2Doc
public class GateBootstrap {
    public static void main(String[] args) {
        DBLog.getInstance().start();
        SpringApplication.run(GateBootstrap.class, args);
    }

    @Bean
    @Primary
    IUserPrincipal userPrincipal(){
        return new UserPrincipal();
    }
}
