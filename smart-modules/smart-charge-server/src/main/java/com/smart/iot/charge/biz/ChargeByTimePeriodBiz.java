package com.smart.iot.charge.biz;

import com.smart.iot.charge.entity.ChargeByTimePeriod;
import com.smart.iot.charge.entity.ChargeRulesType;
import com.smart.iot.charge.feign.ParkingFeign;
import com.smart.iot.charge.mapper.ChargeByTimePeriodMapper;
import com.smart.iot.charge.util.ChargeByTimePeriodUtil;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import com.yuncitys.smart.parking.common.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * 收费规则表
 *
 * @author Mr.AG
 * @email
 *@version 2022-07-26 13:54:45
 */
@Service
public class ChargeByTimePeriodBiz extends BusinessBiz<ChargeByTimePeriodMapper,ChargeByTimePeriod> {
    @Autowired
    public ChargeRulesTypeBiz chargeRulesTypeBiz;
        public BigDecimal test(String date1,String date2) throws ParseException {
            Date date= DateUtil.sdf.parse(date1);
            Date dates= DateUtil.sdf.parse(date2);
            ChargeByTimePeriod chargeByTimePeriod=new ChargeByTimePeriod();
            chargeByTimePeriod.setCarType("auto");
            chargeByTimePeriod.setParkingId("1");
            ChargeByTimePeriod chargeByTimePeriods=this.selectOne(chargeByTimePeriod);
            BigDecimal price=ChargeByTimePeriodUtil.CalculatingPrice(date,dates,chargeByTimePeriods);
            return price;
        }

    public String splicingHtml(String parkingId,String html){
        ChargeByTimePeriod params=new ChargeByTimePeriod();
        params.setParkingId(parkingId);
        List<ChargeByTimePeriod> chargeByTimePeriodList=mapper.select(params);
        for(ChargeByTimePeriod chargeByTimePeriod:chargeByTimePeriodList)
        {
            if(chargeByTimePeriod.getCarType().equals("auto")){
                //工作日
                html=html.replaceAll("wdStartTime",chargeByTimePeriod.getWdStartTime());
                html=html.replaceAll("wdEndTime",chargeByTimePeriod.getWdEndTime());
                html=html.replaceAll("wdFirstHour",chargeByTimePeriod.getWdFirstHour());
                html=html.replaceAll("wdFirstPrice",chargeByTimePeriod.getWdFirstPrice().toString());
                html=html.replaceAll("wdAfterHourL",chargeByTimePeriod.getWdAfterHourL());
                html=html.replaceAll("wdAfterPrice",chargeByTimePeriod.getWdAfterPrice().toString());
                html=html.replaceAll("wdLastPrice",chargeByTimePeriod.getWdLastPrice().toString());
                //非工作日
                html= html.replaceAll("odStartTime",chargeByTimePeriod.getOdStartTime());
                html=html.replaceAll("odEndTime",chargeByTimePeriod.getOdEndTime());
                html=html.replaceAll("odFirstHour",chargeByTimePeriod.getOdFirstHour());
                html= html.replaceAll("odFirstPrice",chargeByTimePeriod.getOdFirstPrice().toString());
                html= html.replaceAll("odAfterHourlL",chargeByTimePeriod.getOdLastHour());
                html=html.replaceAll("odLastHour",chargeByTimePeriod.getOdLastHour());
                html=html.replaceAll("odAfterPrice",chargeByTimePeriod.getOdAfterPrice().toString());
                html=html.replaceAll("odLastPrice",chargeByTimePeriod.getOdLastPrice().toString());
                html=html.replaceAll("wdFreeMin",chargeByTimePeriod.getWdFreeMin());

            }
        }
        html.replace("\\r","");
        html.replace("\\t","");
        html.replace("\\n","");
        return html;
    }
}
