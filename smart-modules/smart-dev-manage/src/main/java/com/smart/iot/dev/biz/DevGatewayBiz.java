package com.smart.iot.dev.biz;

import com.smart.iot.dev.utils.PageUtil;
import com.yuncitys.smart.parking.common.msg.TableResultPageResponse;
import com.yuncitys.smart.parking.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smart.iot.dev.entity.DevGateway;
import com.smart.iot.dev.mapper.DevGatewayMapper;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;

import java.util.List;
import java.util.Map;

/**
 * 网关表
 *
 * @author yuncitys
 * @email yuncitys@smart.iot.com
 * @version 2022-07-08 09:28:51
 */
@Service
public class DevGatewayBiz extends BusinessBiz<DevGatewayMapper,DevGateway> {
    @Autowired
    public  DevGatewayMapper devGatewayMapper;
    public TableResultPageResponse<Object> queryDevGateWayList(Map params) {
        PageUtil.makeStartPoint(params);
        //查询车位
        List<Object> spaceList = devGatewayMapper.queryDevGateWayList(params);
        return new TableResultPageResponse<Object>(devGatewayMapper.queryDevGateWayCount(params),
                spaceList,
                Long.parseLong(params.get("startPoint").toString()),
                Long.parseLong(params.get("limit").toString()));
    }
}
