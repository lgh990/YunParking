package com.yuncitys.smart.parking.gate.feign;

import com.yuncitys.smart.parking.api.vo.authority.PermissionInfo;
import com.yuncitys.smart.parking.gate.config.FeignConfiguration;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


/**
 * ${DESCRIPTION}
 *
 * @author smart
 * @version 2022-06-21 8:11
 */
@FeignClient(value = "smart-admin",configuration = FeignConfiguration.class)
public interface IUserFeign {
  /**
   * 获取用户的菜单和按钮权限
   * @return
     */
  @RequestMapping(value="/api/user/permissions",method = RequestMethod.GET)
  public List<PermissionInfo> getPermissionByUsername();

  /**
   * 获取所有菜单和按钮权限
   * @return
     */
  @RequestMapping(value="/api/permissions",method = RequestMethod.GET)
  List<PermissionInfo> getAllPermissionInfo();
}
