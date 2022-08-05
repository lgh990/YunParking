package com.smart.iot.dev.rest;

import com.smart.iot.dev.biz.DevGeomagneticDataBiz;
import com.smart.iot.dev.entity.DevGeomagneticData;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.rest.BaseController;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import io.swagger.annotations.Api;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;

import java.util.Map;


@RestController
@RequestMapping("devGeomagneticData")
@CheckClientToken
@CheckUserToken
@Api(tags = "")
public class DevGeomagneticDataController extends BaseController<DevGeomagneticDataBiz, DevGeomagneticData,String> {
    @Autowired
    public DevGeomagneticDataBiz devGeomagneticDataBiz;
    @RequestMapping(value = "/queryRemoteMonitoring",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("查询远程监控列表")
    public ObjectRestResponse queryRemoteMonitoring(@RequestParam Map<String, String> params){
        return devGeomagneticDataBiz.queryRemoteMonitoring(params);
    }

}
