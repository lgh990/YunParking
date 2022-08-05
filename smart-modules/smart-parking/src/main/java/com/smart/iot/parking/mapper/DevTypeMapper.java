package com.smart.iot.parking.mapper;

import com.smart.iot.parking.entity.DevType;
import com.yuncitys.smart.parking.common.mapper.CommonMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 *
 * @author Mr.AG
 * @email
 *@version 2022-08-04 14:08:17
 */
public interface DevTypeMapper extends CommonMapper<DevType> {
    List<DevType> queryOnerankeByScenetype(@Param("sceneType") String sceneType);

}
