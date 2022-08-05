package com.smart.iot.dev.biz;

import com.smart.iot.dev.mapper.DevGatewayMapper;
import com.smart.iot.dev.mapper.OnerankdevMapper;
import com.smart.iot.dev.utils.PageUtil;
import com.yuncitys.smart.parking.common.msg.TableResultPageResponse;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smart.iot.dev.entity.DevMaintenanceRecords;
import com.smart.iot.dev.mapper.DevMaintenanceRecordsMapper;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;

import java.util.List;
import java.util.Map;

/**
 * 维修记录表
 *
 * @author yuncitys
 * @email yuncitys@smart.iot.com
 * @version 2022-07-08 09:28:51
 */
@Service
public class DevMaintenanceRecordsBiz extends BusinessBiz<DevMaintenanceRecordsMapper,DevMaintenanceRecords> {

    @Autowired
    private OnerankdevMapper onerankdevMapper;

    @Autowired
    private DevGatewayMapper devGatewayMapper;

    public TableResultPageResponse<Object> maintenanceRecordsDevQuery(Map params){
        PageUtil.makeStartPoint(params);
        String type = params.get("onerankdevType").toString();
        if("80".equals(type)){
            params.remove("onerankdevType");
            params.put("isGateway","1");
        }
        List<Object> devRecords = mapper.maintenanceRecordsDevQuery(params);
        return new TableResultPageResponse(mapper.maintenanceRecordsDevCount(params),
                devRecords,Long.parseLong(params.get("startPoint").toString()),Long.parseLong(params.get("limit").toString()) );
    }

    public Map queryByReport(Map params){
        Map returnMap = Maps.newHashMap();
        Map pieMap = Maps.newHashMap();
        Integer recordCount = mapper.queryRecordsCountByType(params);
        Integer devCount;
        if("Year".equals(params.get("queryType").toString())){
            String year = params.get("queryDate").toString();
            params.put("queryYear",year);
            Map recordsbyYear = mapper.queryEveryMonthCountByYear(params);
            Map devByYear;
            if (params.get("isGateway")!=null&&"true".equals(params.get("isGateway").toString())) {
                devByYear = devGatewayMapper.queryEveryMonthCountByYear(params);
                devCount = devGatewayMapper.queryDevGateWayCount(params);
            }else{
                devCount = onerankdevMapper.queryDevCountByType(params);
                devByYear = onerankdevMapper.queryEveryMonthCountByYear(params);
            }
            returnMap.put("records",recordsbyYear);
            returnMap.put("dev",devByYear);
        }else{
            String date = params.get("queryDate")==null?"":params.get("queryDate").toString();
            String[] yearAndMonth = date.split("-");
            if(yearAndMonth.length>=2){
                params.put("queryMonth",yearAndMonth[1]);
                params.put("queryYear",yearAndMonth[0]);
            }
            List recordsbyMouth = mapper.queryEveryDayCountByMouth(params);
            List devByMouth;
            if (params.get("isGateway")!=null&&"true".equals(params.get("isGateway").toString())) {
                devByMouth = devGatewayMapper.queryEveryDayCountByMouth(params);
                devCount = devGatewayMapper.queryDevGateWayCount(params);
            }else{
                devByMouth = onerankdevMapper.queryEveryDayCountByMouth(params);
                devCount = onerankdevMapper.queryDevCountByType(params);
            }
            returnMap.put("records",recordsbyMouth);
            returnMap.put("dev",devByMouth);
        }

        pieMap.put("fault",recordCount);
        pieMap.put("total",devCount);
        returnMap.put("pie",pieMap);
        return returnMap;
    }


}
