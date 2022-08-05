package com.smart.iot.charge.rest;

import com.smart.iot.charge.biz.ChargeByTimePeriodBiz;
import com.smart.iot.charge.entity.ChargeByHour;
import com.smart.iot.charge.entity.ChargeByTimePeriod;
import com.smart.iot.charge.entity.ChargeRulesType;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.rest.BaseController;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("chargeByTimePeriod")
public class ChargeByTimePeriodController extends BaseController<ChargeByTimePeriodBiz,ChargeByTimePeriod,Integer> {

    @Autowired
    public ChargeByTimePeriodBiz chargeByTimePeriodBiz;
    @ApiOperation("获取收费规则")
    @RequestMapping(value = "/queryByChargeByTimePeriod", method = RequestMethod.POST)
    public ObjectRestResponse<Object> queryChargeRules(String date1, String date2) {
        System.out.println("==============收费时间"+date1+"    "+date2);
        BigDecimal bigDecima=new BigDecimal(0.0);
        try {
            bigDecima=chargeByTimePeriodBiz.test(date1,date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new ObjectRestResponse<Object>().data(bigDecima);
    }
}
