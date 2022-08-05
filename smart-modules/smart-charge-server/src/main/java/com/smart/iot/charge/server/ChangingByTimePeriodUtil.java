package com.smart.iot.charge.server;

import com.smart.iot.charge.entity.ChargeByTimePeriod;
import com.yuncitys.smart.parking.common.util.DateUtil;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * 路侧停车场收费计算
 *
 */
public class ChangingByTimePeriodUtil
{


    /**
     *
     * @
     * @returnparam szluargingRulesRepository
     */
    public static BigDecimal queryPrice(ChargeByTimePeriod lr, String beginDate, String endDate)
    {
        long dateStr = DateUtil.getDiffTimeStamp(endDate,beginDate);
        double date=dateStr/(1000*60);
        String week = getWeekOfDate(DateUtil.parse(beginDate,null));
        double money = 0;
        long b = 0;
        if(lr==null){
            return new BigDecimal(10.0);
        }
        if (week.equals("工作日"))
        {
            if (DateUtil.parse(beginDate,null).before(DateUtil.parse(lr.getWdEndTime(),null)) || DateUtil.parse(endDate,null).before(DateUtil.parse(lr.getWdStartTime(),null)))
            {
                b =DateUtil.parse(lr.getWdStartTime(),null).getTime();
            }
            else
            {
                b = DateUtil.parse(endDate,null).getTime();
            }

            /*
             * int n=(int) (date/24);
             * double m=((date-24*n)%24);
             */
            long end = (long) (b + date * 60 * 1000);
            if (end > DateUtil.parse(lr.getWdEndTime(),null).getTime())
            {
                end =DateUtil.parse(lr.getWdEndTime(),null).getTime();
            }
            long d = (end - b);
            // long d1=(end-b);
           Double freeMin= Double.parseDouble(lr.getWdFreeMin()) * 60 * 1000;
            if (Double.valueOf(d) >=freeMin)
            {
                d = (long) (d - (Double.parseDouble(lr.getWdFreeMin()) * 60 * 1000));
                money= lr.getWdFirstPrice().add(BigDecimal.valueOf(money)).doubleValue();
                if ((d > Double.parseDouble(lr.getWdFirstHour()) * 60 * 60 * 1000))
                {
                    d = (long) (d - Double.parseDouble(lr.getWdFirstHour()) * 60 * 60 * 1000);
                    if ((d < (Double.parseDouble(lr.getWdAfterHourL()) - Double.parseDouble(lr.getWdFirstHour())) * 60 * 60 * 1000))
                    {
                        int num = (int) (d / (30 * 60 * 1000));
                        if (d % (30 * 60 * 1000) != 0)
                        {
                            num += 1;
                        }
                        money += num * lr.getWdAfterPrice().doubleValue();
                    }
                    else
                    {
                        int numn = (int) (((Double.parseDouble(lr.getWdAfterHourL()) - Double.parseDouble(lr
                                .getWdAfterHourF())) * 60 * 60 * 1000) / (30 * 60 * 1000));
                        if (((Double.parseDouble(lr.getWdAfterHourL()) - Double.parseDouble(lr
                                .getWdAfterHourF())) * 60 * 60 * 1000) % (30 * 60 * 1000) != 0)
                        {
                            numn += 1;
                        }
                        money += numn * lr.getWdAfterPrice().doubleValue();
                        if ((d > Double.parseDouble(lr.getWdAfterHourL())))
                        {
                            d = (long) (d - (Double.parseDouble(lr.getWdAfterHourL()) - Double.parseDouble(lr
                                    .getWdAfterHourF())) * 60 * 60 * 1000);
                            int num = (int) (d / (30 * 60 * 1000));
                            if (d % (30 * 60 * 1000) != 0)
                            {
                                num += 1;
                            }
                            money += num * lr.getWdLastPrice().doubleValue();
                        }
                    }
                }
            }
        }
        else
        {
            if (DateUtil.parse(endDate,null).before(DateUtil.parse(lr.getOdStartTime(),null)))
            {
                b = DateUtil.parse(lr.getOdStartTime(),null).getTime();
            }
            else
            {
                b = DateUtil.parse(endDate,null).getTime();
            }

            /*
             * int n=(int) (date/24);
             * double m=((date-24*n)%24);
             */
            long end = (long) (b + date * 60 * 1000);
            if (end > DateUtil.parse(lr.getOdEndTime(),null).getTime())
            {
                end = DateUtil.parse(lr.getOdEndTime(),null).getTime();
            }
            long d = (end - b);
            // long d1=(end-b);
            Double freeMin=Double.parseDouble(lr.getOdFreeMin()) * 60 * 1000;
            if (Double.valueOf(d) > freeMin)
            {
                d = (long) (d - (Double.parseDouble(lr.getOdFreeMin()) * 60 * 1000));
                money= lr.getOdFirstPrice().add(BigDecimal.valueOf(money)).doubleValue();
                if ((d > Double.parseDouble(lr.getOdFirstHour()) * 60 * 60 * 1000))
                {
                    d = (long) (d - Double.parseDouble(lr.getOdFirstHour()) * 60 * 60 * 1000);
                    if ((d < (Double.parseDouble(lr.getOdAfterHourlL()) - Double.parseDouble(lr.getOdFirstHour())) * 60 * 60 * 1000))
                    {
                        int num = (int) (d / (30 * 60 * 1000));
                        if (d % (30 * 60 * 1000) != 0)
                        {
                            num += 1;
                        }
                        money += num * lr.getOdAfterPrice().doubleValue();
                    }
                    else
                    {
                        int numn = (int) (((Double.parseDouble(lr.getOdAfterHourlL()) - Double.parseDouble(lr
                                .getOdAfterHourF())) * 60 * 60 * 1000) / (30 * 60 * 1000));
                        if (((Double.parseDouble(lr.getOdAfterHourlL()) - Double.parseDouble(lr
                                .getOdAfterHourF())) * 60 * 60 * 1000) % (30 * 60 * 1000) != 0)
                        {
                            numn += 1;
                        }
                        money += numn * lr.getOdAfterPrice().doubleValue();
                        if ((d > Double.parseDouble(lr.getOdAfterHourlL())))
                        {
                            d = (long) (d - (Double.parseDouble(lr.getOdAfterHourlL()) - Double.parseDouble(lr
                                    .getOdAfterHourF())) * 60 * 60 * 1000);
                            int num = (int) (d / (30 * 60 * 1000));
                            if (d % (30 * 60 * 1000) != 0)
                            {
                                num += 1;
                            }
                            money += num * lr.getOdLastPrice().doubleValue();
                        }
                    }
                }
            }
        }
        BigDecimal price = new BigDecimal(money).setScale(4,BigDecimal.ROUND_HALF_DOWN);
        return price;
    }

