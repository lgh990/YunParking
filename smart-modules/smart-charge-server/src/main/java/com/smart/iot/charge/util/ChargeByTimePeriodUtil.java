package com.smart.iot.charge.util;

import com.smart.iot.charge.entity.ChargeByTimePeriod;
import com.yuncitys.smart.parking.common.util.DateUtil;
import com.yuncitys.smart.parking.common.util.StringUtil;

import java.awt.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class ChargeByTimePeriodUtil {
    public static int chargeTime=30;//收费间隔
    public static int Minutes=60;
    public static BigDecimal CalculatingPrice(Date beginTime, Date endTime, ChargeByTimePeriod chargeByTimePeriod) throws ParseException {
        BigDecimal price=new BigDecimal(0.0);

        if(chargeByTimePeriod!=null && StringUtil.isNotNull(chargeByTimePeriod.getWdFreeMin())) {

            int i=ChargingByUtil.differentDays(beginTime,endTime);  //计算开始时间与结束时间相差天数
            List list=dateArray(i,beginTime,endTime,chargeByTimePeriod);//按天分割成集合
            int minute=ChargingByUtil.CalculatingMinutes(list);//计算停车总共时长
            String min=ChargingByUtil.getWeekOfDate(beginTime)==1?chargeByTimePeriod.getOdFreeMin():chargeByTimePeriod.getWdFreeMin();//获取免费时长
            if (minute <= Integer.valueOf(min)) {
                //免费时段免费
                return price;
            }

            //已过免费时长，产生费用计算
            price=moneys(list,chargeByTimePeriod,beginTime);
        }
        return price;
    }


    /**
     * 计算非工作日价格
     * @param chargeByTimePeriod 收费规则实体
     * @param minute 停车总共时长
     * @param price 价格
     * @return
     */
    public static BigDecimal nonWorkingDayPrice(ChargeByTimePeriod chargeByTimePeriod,int minute,BigDecimal price,int type,int weekMinutes){
        int odFirstHour= Integer.valueOf(chargeByTimePeriod.getOdFirstHour());//非工作日起步时长  odFirstHour
        int odAfterHourF= Integer.valueOf(chargeByTimePeriod.getOdAfterHourF());//非工作日收费起始
        int odAfterHourL= Integer.valueOf(chargeByTimePeriod.getOdAfterHourlL());//非工作日收费末
        if(type==1 && minute==weekMinutes) {
            //minute=minute-Integer.valueOf(chargeByTimePeriod.getOdFreeMin());
            if (weekMinutes <= odFirstHour * Minutes) {
                //低于首次收费，把首次收费价格加上
                int flag = 1;
                if (weekMinutes > chargeTime) {
                    //停车时长大于30分钟进行计算  价格*flag(几个半小时)
                    if (minute % chargeTime == 0) {
                        flag = minute / chargeTime;
                    } else {
                        flag = (minute / chargeTime) + 1;
                    }
                    price = chargeByTimePeriod.getOdFirstPrice().multiply(new BigDecimal(flag));
                }else{
                    return  chargeByTimePeriod.getOdFirstPrice();
                }
              //非工作日第一阶段价格 odFirstPrice
            }
        }
        if(weekMinutes>=odAfterHourF*Minutes){//大于首次收费时间 低于 第三时段时间
            int od=0;
            int flag = 0;
            if(type==1 && minute==weekMinutes) {
                int firstHour = (odFirstHour * Minutes) / chargeTime;
                price = chargeByTimePeriod.getOdFirstPrice().multiply(new BigDecimal(firstHour));//增加首次收费价格
                od = minute - (odFirstHour * Minutes);
            }else{
                od=minute;
            }
            //价格*flag(几个半小时)
            if(od > 0  ) {
                if (od % chargeTime == 0) {
                    flag = od / chargeTime;//扣除首次收费价格
                } else {
                    flag = (od / chargeTime) + 1;
                }
                BigDecimal odFirstPrice = chargeByTimePeriod.getOdAfterPrice();//非工作日第二阶段收费  odAfterPrice
                odFirstPrice = odFirstPrice.multiply(new BigDecimal(flag));//第二阶段收费*多少个半个小时
                price = price.add(odFirstPrice);
            }
        }else if(weekMinutes>odAfterHourL*Minutes){ //高于 第三时段时间
            int flag = 0;
            int od=0;
            if(type==1 && minute==weekMinutes) {
                int firstHour = (odFirstHour * Minutes) / chargeTime;
                price = chargeByTimePeriod.getOdFirstPrice().multiply(new BigDecimal(firstHour));//增加首次收费价格
                BigDecimal odFirstPrice = chargeByTimePeriod.getOdAfterPrice().multiply(new BigDecimal(odAfterHourL));//增加第二阶段的价格
                price = price.add(odFirstPrice);
                od= (minute - (odAfterHourL * Minutes));
            }else{
                od=minute;
            }
            if(od > 0  ) {
                if ( od % chargeTime == 0) {
                    flag = od / chargeTime;
                    ;//扣除首次收费价格
                } else {
                    flag = (od / chargeTime) + 1;
                }
                BigDecimal odLastPrice = chargeByTimePeriod.getOdLastPrice();//非工作日第三阶段收费 odLastPrice
                odLastPrice = odLastPrice.multiply(new BigDecimal(flag));
                price = price.add(odLastPrice);
            }
        }
        return price;
    }

    /**
     *  计算工作日价格
     * @param chargeByTimePeriod 收费规则实体
     * @param minute 停车总共时长
     * @param price 价格
     */
    public static BigDecimal workingDayPrice(ChargeByTimePeriod chargeByTimePeriod,int minute,BigDecimal price,int type,int weekMinutes){
        int wdFirstHour= Integer.valueOf(chargeByTimePeriod.getWdFirstHour());//工作日起步时长  wdFirstHour
        int wdAfterHourF= Integer.valueOf(chargeByTimePeriod.getWdAfterHourF());//工作日收费起始
        int wdLastHour= Integer.valueOf(chargeByTimePeriod.getWdLastHour());//工作日第三阶段时间
        if(type==2 && minute==weekMinutes) {
            //minute=minute-Integer.valueOf(chargeByTimePeriod.getOdFreeMin());
            if (weekMinutes <= wdFirstHour * Minutes) {
                //低于首次收费，把首次收费价格加上
                int flag = 1;
                if (weekMinutes > chargeTime) {
                    //停车时长大于30分钟进行计算  价格*flag(几个半小时)
                    if (minute % chargeTime == 0) {
                        flag = minute / chargeTime;
                    } else {
                        flag = (minute / chargeTime) + 1;
                    }
                    price = chargeByTimePeriod.getWdFirstPrice().multiply(new BigDecimal(flag));//非工作日第一阶段价格 odFirstPrice
                }else{
                    return  chargeByTimePeriod.getWdFirstPrice();
                }
            }
        }
        if(wdAfterHourF>=wdLastHour*Minutes){//大于首次收费时间 低于 第三时段时间
            int wd=0;
            int flag = 0;
            if(type==2 && minute==weekMinutes) {
                int firstHour = (wdFirstHour * Minutes) / chargeTime;
                price = chargeByTimePeriod.getWdFirstPrice().multiply(new BigDecimal(firstHour));//增加首次收费价格
                wd = (minute - (wdFirstHour * Minutes));
            }else{
                wd=minute;
            }
            if(wd>0 ) {
                if (wd % chargeTime == 0) {
                    flag = wd / chargeTime;//扣除首次收费价格
                } else {
                    flag = (wd / chargeTime) + 1;
                }
                BigDecimal wdFirstPrice = chargeByTimePeriod.getWdAfterPrice();//非工作日第二阶段收费  odAfterPrice
                wdFirstPrice = wdFirstPrice.multiply(new BigDecimal(flag));//第二阶段收费*多少个半个小时
                price = price.add(wdFirstPrice);
            }
        }else if(weekMinutes>wdLastHour*Minutes){//高于 第三时段时间
            int flag = 0;
            int wd=0;
            if(type==2 && minute==weekMinutes) {
                int firstHour = (wdFirstHour * Minutes) / chargeTime;
                price = chargeByTimePeriod.getWdFirstPrice().multiply(new BigDecimal(firstHour));//增加首次收费价格
                BigDecimal wdFirstPrice = chargeByTimePeriod.getWdAfterPrice().multiply(new BigDecimal(wdLastHour));//增加第二阶段的价格
                price = price.add(wdFirstPrice);
                 wd = (minute - (wdLastHour * Minutes));
            }else{
                wd=minute;
            }
            if(wd>0) {
                if (wd % chargeTime == 0) {
                    flag = wd / chargeTime;
                    ;//扣除首次收费价格
                } else {
                    flag = (wd / chargeTime) + 1;
                }
                BigDecimal wdLastPrice = chargeByTimePeriod.getWdLastPrice();//非工作日第三阶段收费 odLastPrice
                wdLastPrice = wdLastPrice.multiply(new BigDecimal(flag));
                price = price.add(wdLastPrice);
            }
        }
        return price;
    }



    /**
     * 已过免费时长，产生费用计算
     * @param list 按天分割成集合
     * @param chargeByTimePeriod   收费规则实体
     * @return
     * @throws ParseException
     */
    public static BigDecimal moneys(List list, ChargeByTimePeriod chargeByTimePeriod,Date beginTime) throws ParseException {
        BigDecimal price=new BigDecimal(0);
        int weekMinute=0;
        int minute=0;
        for(int i=0;i<list.size();i++){
            Map map= (Map) list.get(i);
            Date beginDate=DateUtil.sdf.parse(String.valueOf(map.get("beginDate")));//获取开始时间
            Date endDate=DateUtil.sdf.parse(String.valueOf(map.get("endDate")));//获取结束时间
            if(ChargingByUtil.getWeekOfDate(beginDate)==1) {
                weekMinute += DateUtil.minuteDiff(endDate, beginDate);
            }else if(ChargingByUtil.getWeekOfDate(beginDate)==2) {
                minute += DateUtil.minuteDiff(endDate, beginDate);
            }

        }
            int type=0;
            if(ChargingByUtil.getWeekOfDate(beginTime)==1) {
                type=1;
            }else if(ChargingByUtil.getWeekOfDate(beginTime)==2) {
                type=2;
            }
            System.out.println("=========首次计费类型"+(type==1?"非工作日":"工作日"));
             BigDecimal prices=new BigDecimal(0);
        int weekMinutes=0;
        for(int i=0;i<list.size();i++){
            Map map= (Map) list.get(i);
            Date beginDate=DateUtil.sdf.parse(String.valueOf(map.get("beginDate")));//获取开始时间
            Date endDate=DateUtil.sdf.parse(String.valueOf(map.get("endDate")));//获取结束时间
            DateUtil.minuteDiff(endDate, beginDate);
            weekMinute=0;
            minute=0;
            prices=new BigDecimal(0);
            if(ChargingByUtil.getWeekOfDate(beginDate)==1) {
                weekMinute =DateUtil.minuteDiff(endDate, beginDate);
                 weekMinutes+=weekMinute+minute;
                prices =nonWorkingDayPrice(chargeByTimePeriod, weekMinute, prices, type, weekMinutes);
                System.out.println(i+"=========非工作日价格"+prices+"==========非工作日分钟"+weekMinute);
                price=price.add(prices);
            }else if(ChargingByUtil.getWeekOfDate(beginDate)==2) {
                minute  =DateUtil.minuteDiff(endDate, beginDate);
                weekMinutes+=weekMinute+minute;
                prices=workingDayPrice(chargeByTimePeriod,minute,prices,type,weekMinutes);
                System.out.println("=========工作日价格"+prices+"==========工作日分钟"+minute);
                price=price.add(prices);
            }
        }

        return price;
    }

    public static Date date(String date,String firstStartTime) throws ParseException {
        String format=date+" " + firstStartTime + ":00";
        return   DateUtil.sdf.parse(format);
    }

    /**
     * 按天分割成集合
     * @param i  开始时间与结束时间相差天数
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @param chargeByTimePeriod 收费规则实体
     * @return
     * @throws ParseException
     */
    public static List dateArray(int i, Date beginTime, Date endTime, ChargeByTimePeriod chargeByTimePeriod) throws ParseException {
        List list=new ArrayList();
        for(int j=0;j<=i;j++){
            Date date1=new Date();
            Date date2=new Date();
            Date beginDate=DateUtil.addDay(beginTime,j);
            int week=ChargingByUtil.getWeekOfDate(beginDate);
            String startTime=week==1?chargeByTimePeriod.getOdStartTime():chargeByTimePeriod.getWdStartTime();//收费起始
            String endTimes=week==1?chargeByTimePeriod.getOdEndTime():chargeByTimePeriod.getWdEndTime();//收费末
            Date hourfDate = date(DateUtil.sdfAll.format(beginTime),startTime);
            Date hourllDate = date(DateUtil.sdfAll.format(beginTime),endTimes);
            hourfDate=DateUtil.addDay(hourfDate,j);
            hourllDate=DateUtil.addDay(hourllDate,j);
            if(beginTime.before(hourfDate)){
                date1=hourfDate;
            }else{
                date1=beginTime;
            }
            if(hourllDate.before(endTime)){
                date2 = hourllDate;
            }else{
                date2=endTime;
            }
            Map map=new HashMap();
            map.put("beginDate",DateUtil.sdf.format(date1));
            map.put("endDate",DateUtil.sdf.format(date2));
            list.add(map);
        }
        return list;
    }

    public static void main(String[] args) throws ParseException {

        System.out.println(60%60);
    }


}
