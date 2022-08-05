package com.smart.iot.parking.mapper;

import com.smart.iot.parking.entity.Onerankdev;
import com.yuncitys.smart.parking.common.mapper.CommonMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备表
 *
 * @author Mr.AG
 * @email
 *@version 2022-07-30 14:30:11
 */
public interface OnerankdevMapper extends CommonMapper<Onerankdev> {

    List<Onerankdev> queryOnerankdevBySpaceid(@Param("id") String id);

    List<Onerankdev> querySpaceUnbindOnerankdevByDevtype(@Param("devType") String devType);

    List<Onerankdev> queryBindOnerankdevByIoidAndDevType(@Param("id") String id, @Param("devType") String devType);

    List<Onerankdev> queryBindOnerankdevBy(@Param("cameraSn") String cameraSn);
    List<Onerankdev> queryIoUnbindOnerankdevByDevtype(@Param("devType") String devType);

    Onerankdev queryOnerankdevBySpaceidAndDevType(@Param("spaceId") String spaceId, @Param("devType") String devType);

    List<Onerankdev> queryBindOnerankdevByParkingIdAndDevType(@Param("parkingId") String parkingId, @Param("devType") String devType);

}
