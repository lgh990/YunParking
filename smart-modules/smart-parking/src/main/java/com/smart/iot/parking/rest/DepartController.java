package com.smart.iot.parking.rest;

import com.smart.iot.parking.feign.DepartFeign;
import com.smart.iot.parking.feign.UserFeign;
import com.smart.iot.parking.utils.publicUtils;
import com.yuncitys.ag.core.context.BaseContextHandler;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/8/3 0003.
 */

@RestController
@RequestMapping("/depart")
@CheckClientToken
@CheckUserToken
@Api(tags = "部门管理")
public class DepartController {
    @Autowired
    public DepartFeign departFeign;
    @Autowired
    public UserFeign userFeign;

    @RequestMapping(value = "/queryDepartRole",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("根据权限查询部门")
    public List<HashMap<String, Object>> queryDepartRole(){
        List<HashMap<String,Object>> treeMap = publicUtils.getCurrDepart(departFeign);
        treeMap.get(0).remove("children");
        return treeMap;
    }
    @RequestMapping(value = "/queryUserByRole",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("根据部门查询下级用户")
    public HashMap<String, Object> queryUserByRole(@RequestParam Map<String, Object> params){
        String userID = BaseContextHandler.getUserID();

        List<HashMap<String,Object>> treeMap = publicUtils.getCurrDepart(departFeign);
        HashMap<String,Object> map = (HashMap<String, Object>) treeMap.get(0);
        if("PARKING_ADMIN".equals(map.get("code")))//停车场超级管理员,只查询所有停车场管理员
        {
            List<HashMap<String, Object>> child = (List<HashMap<String, Object>>) treeMap.get(0).get("children");
            params.put("departId",child.get(0).get("parentId"));
        }else //停车场管理员,查询所有该人员创建的用户
        {
            params.put("crtUserId",userID);
        }
        params.put("isDeleted",0);
        HashMap<String,Object> usermap = userFeign.queryUserByDepart(params);
        return usermap;
    }


}
