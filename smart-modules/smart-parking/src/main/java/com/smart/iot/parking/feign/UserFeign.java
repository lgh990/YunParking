package com.smart.iot.parking.feign;

import com.yuncitys.smart.parking.auth.client.config.FeignApplyConfiguration;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ${DESCRIPTION}
 *
 * @author smart
 * @version 2022-07-01 15:16
 */
@FeignClient(value = "smart-admin",configuration = FeignApplyConfiguration.class)
public interface UserFeign {
  @RequestMapping(value="/user",method = RequestMethod.POST)
  public HashMap<String,Object> addUser(Map<String, Object> params);
  @RequestMapping(value="/user/page",method = RequestMethod.GET)
  public HashMap<String,Object> queryUserByDepart(@RequestParam Map<String, Object> params);
  @RequestMapping(value="/user/getByUserIds",method = RequestMethod.GET)
  public HashMap<String,String> getByUserIds(@RequestParam("ids") String ids);
  @RequestMapping(value="/user/{id}",method = RequestMethod.GET)
  public HashMap<String,Object> getUserById(@PathVariable("id") String id);
  @RequestMapping(value="/user/queryByIds/{code}",method = RequestMethod.GET)
  public HashMap<String,Object> queryByIds(@PathVariable("code") String code);
  @RequestMapping(value="/user/userInList",method = RequestMethod.POST)
  public List<Map> userInList(@RequestParam("userIdList") List<String> userIdList);



}
