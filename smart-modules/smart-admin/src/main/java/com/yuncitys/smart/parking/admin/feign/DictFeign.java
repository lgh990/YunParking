package com.yuncitys.smart.parking.admin.feign;

import com.yuncitys.smart.parking.auth.client.config.FeignApplyConfiguration;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * @author smart
 * @create 2018/2/1.
 */
@FeignClient(value = "smart-dict",configuration = FeignApplyConfiguration.class)
public interface DictFeign {
    /**
     * 获取字典对对应值
     * @param code
     * @return
     */
    @RequestMapping(value = "/dictValue/feign/{code}",method = RequestMethod.GET)
    public Map<String,String> getDictValues(@PathVariable("code") String code);
}
