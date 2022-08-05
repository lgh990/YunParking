package com.smart.iot.dev.rest;

import com.smart.iot.dev.biz.SpaceOnerankdeBiz;
import com.smart.iot.dev.constant.BaseConstants;
import com.smart.iot.dev.entity.SpaceOnerankde;
import com.smart.iot.dev.mapper.SpaceOnerankdeMapper;
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
import org.springframework.web.bind.annotation.*;


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


}
