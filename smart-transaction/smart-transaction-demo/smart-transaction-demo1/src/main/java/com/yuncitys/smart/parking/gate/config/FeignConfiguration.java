package com.yuncitys.smart.parking.gate.config;

import com.codingapi.tx.springcloud.feign.TransactionRestTemplateInterceptor;
import com.yuncitys.smart.parking.auth.client.interceptor.ServiceFeignInterceptor;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

/**
 * @author smart
 *@version 2022/1/18.
 */
public class FeignConfiguration {
    @Bean
    public RequestInterceptor requestInterceptor(){
        return new TransactionRestTemplateInterceptor();
    }

    @Bean
    public ServiceFeignInterceptor serviceFeignInterceptor(){
        return new ServiceFeignInterceptor();
    }
}
