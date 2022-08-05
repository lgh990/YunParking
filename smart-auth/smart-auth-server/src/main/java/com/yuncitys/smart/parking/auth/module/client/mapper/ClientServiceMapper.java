package com.yuncitys.smart.parking.auth.module.client.mapper;

import com.yuncitys.smart.parking.auth.module.client.entity.ClientService;
import com.yuncitys.smart.parking.common.mapper.CommonMapper;

public interface ClientServiceMapper extends CommonMapper<ClientService> {
    void deleteByServiceId(String id);
}
