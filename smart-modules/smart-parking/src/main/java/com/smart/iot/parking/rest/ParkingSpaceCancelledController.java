package com.smart.iot.parking.rest;

import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.rest.BaseController;
import com.smart.iot.parking.biz.ParkingSpaceCancelledBiz;
import com.smart.iot.parking.entity.ParkingSpaceCancelled;
import com.yuncitys.smart.parking.common.util.StringUtil;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;

@RestController
@RequestMapping("parkingSpaceCancelled")
@CheckClientToken
@CheckUserToken
@Api(tags = "车位注销申请记录")
public class ParkingSpaceCancelledController extends BaseController<ParkingSpaceCancelledBiz,ParkingSpaceCancelled,String> {

    @Override
    public ObjectRestResponse<ParkingSpaceCancelled> add(@RequestBody ParkingSpaceCancelled parkingSpaceCancelled) {
        parkingSpaceCancelled.setId(StringUtil.uuid());
        return baseBiz.add(parkingSpaceCancelled);
    }
}
