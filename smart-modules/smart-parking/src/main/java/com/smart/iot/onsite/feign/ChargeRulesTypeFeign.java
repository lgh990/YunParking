package com.smart.iot.onsite.feign;

import com.yuncitys.smart.parking.auth.client.config.FeignApplyConfiguration;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Map;

/**
 * ${DESCRIPTION}
 *
 * @author smart
 * @version 2022-07-01 15:16
 */
@FeignClient(value = "smart-charge-server",configuration = FeignApplyConfiguration.class)
public interface ChargeRulesTypeFeign {
  @RequestMapping(value="/chargeRulesType/queryCostByIdAndParkingId",method = RequestMethod.GET)
  public ObjectRestResponse<BigDecimal> queryCostByIdAndParkingId(@RequestParam Map<String, Object> params);


 /* @RequestMapping(value="/chargeRulesType/queryCostByVipChargePrice",method = RequestMethod.GET)
  public ObjectRestResponse<BigDecimal> queryCostByVipChargePrice(@RequestParam Map<String, Object> params);*/
}
