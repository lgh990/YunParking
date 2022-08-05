package com.smart.iot.parking.mapper;

import com.smart.iot.parking.entity.LotMsg;
import com.yuncitys.smart.parking.common.mapper.CommonMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 停车记录表
 *
 * @author Mr.AG
 * @email
 *@version 2022-08-08 11:32:56
 */
public interface LotMsgMapper extends CommonMapper<LotMsg> {

    LotMsg queryRunLotMsgBySpaceId(@Param("spaceId") String spaceId);

    List<LotMsg> queryRunLotMsgByParkingIdAndSpaceType(@Param("parkingId") String parkingId, @Param("spaceType") String spaceType);

    List<Object> queryLotMsgs(Map params);

    Integer queryLotMsgsCount(Map params);
}
