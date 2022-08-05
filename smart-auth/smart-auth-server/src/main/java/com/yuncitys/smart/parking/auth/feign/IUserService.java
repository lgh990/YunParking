package com.yuncitys.smart.parking.auth.feign;

import com.yuncitys.smart.parking.auth.configuration.FeignConfiguration;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;


/**
 * ${DESCRIPTION}
 *
 * @author smart
 * @version 2022-06-21 8:11
 */
@FeignClient(value = "${jwt.user-service}",configuration = FeignConfiguration.class)
public interface IUserService {
  /**
   * 通过账户\密码的方式登陆
   * @param username
   * @param password
   * @return
     */
  @RequestMapping(value = "/user/validate", method = RequestMethod.POST)
  public ObjectRestResponse<Map<String,String>> validate(@RequestParam("username") String username, @RequestParam("password") String password);
  @RequestMapping(value = "/user/info", method = RequestMethod.POST)
  public ObjectRestResponse<Map<String,String>> getUserInfoByUsername(@RequestParam("username") String username);
}
