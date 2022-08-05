package com.yuncitys.smart.parking.gate.config;

import com.yuncitys.smart.parking.auth.client.interceptor.ServiceFeignInterceptor;
import org.springframework.context.annotation.Bean;

/**
 *
 * @author smart
 * @date 2017/9/12
 */
public class FeignConfiguration {
    @Bean
    ServiceFeignInterceptor getClientTokenInterceptor(){
        return new ServiceFeignInterceptor();
    }
}
