package com.smart.iot.parking.rest;

import com.smart.iot.parking.biz.LotMsgBiz;
import com.smart.iot.parking.biz.PlateBiz;
import com.smart.iot.parking.entity.LotMsg;
import com.smart.iot.parking.mapper.LotMsgMapper;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.msg.TableResultPageResponse;
import com.yuncitys.smart.parking.common.rest.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("lotMsg")
@CheckClientToken
@CheckUserToken
@Api(tags = "停车记录管理")
public class LotMsgController extends BaseController<LotMsgBiz,LotMsg,String> {
    @Autowired
    public LotMsgMapper lotMsgMapper;
    @Autowired
    public PlateBiz plateBiz;
    @Autowired
    public LotMsgBiz lotMsgBiz;
    @RequestMapping(value = "/queryRunLotMsgBySpaceId",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("查询进行中的停车记录")
    public ObjectRestResponse<LotMsg> queryRunLotMsgBySpaceId(@RequestBody LotMsg lotMsg){
        LotMsg lotMsg1 = lotMsgMapper.queryRunLotMsgBySpaceId(lotMsg.getSpaceId());
        return new ObjectRestResponse<LotMsg>().data(lotMsg1);
    }
    @Override
    public ObjectRestResponse<LotMsg> update(@RequestBody LotMsg lotMsg){
        return lotMsgBiz.updateLotMsgPlateInfo(lotMsg);
    }

    @ApiOperation("路测出入口-分页获取数据")
    @RequestMapping(value = "/queryLotMsg",method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "spaceNum", value = "车位号", dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "beginTime", value = "开始时间", dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "endTime", value = "结束时间", dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "limit", value = "分页条数", dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "page", value = "分页页数", dataType = "String"),
    })
    public TableResultPageResponse<Object> queryLotMsg(@RequestParam Map<String, Object> params) throws InvocationTargetException, ExecutionException, IllegalAccessException, NoSuchMethodException {
        return lotMsgBiz.queryLotMsg(params);
    }


}
