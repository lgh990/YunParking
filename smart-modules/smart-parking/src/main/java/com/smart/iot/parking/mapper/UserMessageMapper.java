package com.smart.iot.parking.mapper;

import com.smart.iot.parking.entity.UserMessage;
import com.yuncitys.smart.parking.common.mapper.CommonMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 我的信息
 *
 * @author Mr.AG
 * @email
 *@version 2022-08-28 17:09:52
 */
public interface UserMessageMapper extends CommonMapper<UserMessage> {
	void updateUserMessageByOrderNum(@Param("orderNum") String orderNum, @Param("proceStatus") String proceStatus);
}
