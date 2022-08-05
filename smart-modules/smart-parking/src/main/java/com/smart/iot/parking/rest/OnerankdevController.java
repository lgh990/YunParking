package com.smart.iot.parking.rest;

import com.smart.iot.parking.biz.OnerankdevBiz;
import com.smart.iot.parking.biz.SpaceOnerankdeBiz;
import com.smart.iot.parking.entity.Onerankdev;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.msg.TableResultResponse;
import com.yuncitys.smart.parking.common.rest.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("onerankdev")
@CheckClientToken
@CheckUserToken
@Api(tags = "设备接口")
public class OnerankdevController extends BaseController<OnerankdevBiz,Onerankdev,String> {
    @Autowired
    public SpaceOnerankdeBiz spaceOnerankdeBiz;
    @RequestMapping(value = "queryOnerankdevBySpaceid/{id}",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("查询车位已绑定设备")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="body", name = "id", value = "车位编号", required = true, dataType = "String")
    })
    public ObjectRestResponse<Object> queryBindOnerankdevBySpaceid(@PathVariable String id){
        return this.baseBiz.queryOnerankdevBySpaceid(id);
    }
    @RequestMapping(value = "/querySpaceUnBindDev",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("查询车位可绑定设备")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="body", name = "devType", value = "设备类型", required = true, dataType = "String")
    })
    public TableResultResponse<Onerankdev> querySpaceUnbindOnerankdevByDevtype(String devType){
        return this.baseBiz.querySpaceUnbindOnerankdevByDevtype(devType);
    }
    @RequestMapping(value = "queryBindOnerankdevByIoid/{id}",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("查询出入口已绑定对象")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType="body", name = "id", value = "出入口编号", required = true, dataType = "String")
    })
    public ObjectRestResponse<Object> queryBindOnerankdevByIoid (@PathVariable String id){
        return this.baseBiz.queryBindOnerankdevByIoid(id);
    }
    @RequestMapping(value = "/queryIoUnBindDev",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("查询出入口可绑定设备")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="body", name = "devType", value = "设备类型", required = true, dataType = "String")
    })
    public TableResultResponse<Onerankdev> queryIoUnbindOnerankdevByDevtype(String devType){
        return this.baseBiz.queryIoUnbindOnerankdevByDevtype(devType);
    }

    @RequestMapping(value = "/deleteBindDev",method = RequestMethod.DELETE)
    @ResponseBody
    @ApiOperation("删除车位绑定对象")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="body", name = "devSn", value = "设备sn", required = true, dataType = "String")
    })
    public void deleteOnerankdevByDevsn(String devSn){
        //判断是否存在进行中订单   ？？？
        this.spaceOnerankdeBiz.deleteOnerankdevByDevsn(devSn);
    }

}
