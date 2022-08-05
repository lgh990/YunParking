package com.smart.iot.parking.mapper;

import com.smart.iot.parking.entity.ParkingArea;
import com.yuncitys.smart.parking.common.mapper.CommonMapper;

import java.util.List;
import java.util.Map;

/**
 * 区层
 *
 * @author Mr.AG
 * @email
 *@version 2022-07-30 14:30:12
 */
public interface ParkingAreaMapper extends CommonMapper<ParkingArea> {
    List<Object> queryParkingArea(Map params);

    Integer queryParkingAreaCount(Map params);
}
