package com.smart.iot.parking.rest;

import com.smart.iot.parking.biz.ParkingAdvisoryBiz;
import com.smart.iot.parking.entity.ParkingAdvisory;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.rest.BaseController;
import com.yuncitys.smart.parking.common.util.StringUtil;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("parkingAdvisory")
@CheckClientToken
@CheckUserToken
@Api(tags = "停车咨询")
public class ParkingAdvisoryController extends BaseController<ParkingAdvisoryBiz,ParkingAdvisory,String> {
    @Override
    public ObjectRestResponse<ParkingAdvisory> add(@RequestBody ParkingAdvisory entity){
        entity.setAdId(StringUtil.uuid());
        baseBiz.insertSelective(entity);
        return new ObjectRestResponse<ParkingAdvisory>().data(entity);
    }

}
