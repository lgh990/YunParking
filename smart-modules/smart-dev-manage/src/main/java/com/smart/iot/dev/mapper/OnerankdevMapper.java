package com.smart.iot.dev.mapper;

        import com.smart.iot.dev.entity.Onerankdev;
        import com.yuncitys.smart.parking.common.mapper.CommonMapper;

        import java.util.List;
        import java.util.Map;

/**
 * 设备表
 *
 * @author yuncitys
 * @email yuncitys@smart.iot.com
 * @version 2022-07-08 09:28:51
 */
public interface OnerankdevMapper extends CommonMapper<Onerankdev> {

    List queryOnerankdevList(Map params);

    Integer queryOnerankdevCount(Map params);

    List querySpaceList(Map params);

    Integer querySpaceCount(Map params);

    List queryIoList(Map params);

    Integer queryIoCount(Map params);

    String querySceneType(String devType);

    List queryDevTypeList();

    Map queryEveryMonthCountByYear(Map params);

    List queryEveryDayCountByMouth(Map params);

    Integer queryDevCountByType(Map params);

    List queryDevSnAndDeviceId(Map params);
}
