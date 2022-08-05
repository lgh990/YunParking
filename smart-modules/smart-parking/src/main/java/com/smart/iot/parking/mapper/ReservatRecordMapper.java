package com.smart.iot.parking.mapper;

import com.smart.iot.parking.entity.ReservatRecord;
import com.yuncitys.smart.parking.common.mapper.CommonMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 导航记录表
 *
 * @author Mr.AG
 * @email 
 *@version 2022-08-15 10:08:42
 */
public interface ReservatRecordMapper extends CommonMapper<ReservatRecord> {

    ReservatRecord selectReservatRecord(@Param("plateId") String plateId, @Param("reservatStatus") String navigateStatus, @Param("parkingId") String parkingId);

}
