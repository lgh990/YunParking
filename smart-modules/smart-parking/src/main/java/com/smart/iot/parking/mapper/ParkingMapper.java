package com.smart.iot.parking.mapper;

import com.smart.iot.parking.entity.Parking;
import com.yuncitys.smart.parking.common.mapper.CommonMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 停车场
 *
 * @author Mr.AG
 * @email
 *@version 2022-07-30 14:30:12
 */
public interface ParkingMapper extends CommonMapper<Parking> {

    List<Parking> queryNearbyParking(@Param("spaceType") String spaceType, @Param("addlat") Double addlat, @Param("reducelat") Double reducelat, @Param("addlng") Double addlng, @Param("reducelng") Double reducelng, @Param("chargePile") String chargePile);

    Parking queryParkingBySpaceNum(@Param("spaceNum") String spaceNum, @Param("cityCode") String cityCode);

    List<Parking> querySpaceCountByIds(@Param("idList") String[] idList);

    List<Parking> querySpaceCountByParkingIds(@Param("spaceType") String spaceType,@Param("idList") String[] idList);

    List<Parking> querySpaceCountByParkingId(@Param("spaceType") String spaceType,@Param("parkingId") String parkingId);

    List<Parking> querySpaceSumByIds(@Param("idList") String[] idList);

    List<Parking> queryParkingByUserIds(@Param("idList") String[] idList);

    Parking queryNearParking( @Param("addlat") Double addlat, @Param("reducelat") Double reducelat, @Param("addlng") Double addlng, @Param("reducelng") Double reducelng,@Param("parkingId") String parkingId);

    Map queryEveryMonthCountByYear(Map params);

    Long queryMonthCount(Map params);
}
