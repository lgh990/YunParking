package com.smart.iot.parking.rest;

import com.smart.iot.parking.biz.PositionBiz;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.auth.client.annotation.IgnoreUserToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("position")
@CheckClientToken
@CheckUserToken
@Api(tags = "获取车场位置表")
public class PositionController{
    @Autowired
    public PositionBiz positionBiz;
    @IgnoreUserToken
    @ApiOperation("生成位置二维码")
    @RequestMapping(value = "/generate_qr_code")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="body", name = "component_type", value = "二维码类型（parkingSpace=车位，parkingIo=出入口，componentData=自定义组件）", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="body", name = "component_id", value = "编号", required = true, dataType = "String")
    })
    public void web_generate_qr_code(HttpServletResponse response, String component_type, String component_id) throws Exception
    {
        response.addHeader("Access-Control-Allow-Origin", "*");
        positionBiz.generate_qr_code(response,component_type,component_id);
    }

}
