package com.smart.iot.parking.mapper;

import com.smart.iot.parking.entity.AppUser;
import com.yuncitys.smart.parking.common.mapper.CommonMapper;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author Mr.AG
 * @email
 *@version 2022-08-24 18:05:24
 */
public interface AppUserMapper extends CommonMapper<AppUser> {
    Map queryEveryMonthCountByYear(Map params);

    List queryEveryDayCountByMouth(Map params);

    Long queryUserByType(Map params);
}
