package com.smart.iot.dev.mapper;

import com.smart.iot.dev.entity.DevMaintenanceRecords;
import com.yuncitys.smart.parking.common.mapper.CommonMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 维修记录表
 *
 * @author yuncitys
 * @email yuncitys@smart.iot.com
 * @version 2022-07-08 09:28:51
 */
public interface DevMaintenanceRecordsMapper extends CommonMapper<DevMaintenanceRecords> {

    List<Object> maintenanceRecordsDevQuery(Map params);

    Integer maintenanceRecordsDevCount(Map params);

    Map queryEveryMonthCountByYear(Map params);

    List queryEveryDayCountByMouth(Map params);

    Integer queryRecordsCountByType(Map params);

}
