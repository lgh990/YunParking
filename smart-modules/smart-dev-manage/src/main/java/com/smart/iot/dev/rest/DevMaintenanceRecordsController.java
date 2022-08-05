package com.smart.iot.dev.rest;

import com.yuncitys.smart.parking.auth.client.annotation.IgnoreUserToken;
import com.yuncitys.smart.parking.common.msg.TableResultPageResponse;
import com.yuncitys.smart.parking.common.rest.BaseController;
import com.smart.iot.dev.biz.DevMaintenanceRecordsBiz;
import com.smart.iot.dev.entity.DevMaintenanceRecords;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import io.swagger.annotations.Api;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;

import java.util.Map;


@RestController
@RequestMapping("devMaintenanceRecords")
@CheckClientToken
@CheckUserToken
@Api(tags = "维修记录表")
public class DevMaintenanceRecordsController extends BaseController<DevMaintenanceRecordsBiz,DevMaintenanceRecords,String> {

    @RequestMapping("test")
    public String test(){
        return "success";
    }

    @ApiOperation("维修记录查询")
    @RequestMapping("maintenanceRecordsDevQuery")
    public TableResultPageResponse<Object> maintenanceRecordsDevQuery(@RequestParam Map params){
        return baseBiz.maintenanceRecordsDevQuery(params);
    }

    @ApiOperation("首页综合")
    @RequestMapping("queryByReport")
    @IgnoreUserToken
    public Map queryByReport(@RequestParam Map params){
        return baseBiz.queryByReport(params);
    }

}
