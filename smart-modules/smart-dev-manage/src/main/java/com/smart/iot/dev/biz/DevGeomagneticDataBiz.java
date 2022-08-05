package com.smart.iot.dev.biz;

import com.smart.iot.dev.entity.DevGeomagneticData;
import com.smart.iot.dev.mapper.DevGatewayMapper;
import com.smart.iot.dev.mapper.DevGeomagneticDataMapper;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuncitys.smart.parking.common.biz.BusinessBiz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author yuncitys
 * @email yuncitys@smart.iot.com
 * @version 2022-07-21 13:56:23
 */
@Service
public class DevGeomagneticDataBiz extends BusinessBiz<DevGeomagneticDataMapper, DevGeomagneticData> {
    @Autowired
    public DevGeomagneticDataMapper devGeomagneticDataMapper;

    public ObjectRestResponse queryRemoteMonitoring(Map<String,String> params){
        String beginDate=params.get("date");
        String endDate=params.get("date");
        if(!StringUtil.isEmpty(beginDate)){
            params.put("beginDate",beginDate+=" 00:00:00");
            params.put("endDate",endDate+=" 23:59:59");
        }
        DevGeomagneticData devGeomagneticData=devGeomagneticDataMapper.queryDevGeomagneticData(params);
        List<DevGeomagneticData> list=devGeomagneticDataMapper.queryDevGeomagneticDataList(params);
        Map map=new HashMap();
        map.put("devGeomagneticData",devGeomagneticData);
        map.put("list",list);
        ObjectRestResponse objectRestResponse=new ObjectRestResponse();
        objectRestResponse.setData(map);
        return objectRestResponse;
    }
}
