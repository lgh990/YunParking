package com.smart.iot.dev.rest;

import com.smart.iot.dev.constant.BaseConstants;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.msg.TableResultPageResponse;
import com.yuncitys.smart.parking.common.msg.TableResultResponse;
import com.yuncitys.smart.parking.common.rest.BaseController;
import com.smart.iot.dev.biz.DevGatewayBiz;
import com.smart.iot.dev.entity.DevGateway;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import io.swagger.annotations.Api;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;

import java.util.Map;


@RestController
@RequestMapping("devGateway")
@CheckClientToken
@CheckUserToken
@Api(tags = "网关表")
public class DevGatewayController extends BaseController<DevGatewayBiz,DevGateway,Integer> {
    @RequestMapping(value = "",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("新增单个对象")
    public ObjectRestResponse<DevGateway> add(@RequestBody DevGateway entity){
        DevGateway devGatewayParams=new DevGateway();
        devGatewayParams.setGwSn(entity.getGwSn());
        DevGateway devGateway=baseBiz.selectOne(devGatewayParams);
        if(devGateway==null) {
            return new ObjectRestResponse().BaseResponse(201,"网关不存在，请核实后重新输入！！");
        }
        devGateway.setEnabledFlag(BaseConstants.enabledFlag.y);
        baseBiz.updateById(devGateway);
        return new ObjectRestResponse<DevGateway>().data(devGateway);

    }
    @ApiOperation("分页获取数据")
    @RequestMapping(value = "/queryDevGateWayList",method = RequestMethod.GET)
    @ResponseBody
    public TableResultPageResponse<Object> queryDevGateWayList(@RequestParam Map<String, Object> params){
        return baseBiz.queryDevGateWayList(params);
    }


}
