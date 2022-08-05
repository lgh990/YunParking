package com.yuncitys.smart.parking.admin;

import com.smart.cache.EnableAceCache;
import com.yuncitys.smart.merge.EnableAceMerge;
import com.yuncitys.smart.parking.auth.client.EnableAceAuthClient;
import com.spring4all.swagger.EnableSwagger2Doc;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * ${DESCRIPTION}
 *
 * @author smart
 * @version 2022-05-25 12:44
 */
@EnableEurekaClient
@EnableCircuitBreaker
@SpringBootApplication
@EnableFeignClients({"com.yuncitys.smart.parking.auth.client.feign","com.yuncitys.smart.parking.admin.feign"})
@EnableScheduling
@EnableAceCache
@EnableTransactionManagement
@MapperScan("com.yuncitys.smart.parking.admin.mapper")
@EnableAceAuthClient
@EnableSwagger2Doc
@EnableAceMerge
public class AdminBootstrap {
    public static void main(String[] args) {
        new SpringApplicationBuilder(AdminBootstrap.class).web(true).run(args);    }
}
