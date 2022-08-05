package com.yuncitys.smart.parking.admin.rpc;

import com.smart.cache.annotation.Cache;
import com.yuncitys.ag.core.context.BaseContextHandler;
import com.yuncitys.smart.parking.admin.rpc.service.PermissionService;
import com.yuncitys.smart.parking.api.vo.authority.PermissionInfo;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.auth.client.annotation.IgnoreUserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author smart
 * @version 2022-06-21 8:15
 */
@RestController
@RequestMapping("api")
@CheckUserToken
@CheckClientToken
public class UserRest {
    @Autowired
    private PermissionService permissionService;

    @Cache(key="permission")
    @RequestMapping(value = "/permissions", method = RequestMethod.GET)
    @IgnoreUserToken
    public @ResponseBody
    List<PermissionInfo> getAllPermission(){
        return permissionService.getAllPermission();
    }

    @RequestMapping(value = "/user/permissions", method = RequestMethod.GET)
    public @ResponseBody List<PermissionInfo> getPermissionByUsername(){
        String username = BaseContextHandler.getUsername();
        return permissionService.getPermissionByUsername(username);
    }




}
