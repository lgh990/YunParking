package com.smart.iot.parking.mapper;

import com.smart.iot.parking.entity.UserParkingSpace;
import com.yuncitys.smart.parking.common.mapper.CommonMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户共享车位表
 *
 * @author Mr.AG
 * @email
 *@version 2022-08-16 14:17:02
 */
public interface UserParkingSpaceMapper extends CommonMapper<UserParkingSpace> {

    List<UserParkingSpace> queryParkingByUserId(@Param("userId") String userId, @Param("parkingId") String parkingId);

}
