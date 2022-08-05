package com.smart.iot.parking.mapper;

import com.smart.iot.parking.entity.ParkingSpace;
import com.yuncitys.smart.parking.common.mapper.CommonMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 车位表
 *
 * @author Mr.AG
 * @email
 *@version 2022-07-30 14:30:12
 */
public interface ParkingSpaceMapper extends CommonMapper<ParkingSpace> {
    ParkingSpace findListbyParkingSpace(@Param("spacesNum") String spacesNum, @Param("cityId") String cityId, @Param("parkingType") String parkingType);

    void updateSpaceStatus(@Param("spaceStatus") String spaceStatus, @Param("spaceId") String spaceId);

    List<ParkingSpace> queryRandUlotParkingSpaceByParkingId(@Param("areaId") String areaId, @Param("spaceType") String spaceType, @Param("spaceStatus") String spaceStatus, @Param("chargePile") String chargePile,@Param("spaceId") String spaceId);

    List<ParkingSpace> queryOutOfContactSpace();

    List<Object> queryParkingSpace(Map params);

    Integer queryParkingSpaceCount(Map params);

}
