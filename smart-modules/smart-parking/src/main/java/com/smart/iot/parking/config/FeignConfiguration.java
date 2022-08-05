package com.smart.iot.parking.config;

import com.yuncitys.smart.parking.auth.client.interceptor.ServiceFeignInterceptor;
import org.springframework.context.annotation.Bean;

/**
 * @author smart
 *@version 2022/1/18.
 */
public class FeignConfiguration {

    @Bean
    public ServiceFeignInterceptor serviceFeignInterceptor(){
        return new ServiceFeignInterceptor();
    }
}
