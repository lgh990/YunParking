package com.smart.iot.parking.rest;

import com.yuncitys.smart.parking.common.rest.BaseController;
import com.smart.iot.parking.biz.SharingRuleRecordsBiz;
import com.smart.iot.parking.entity.SharingRuleRecords;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;

@RestController
@RequestMapping("sharingRuleRecords")
@CheckClientToken
@CheckUserToken
@Api(tags = "共享规则记录表")
public class SharingRuleRecordsController extends BaseController<SharingRuleRecordsBiz,SharingRuleRecords,String> {

}
