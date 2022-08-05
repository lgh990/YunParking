package com.smart.iot.roadside.rest;

import com.smart.iot.parking.biz.UserMessageBiz;
import com.smart.iot.parking.entity.UserMessage;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.rest.BaseController;
import com.yuncitys.smart.parking.common.util.StringUtil;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("userMessageRs")
@CheckClientToken
@CheckUserToken
@Api(tags = "用户消息记录")
public class UserMessageRSController extends BaseController<UserMessageBiz,UserMessage,String> {

    @Override
    public ObjectRestResponse<UserMessage> add(@RequestBody UserMessage entity){
        entity.setId(StringUtil.uuid());
        baseBiz.insertSelective(entity);
        return new ObjectRestResponse<UserMessage>().data(entity);
    }

}
