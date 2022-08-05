package com.smart.iot.parking.rest;

import com.smart.iot.parking.biz.ParkingBusinessTypeBiz;
import com.smart.iot.parking.entity.ParkingBusinessType;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.common.rest.BaseController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("parkingBusinessType")
@CheckClientToken
@CheckUserToken
@Api(tags = "业务流程类型管理")
public class ParkingBusinessTypeController extends BaseController<ParkingBusinessTypeBiz,ParkingBusinessType,String> {

}
