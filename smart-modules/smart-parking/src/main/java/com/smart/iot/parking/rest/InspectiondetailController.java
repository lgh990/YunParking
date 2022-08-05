package com.smart.iot.parking.rest;

import com.smart.iot.parking.biz.InspectiondetailBiz;
import com.smart.iot.parking.entity.Inspectiondetail;
import com.yuncitys.smart.parking.common.rest.BaseController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;

@RestController
@RequestMapping("inspectiondetail")
@CheckClientToken
@CheckUserToken
@Api(tags = "罚单表")
public class InspectiondetailController extends BaseController<InspectiondetailBiz,Inspectiondetail,String> {

}
