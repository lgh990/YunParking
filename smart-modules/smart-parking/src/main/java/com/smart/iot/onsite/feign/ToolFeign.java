package com.smart.iot.onsite.feign;

import com.yuncitys.smart.parking.auth.client.config.FeignApplyConfiguration;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;

/**
 * @author smart
 * @create 2018/2/1.
 */
@FeignClient(value = "smart-tool",configuration = FeignApplyConfiguration.class)
public interface ToolFeign {

    @RequestMapping(value="/oss/imageUpload",method = RequestMethod.POST)
    public ObjectRestResponse imageUpload(@RequestParam("str") String str, @RequestParam("pathName") String pathName);
}
