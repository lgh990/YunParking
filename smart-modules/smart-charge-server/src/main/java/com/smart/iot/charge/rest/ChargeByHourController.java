package com.smart.iot.charge.rest;

import com.smart.iot.charge.biz.ChargeByHourBiz;
import com.smart.iot.charge.entity.ChargeByHour;
import com.yuncitys.smart.parking.common.msg.TableResultResponse;
import com.yuncitys.smart.parking.common.rest.BaseController;
import com.yuncitys.smart.parking.common.util.Query;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;

import java.util.Map;

@RestController
@RequestMapping("chargeByHour")
public class ChargeByHourController extends BaseController<ChargeByHourBiz,ChargeByHour,Integer> {


}
