package com.smart.iot.dev.mapper;

import com.smart.iot.dev.entity.DevGeomagneticData;
import com.yuncitys.smart.parking.common.mapper.CommonMapper;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author yuncitys
 * @email yuncitys@smart.iot.com
 * @version 2022-07-21 13:56:23
 */
public interface DevGeomagneticDataMapper extends CommonMapper<DevGeomagneticData> {
	public DevGeomagneticData queryDevGeomagneticData(Map<String,String> params);

    public List<DevGeomagneticData> queryDevGeomagneticDataList(Map<String,String> params);
}
