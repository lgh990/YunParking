package com.smart.iot.charge.biz;

import com.smart.iot.charge.entity.ChargeByHours;
import com.smart.iot.charge.mapper.ChargeByHoursMapper;
import com.smart.iot.charge.util.ChargeByHourUtil;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import com.yuncitys.smart.parking.common.util.DateUtil;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 收费规则表
 *
 * @author Mr.AG
 * @email
 * @date 2018-08-30 10:23:50
 */
@Service
public class ChargeByHoursBiz extends BusinessBiz<ChargeByHoursMapper,ChargeByHours> {
    public Map test(String date1, String date2) throws ParseException {
        Date date= DateUtil.sdf.parse(date1);
        Date dates= DateUtil.sdf.parse(date2);
        ChargeByHours chargeByHours=new ChargeByHours();
        chargeByHours.setCarType("auto");
        chargeByHours.setParkingId("1");
        ChargeByHours chargeByHours1=this.selectOne(chargeByHours);
        Map map= ChargeByHourUtil.CalculatingPrice(date,dates,chargeByHours1);
        return map;
    }

    public String splicingHtml(String parkingId, String html){
        ChargeByHours params=new ChargeByHours();
        params.setParkingId(parkingId);
        List<ChargeByHours> chargeByHoursList=mapper.select(params);
        for(ChargeByHours chargeByHours: chargeByHoursList)
        {
            if(chargeByHours.getCarType().equals("auto")){
                html=html.replaceAll("trolleyFirstStartTime",chargeByHours.getFirstStartTime());
                html=html.replaceAll("trolleyFirstEndTime",chargeByHours.getFirstEndTime());
                html=html.replaceAll("trolleyFirstPrice",chargeByHours.getFirstPrice().toString());
                html=html.replaceAll("trolleyFirstHoursPrice",chargeByHours.getFirstHoursPrice().toString());
                html= html.replaceAll("trolleyOdStartTime",chargeByHours.getOdStartTime());
                html=html.replaceAll("trolleyOdEndTime",chargeByHours.getOdEndTime());
                html=html.replaceAll("trolleyOdLastPrice",chargeByHours.getOdLastPrice().toString());
                html=html.replaceAll("trolleyTdPrice",chargeByHours.getTdPrice().toString());
                html=html.replaceAll("trolleyTdHoursPrice",chargeByHours.getTdHoursPrice().toString());
                html=html.replaceAll("trolleyMoncardsMoney",chargeByHours.getMonthsCardPrice().toString());
            }
            if(chargeByHours.getCarType().equals("truck")){
                html=html.replaceAll("cartFirstStartTime",chargeByHours.getFirstStartTime());
                html=html.replaceAll("cartFirstEndTime",chargeByHours.getFirstEndTime());
                html=html.replaceAll("cartFirstPrice",chargeByHours.getFirstPrice().toString());
                html=html.replaceAll("cartFirstHoursPrice",chargeByHours.getFirstHoursPrice().toString());
                html=html.replaceAll("cartOdStartTime",chargeByHours.getOdStartTime());
                html=html.replaceAll("cartOdEndTime",chargeByHours.getOdEndTime());
                html=html.replaceAll("cartOdLastPrice",chargeByHours.getOdLastPrice().toString());
                html=html.replaceAll("cartTdPrice",chargeByHours.getTdPrice().toString());
                html=html.replaceAll("cartTdHoursPrice",chargeByHours.getTdHoursPrice().toString());
                html=html.replaceAll("cartMoncardsMoney",chargeByHours.getMonthsCardPrice().toString());
            }
            if(chargeByHours.getCarType().equals("private")){
                html=html.replaceAll("shareFirstStartTime",chargeByHours.getFirstStartTime());
                html=html.replaceAll("shareFirstEndTime",chargeByHours.getFirstEndTime());
                html=html.replaceAll("shareFirstPrice",chargeByHours.getFirstPrice().toString());
                html=html.replaceAll("shareFirstHoursPrice",chargeByHours.getFirstHoursPrice().toString());
                html=html.replaceAll("shareOdLastPrice",chargeByHours.getOdLastPrice().toString());
            }
            html=html.replaceAll("freeTime",chargeByHours.getFreeTime());
        }
        html.replaceAll("\\r","");
        html.replaceAll("\\t","");
        html.replaceAll("\\n","");
        return html;
    }
}
