package com.smart.iot.roadside.rest;

import com.smart.iot.parking.biz.AbnormalFeedbackBiz;
import com.smart.iot.parking.entity.AbnormalFeedback;
import com.smart.iot.roadside.biz.AbnormalFeedbackRSBiz;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.rest.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("abnormalFeedbackRs")
@CheckClientToken
@CheckUserToken
@Api(tags = "室外车位异常处理")
public class AbnormalFeedbackRSController extends BaseController<AbnormalFeedbackBiz,AbnormalFeedback,String> {
    @Autowired
    public AbnormalFeedbackRSBiz abnormalFeedbackRSBiz;

    @ApiOperation("处理车位异常记录")
    @RequestMapping(value = "/haddleSpaceErrRecord",method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<AbnormalFeedback> haddleSpaceErrRecord(@RequestBody AbnormalFeedback abnormalFeedback){
        return  abnormalFeedbackRSBiz.haddleSpaceErrRecord(abnormalFeedback);
    }

}
