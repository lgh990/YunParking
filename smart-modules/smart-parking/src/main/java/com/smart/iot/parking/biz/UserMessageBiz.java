package com.smart.iot.parking.biz;

import com.smart.iot.parking.entity.UserMessage;
import com.smart.iot.parking.mapper.UserMessageMapper;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import org.springframework.stereotype.Service;

/**
 * 我的信息
 *
 * @author Mr.AG
 * @email
 *@version 2022-08-28 17:09:52
 */
@Service
public class UserMessageBiz extends BusinessBiz<UserMessageMapper,UserMessage> {
}
