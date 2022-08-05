package com.smart.iot.charge.mapper;

import com.smart.iot.charge.entity.ChargeRulesType;
import com.yuncitys.smart.parking.common.mapper.CommonMapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 收费规则表
 *
 * @author Mr.AG
 * @email
 *@version 2022-07-26 14:54:16
 */
public interface ChargeCenterMapper extends CommonMapper<ChargeRulesType> {
    public List<Map<String, Object>> queryChargeRule(@Param("table_name") String table_name, @Param("parking_id") String parking_id);
}
