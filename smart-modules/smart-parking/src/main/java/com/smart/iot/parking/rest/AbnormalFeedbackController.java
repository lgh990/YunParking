package com.smart.iot.parking.rest;

import com.smart.iot.parking.biz.AbnormalFeedbackBiz;
import com.smart.iot.parking.entity.AbnormalFeedback;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.common.rest.BaseController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("abnormalFeedback")
@CheckClientToken
@CheckUserToken
@Api(tags = "车位故障上报表")
public class AbnormalFeedbackController extends BaseController<AbnormalFeedbackBiz,AbnormalFeedback,String> {

}
