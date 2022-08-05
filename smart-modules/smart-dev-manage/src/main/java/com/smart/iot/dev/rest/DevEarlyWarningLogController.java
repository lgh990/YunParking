package com.smart.iot.dev.rest;

import com.smart.iot.dev.biz.DevEarlyWarningLogBiz;
import com.smart.iot.dev.entity.DevEarlyWarningLog;
import com.yuncitys.smart.parking.common.rest.BaseController;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import io.swagger.annotations.Api;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;


@RestController
@RequestMapping("devEarlyWarningLog")
@CheckClientToken
@CheckUserToken
@Api(tags = "")
public class DevEarlyWarningLogController extends BaseController<DevEarlyWarningLogBiz, DevEarlyWarningLog,String> {

}
