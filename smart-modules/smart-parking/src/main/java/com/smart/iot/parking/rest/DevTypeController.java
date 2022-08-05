package com.smart.iot.parking.rest;

import com.smart.iot.parking.biz.DevTypeBiz;
import com.smart.iot.parking.entity.DevType;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.common.rest.BaseController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("devType")
@CheckClientToken
@CheckUserToken
@Api(tags = "设备类型")
public class DevTypeController extends BaseController<DevTypeBiz,DevType,String> {

}
