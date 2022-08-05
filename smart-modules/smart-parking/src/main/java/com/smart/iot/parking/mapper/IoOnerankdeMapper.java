package com.smart.iot.parking.mapper;

import com.smart.iot.parking.entity.IoOnerankde;
import com.yuncitys.smart.parking.common.mapper.CommonMapper;
import org.apache.ibatis.annotations.Param;

/**
 *
 *
 * @author Mr.AG
 * @email 
 *@version 2022-08-07 13:53:45
 */
public interface IoOnerankdeMapper extends CommonMapper<IoOnerankde> {

    IoOnerankde queryDevByIoIdAndDevSn(@Param("parkingioId") String parkingioId, @Param("onerankdevDevSn") String onerankdevDevSn);



}
