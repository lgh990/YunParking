package com.smart.iot.parking.rest;

import com.smart.iot.parking.biz.SpaceOnerankdeBiz;
import com.smart.iot.parking.constant.BaseConstants;
import com.smart.iot.parking.entity.SpaceOnerankde;
import com.smart.iot.parking.mapper.SpaceOnerankdeMapper;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.rest.BaseController;
import com.yuncitys.smart.parking.common.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("spaceOnerankde")
@CheckClientToken
@CheckUserToken
@Api(tags = "车位设备绑定管理")
public class SpaceOnerankdeController extends BaseController<SpaceOnerankdeBiz,SpaceOnerankde,String> {
    @Autowired
    public SpaceOnerankdeMapper spaceOnerankdeMapper;
   @Override
   public ObjectRestResponse<SpaceOnerankde> add(@RequestBody SpaceOnerankde spaceOnerankde){
       SpaceOnerankde spaceOnerankde1 = spaceOnerankdeMapper.queryDevBySpaceIdAndDevSn(spaceOnerankde.getSpaceId(),spaceOnerankde.getOnerankdevSn());
       if(spaceOnerankde1 != null)
       {
           return new ObjectRestResponse<SpaceOnerankde>().BaseResponse(BaseConstants.StateConstates.BIND_FAIL_CODE,BaseConstants.StateConstates.BIND_FAIL_MSG);
       }
       spaceOnerankde.setSoId(StringUtil.uuid());
       baseBiz.insertSelective(spaceOnerankde);
       return new ObjectRestResponse<SpaceOnerankde>().data(spaceOnerankde);
   }


    @RequestMapping(value = "/addBatchOnerankde")
    @ApiOperation("车位绑定多个设备")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="insert", name = "数组", value = "集合", dataType = "list"),
            @ApiImplicitParam(paramType="insert", name = "onerankdevSn", value = "设备sn", dataType = "String"),
            @ApiImplicitParam(paramType="insert", name = "spaceId", value = "车位id", dataType = "String"),
    })
    public ObjectRestResponse<Integer> addBatchOnerankde(@RequestBody List<SpaceOnerankde> onerankdes){
        return new ObjectRestResponse().data(baseBiz.addBatchOnerankde(onerankdes));
    }
}
