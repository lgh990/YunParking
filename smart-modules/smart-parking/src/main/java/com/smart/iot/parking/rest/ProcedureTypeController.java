package com.smart.iot.parking.rest;

import com.smart.iot.parking.biz.ProcedureTypeBiz;
import com.smart.iot.parking.entity.ProcedureType;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.common.rest.BaseController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("procedureType")
@CheckClientToken
@CheckUserToken
@Api(tags = "流程管理")
public class ProcedureTypeController extends BaseController<ProcedureTypeBiz,ProcedureType,String> {

}
