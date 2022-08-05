package com.yuncitys.smart.parking.gate.feign;

import com.yuncitys.smart.parking.api.vo.log.LogInfo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * ${DESCRIPTION}
 *
 * @author smart
 * @version 2022-07-01 15:16
 */
@FeignClient("smart-admin")
public interface ILogFeign {
  @RequestMapping(value="/api/log/save",method = RequestMethod.POST)
  public void saveLog(LogInfo info);
}
