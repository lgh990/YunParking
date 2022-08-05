package com.smart.iot.parking.rest;

import com.smart.iot.parking.biz.ParkingComponentBiz;
import com.smart.iot.parking.entity.ParkingComponent;
import com.smart.iot.parking.entity.ParkingIo;
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
@RequestMapping("parkingComponent")
@CheckClientToken
@CheckUserToken
@Api(tags = "组件管理")
public class ParkingComponentController extends BaseController<ParkingComponentBiz,ParkingComponent,String> {
    @Override
    public ObjectRestResponse<ParkingComponent> add(@RequestBody ParkingComponent entity){
        entity.setComponentId(StringUtil.uuid());
        baseBiz.insertSelective(entity);
        return new ObjectRestResponse<ParkingComponent>().data(entity);
    }
}
