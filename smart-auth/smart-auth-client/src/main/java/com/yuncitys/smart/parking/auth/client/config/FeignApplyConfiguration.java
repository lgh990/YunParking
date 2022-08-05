package com.yuncitys.smart.parking.auth.client.config;

import com.yuncitys.smart.parking.auth.client.interceptor.ServiceFeignInterceptor;
import org.springframework.context.annotation.Bean;

/**
 * Created by smart on 2017/9/12.
 */
public class FeignApplyConfiguration {
    @Bean
    ServiceFeignInterceptor getClientTokenInterceptor(){
        return new ServiceFeignInterceptor();
    }
}
