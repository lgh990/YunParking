package com.smart.iot.parking.rest;

import com.smart.iot.parking.biz.UserMessageBiz;
import com.smart.iot.parking.entity.UserFeedback;
import com.smart.iot.parking.entity.UserMessage;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.rest.BaseController;
import com.yuncitys.smart.parking.common.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("userMessage")
@CheckClientToken
@CheckUserToken
@Api(tags = "用户信息列表")
public class UserMessageController extends BaseController<UserMessageBiz,UserMessage,String> {
    @Override
    public ObjectRestResponse<UserMessage> add(@RequestBody UserMessage entity){
        entity.setId(StringUtil.uuid());
        baseBiz.insertSelective(entity);
        return new ObjectRestResponse<UserMessage>().data(entity);
    }
}
