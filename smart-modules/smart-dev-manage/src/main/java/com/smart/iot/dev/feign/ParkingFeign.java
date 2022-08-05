package com.smart.iot.dev.feign;

import com.yuncitys.smart.parking.auth.client.config.FeignApplyConfiguration;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.msg.TableResultPageResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@FeignClient(value = "smart-parking-server", configuration = FeignApplyConfiguration.class)
public interface ParkingFeign {

    @RequestMapping(value = "/parking/page", method = RequestMethod.GET)
    public TableResultPageResponse queryParkingList(@RequestParam Map<String, Object> params);

    @RequestMapping(value = "/parkingArea/page", method = RequestMethod.GET)
    public TableResultPageResponse queryParkingAreaList(@RequestParam Map<String, Object> params);

    @RequestMapping(value = "/devType/page", method = RequestMethod.GET)
    public TableResultPageResponse queryDevTypeList(@RequestParam Map<String, Object> params);

}