    /**
     * * 获取指定日期是星期几
     * 参数为null时表示获取当前日期是星期几
     *
     * @param date
     * @return
     */
    public static String getWeekOfDate(Date date)
    {
        String[] weekOfDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar calendar = Calendar.getInstance();
        if (date != null)
        {
            calendar.setTime(date);
        }
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
        {
            w = 0;
        }

        if (weekOfDays[w].equals("星期六") || weekOfDays[w].equals("星期日"))
        {
            return "非工作日";
        }
        else
        {
            return "工作日";
        }

    }

}

/*
 * double money=0;
 * Parking park=(Parking) spiderService.findObjectById(Parking.class, id);
 * List<Charging_rules> crlist=park.getCrlist();
 * for (int i = 0; i < crlist.size(); i++) {
 *
 * //结束时间在阶段结束时间之前
 * if(StrinAndDate.StringsToUtilDates(endDate).before(StrinAndDate.
 * StringsToUtilDates(crlist.get(i).getBeginDate()))){
 * if(i==0){
 * long begin =StrinAndDate.StringsToUtilDates(beginDate).getTime();
 * long end = StrinAndDate.StringsToUtilDates(endDate).getTime();
 * int e = (int) ((end - begin) / (1000 * 60 * 30));
 * if ((end - begin) % (1000 * 60 * 30) != 0) {
 * e += 1;
 * }
 * money+=(park.getPrice()/2)*e;
 *
 * }
 *
 * //开始时间在阶段开始时间之前，结束时间在阶段结束时间之前
 * }else if(StrinAndDate.StringsToUtilDates(beginDate).before(StrinAndDate.
 * StringsToUtilDates(crlist.get(i).getBeginDate()))
 * &&StrinAndDate.StringsToUtilDates(endDate).before(StrinAndDate.StringsToUtilDates
 * (crlist.get(i).getEndDate()))){//前前
 * if(i==0){
 * long begin =StrinAndDate.StringsToUtilDates(beginDate).getTime();
 * long end =
 * StrinAndDate.StringsToUtilDates(crlist.get(i).getBeginDate()).getTime();
 * int e = (int) ((end - begin) / (1000 * 60 * 30));
 * if ((end - begin) % (1000 * 60 * 30) != 0) {
 * e += 1;
 * }
 * money+=(park.getPrice()/2)*e;
 *
 * }
 *
 * long beginn
 * =StrinAndDate.StringsToUtilDates(crlist.get(i).getBeginDate()).getTime();
 * long endn = StrinAndDate.StringsToUtilDates(endDate).getTime();
 * int en = (int) ((endn - beginn) / (1000 * 60 * 30));
 * if ((endn - beginn) % (1000 * 60 * 30) != 0) {
 * en += 1;
 * }
 * money+=(crlist.get(i).getPrice()/2)*en;
 *
 * //开始时间在阶段开始时间之后，结束时间在阶段结束时间之前
 * }else
 * if(StrinAndDate.StringsToUtilDates(crlist.get(i).getBeginDate()).before(
 * StrinAndDate.StringsToUtilDates(beginDate))
 * &&StrinAndDate.StringsToUtilDates(endDate).before(StrinAndDate.StringsToUtilDates
 * (crlist.get(i).getEndDate()))){//后前
 *
 * long begin =StrinAndDate.StringsToUtilDates(beginDate).getTime();
 * long end = StrinAndDate.StringsToUtilDates(endDate).getTime();
 * int e = (int) ((end - begin) / (1000 * 60 * 30));
 * if ((end - begin) % (1000 * 60 * 30) != 0) {
 * e += 1;
 * }
 * money+=(crlist.get(i).getPrice()/2)*e;
 *
 *
 * //开始时间在阶段开始时间之前，结束时间在阶段结束时间之后
 * }else if(StrinAndDate.StringsToUtilDates(beginDate).before(StrinAndDate.
 * StringsToUtilDates(crlist.get(i).getBeginDate()))
 * &&StrinAndDate.StringsToUtilDates(crlist.get(i).getEndDate()).before(StrinAndDate
 * .StringsToUtilDates(endDate))){//前后
 *
 * if(i==0){
 * long begin =StrinAndDate.StringsToUtilDates(beginDate).getTime();
 * long end =
 * StrinAndDate.StringsToUtilDates(crlist.get(i).getBeginDate()).getTime();
 * int e = (int) ((end - begin) / (1000 * 60 * 30));
 * if ((end - begin) % (1000 * 60 * 30) != 0) {
 * e += 1;
 * }
 * money+=(park.getPrice()/2)*e;
 *
 * }
 *
 *
 * long beginn
 * =StrinAndDate.StringsToUtilDates(crlist.get(i).getBeginDate()).getTime();
 * long endn =
 * StrinAndDate.StringsToUtilDates(crlist.get(i).getEndDate()).getTime();
 * int en = (int) ((endn - beginn) / (1000 * 60 * 30));
 * if ((endn - beginn) % (1000 * 60 * 30) != 0) {
 * en += 1;
 * }
 * money+=(crlist.get(i).getPrice()/2)*en;
 *
 *
 * if(i==(crlist.size()-1)){
 * long beginm
 * =StrinAndDate.StringsToUtilDates(crlist.get(i).getEndDate()).getTime();
 * long endm = StrinAndDate.StringsToUtilDates(endDate).getTime();
 * int em = (int) ((endm - beginm) / (1000 * 60 * 30));
 * if ((endm - beginm) % (1000 * 60 * 30) != 0) {
 * em += 1;
 * }
 * money+=(park.getPrice()/2)*em;
 *
 * }
 *
 * //开始时间在阶段开始时间之后，结束时间在阶段结束时间之后
 * }else
 * if(StrinAndDate.StringsToUtilDates(crlist.get(i).getBeginDate()).before(
 * StrinAndDate.StringsToUtilDates(beginDate))
 * &&StrinAndDate.StringsToUtilDates(crlist.get(i).getEndDate()).before(StrinAndDate
 * .StringsToUtilDates(endDate))){//后后
 *
 * long beginn =StrinAndDate.StringsToUtilDates(beginDate).getTime();
 * long endn =
 * StrinAndDate.StringsToUtilDates(crlist.get(i).getEndDate()).getTime();
 * int en = (int) ((endn - beginn) / (1000 * 60 * 30));
 * if ((endn - beginn) % (1000 * 60 * 30) != 0) {
 * en += 1;
 * }
 * money+=(crlist.get(i).getPrice()/2)*en;
 *
 *
 * if(i==(crlist.size()-1)){
 * long beginm
 * =StrinAndDate.StringsToUtilDates(crlist.get(i).getEndDate()).getTime();
 * long endm = StrinAndDate.StringsToUtilDates(endDate).getTime();
 * int em = (int) ((endm - beginm) / (1000 * 60 * 30));
 * if ((endm - beginm) % (1000 * 60 * 30) != 0) {
 * em += 1;
 * }
 * money+=(park.getPrice()/2)*em;
 *
 * }
 *
 * //开始时间在阶段开始结束之前
 * }else if((StrinAndDate.StringsToUtilDates(beginDate).before(StrinAndDate.
 * StringsToUtilDates(crlist.get(i).getEndDate())))){
 * if(i==(crlist.size()-1)){
 * long beginm =StrinAndDate.StringsToUtilDates(beginDate).getTime();
 * long endm = StrinAndDate.StringsToUtilDates(endDate).getTime();
 * int em = (int) ((endm - beginm) / (1000 * 60 * 30));
 * if ((endm - beginm) % (1000 * 60 * 30) != 0) {
 * em += 1;
 * }
 * money+=(park.getPrice()/2)*em;
 *
 * }
 * }
 * }
 * return money;
 */
