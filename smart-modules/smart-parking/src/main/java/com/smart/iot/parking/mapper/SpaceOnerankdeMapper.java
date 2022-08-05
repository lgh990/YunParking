package com.smart.iot.parking.mapper;

import com.smart.iot.parking.entity.SpaceOnerankde;
import com.yuncitys.smart.parking.common.mapper.CommonMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 车位与设备关联表
 *
 * @author Mr.AG
 * @email
 *@version 2022-07-30 14:30:11
 */
public interface SpaceOnerankdeMapper extends CommonMapper<SpaceOnerankde> {

    void deleteOnerankdevByDevsn(@Param("devSn") String devSn);

    SpaceOnerankde queryDevBySpaceIdAndDevSn(@Param("spaceId") String spaceId, @Param("onerankdevSn") String onerankdevSn);


    Integer addBatchOnerankde(List<SpaceOnerankde> onerankdes);

}
