package com.yuncitys.smart.parking.admin.mapper;

import com.yuncitys.smart.parking.admin.entity.GateLog;
import com.yuncitys.smart.parking.common.data.Tenant;
import com.yuncitys.smart.parking.common.mapper.CommonMapper;

@Tenant(userField = "crt_user")
public interface GateLogMapper extends CommonMapper<GateLog> {
}
