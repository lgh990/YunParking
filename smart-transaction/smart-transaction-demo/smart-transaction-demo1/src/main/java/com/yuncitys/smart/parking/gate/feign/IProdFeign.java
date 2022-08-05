package com.yuncitys.smart.parking.gate.feign;

import com.yuncitys.smart.parking.gate.config.FeignConfiguration;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author smart
 *@version 2022/1/18.
 */
@FeignClient(value = "smart-transaction-demo2",configuration = FeignConfiguration.class)
public interface IProdFeign {
    @RequestMapping("/prod/test")
    public void test();
}
