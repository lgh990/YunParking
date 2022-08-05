package com.yuncitys.smart.parking.app.feign;

import com.yuncitys.smart.parking.auth.client.config.FeignApplyConfiguration;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * smart-tool
 * @author cyd
 * @create 2018/8/1.
 */
@FeignClient(value = "smart-tool",configuration = FeignApplyConfiguration.class)
public interface ToolServerFeign {
    /**
     *
     * @param phone
     * @param code
     * @param templateType
     * @return
     */
    @RequestMapping(value = "/sms/send", method = RequestMethod.POST)
    public ObjectRestResponse send(@RequestParam("phone") String phone, @RequestParam("code") String code, @RequestParam("templateType") String templateType);
}
