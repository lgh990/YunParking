package com.smart.iot.charge.rest;

import com.smart.iot.charge.biz.ChargeByTimePeriodBiz;
import com.yuncitys.smart.security.common.msg.ObjectRestResponse;
import com.yuncitys.smart.security.common.rest.BaseController;
import com.smart.iot.charge.biz.ChargeByHoursBiz;
import com.smart.iot.charge.entity.ChargeByHours;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("chargeByHours")
public class ChargeByHoursController extends BaseController<ChargeByHoursBiz,ChargeByHours,Integer> {
    @Autowired
    public ChargeByHoursBiz chargeByHoursBiz;
    @ApiOperation("获取收费规则")
    @RequestMapping(value = "/queryByChargeByHours", method = RequestMethod.POST)
    public ObjectRestResponse<Object> queryByChargeByHours(String date1, String date2) {
        System.out.println("==============收费时间"+date1+"    "+date2);
       Map map=new HashMap();
        try {
            map=chargeByHoursBiz.test(date1,date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new ObjectRestResponse<Object>().data(map);
    }
}
