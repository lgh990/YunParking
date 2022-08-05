package com.smart.iot.charge.feign;

import com.yuncitys.smart.parking.auth.client.config.FeignApplyConfiguration;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

/**
 * ${DESCRIPTION}
 *
 * @author smart
 * @version 2022-07-01 15:16
 */
@FeignClient(value = "smart-parking-server",configuration = FeignApplyConfiguration.class)
public interface ParkingFeign {
  @RequestMapping(value="/parking/updateFirstHourPriceByParkingId",method = RequestMethod.PUT)
  public HashMap<String,String> updateFirstHourPriceByParkingId(@RequestBody Map<String, Object> params);

  @RequestMapping(value="/parking/updateFirstHourPrice",method = RequestMethod.POST)
  public HashMap<String,String> updateFirstHourPrice(@RequestBody Map<String, Object> params);


}
