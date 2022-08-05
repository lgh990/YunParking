package com.smart.iot.charge.util;

import com.alibaba.druid.support.json.JSONUtils;
import com.smart.iot.charge.entity.ChargeByHours;
import com.smart.iot.charge.entity.ChargeByTimePeriod;
import com.yuncitys.smart.parking.common.util.DateUtil;

import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ChargeByHourUtil {
    public static int chargeTime = 30;//收费间隔
    public static int Minutes = 60;

    public static Map CalculatingPrice(Date beginTime, Date endTime, ChargeByHours chargeByHours) throws ParseException {
        BigDecimal price = new BigDecimal(0.0);
        int minute = 0;
        Map maps=new HashMap();
        minute = DateUtil.minuteDiff(endTime, beginTime);
        if (minute <= Integer.valueOf(chargeByHours.getFreeTime())) {
            //免费时段免费
            maps.put("price",price);
            return maps;
        }
        int i = ChargingByUtil.differentDays(beginTime, endTime);
        Map map=new HashMap();
        if(chargeByHours.getCarType().equals("private")) {
            map=datesList(beginTime, endTime, i, chargeByHours);
        }else{
             map = dateList(beginTime, endTime, i, chargeByHours);

        }
        System.out.println(JSONUtils.toJSONString(map).toString());
        price = CalculateParkingPrice(chargeByHours, map, price, beginTime);
        maps.put("map",map);
        maps.put("price",price);
        return maps;
    }

    /**
     * 计算车位价格
     *
     * @param chargeByHours 收费规则实体
     * @param map           停车总共时长
     * @param price         价格
     */
    public static BigDecimal CalculateParkingPrice(ChargeByHours chargeByHours, Map map, BigDecimal price, Date beginDates) throws ParseException {
        List dayList = (List) map.get("dayList");//白天时间集合
        List nightList = (List) map.get("nightList");//黑夜时间集合
        List nonWorkingDayList = (List) map.get("nonWorkingDayList");//非工作日时间集合

        int dayMinute = 0;
        for (int j = 0; j < dayList.size(); j++) {
            Map dayMap = (Map) dayList.get(j);
            Date beginDate = DateUtil.sdf.parse(String.valueOf(dayMap.get("beginDate")));
            Date endDate = DateUtil.sdf.parse(String.valueOf(dayMap.get("endDate")));
            dayMinute += DateUtil.minuteDiff(endDate, beginDate);
        }

        int nightMinute = 0;
        for (int j = 0; j < nightList.size(); j++) {
            Map nightMap = (Map) nightList.get(j);
            Date beginDate = DateUtil.sdf.parse(String.valueOf(nightMap.get("beginDate")));
            Date endDate = DateUtil.sdf.parse(String.valueOf(nightMap.get("endDate")));
            nightMinute += DateUtil.minuteDiff(endDate, beginDate);
        }
        int nonWorkingDayMinute = 0;
        for (int j = 0; j < nonWorkingDayList.size(); j++) {
            Map nonWorkingDayMap = (Map) nonWorkingDayList.get(j);
            Date beginDate = DateUtil.sdf.parse(String.valueOf(nonWorkingDayMap.get("beginDate")));
            Date endDate = DateUtil.sdf.parse(String.valueOf(nonWorkingDayMap.get("endDate")));
            nonWorkingDayMinute += DateUtil.minuteDiff(endDate, beginDate);
        }

        int type = 0;
        //计算白天价格
        if (ChargingByUtil.getWeekOfDate(beginDates) == 1) {
            type = 1;
            //非工作日首次计费
            int hours = Integer.valueOf(chargeByHours.getTdHours()) * 60;//白天首次收费时间
            if (nonWorkingDayMinute <= hours) {
                int count=0;
                if (nonWorkingDayMinute % chargeTime == 0) {
                    count = nonWorkingDayMinute / chargeTime;
                } else {
                    count = (nonWorkingDayMinute / chargeTime) + 1;
                }
                BigDecimal bigDecimal = chargeByHours.getTdPrice().multiply(new BigDecimal(count));
                price = price.add(bigDecimal);
                return price;
            } else {
                BigDecimal bigDecimal = chargeByHours.getTdPrice().multiply(new BigDecimal(2));
                price = price.add(bigDecimal);
            }
        } else {
            //工作日首次计费
            Date stratTime = date(DateUtil.sdfAll.format(beginDates), chargeByHours.getFirstStartTime());
            Date endTime = date(DateUtil.sdfAll.format(beginDates), chargeByHours.getFirstEndTime());
            if ((stratTime.before(beginDates) && beginDates.before(endTime) || DateUtil.sdf.format(stratTime).equals(DateUtil.sdf.format(beginDates)))) {
                type = 2;
                //白天首小时计算
                int hours = Integer.valueOf(chargeByHours.getFirstHours()) * 60;//白天首次收费时间
                if (dayMinute <= hours) {
                    int count=0;
                    if (dayMinute % chargeTime == 0) {
                        count = dayMinute / chargeTime;
                    } else {
                        count = (dayMinute / chargeTime) + 1;
                    }
                    BigDecimal bigDecimal = chargeByHours.getFirstPrice().multiply(new BigDecimal(count));
                    price = price.add(bigDecimal);
                    return price;
                } else {
                    BigDecimal bigDecimal = chargeByHours.getFirstPrice().multiply(new BigDecimal(2));
                    price = price.add(bigDecimal);
                }
            }
        }

        int hours = Integer.valueOf(chargeByHours.getFirstHours()) * 60;//白天首次收费时间
        if (dayMinute > hours) {
            if (type == 2) {
                dayMinute = (dayMinute - hours);
            }
            if (dayMinute % chargeTime == 0) {
                dayMinute = dayMinute / chargeTime;
            } else {
                dayMinute = (dayMinute / chargeTime) + 1;
            }
            BigDecimal bigDecimal = chargeByHours.getFirstHoursPrice().multiply(new BigDecimal(dayMinute));
            price = price.add(bigDecimal);
        }

        //计算黑夜价格
        double counts = 0.0;
        if (nightMinute % chargeTime == 0) {
            counts = nightMinute / chargeTime;
        } else {
            counts = (nightMinute / chargeTime) + 1;
        }
        BigDecimal bigDecimals = chargeByHours.getOdLastPrice().multiply(new BigDecimal(counts));
        price = price.add(bigDecimals);

        //计算非工作日价格
        int hoursNon = Integer.valueOf(chargeByHours.getTdHours()) * Minutes;//白天首次收费时间
        if (nonWorkingDayMinute > hoursNon) {
            if (type == 1) {
                nonWorkingDayMinute = (nonWorkingDayMinute - hoursNon);
            }
            if (nonWorkingDayMinute % chargeTime == 0) {
                nonWorkingDayMinute = nonWorkingDayMinute / chargeTime;
            } else {
                nonWorkingDayMinute = (nonWorkingDayMinute / chargeTime) + 1;
            }
            BigDecimal bigDecimalNon = BigDecimal.valueOf(Double.valueOf(chargeByHours.getTdHoursPrice())).multiply(new BigDecimal(nonWorkingDayMinute));
            price = price.add(bigDecimalNon);
        }
        return price;
    }

    public static void main(String[] args) throws ParseException {
        Date date1 = DateUtil.sdf.parse("2018-07-30 08:00:01");
        Date date2 = DateUtil.sdf.parse("2018-08-31 22:21:00");
        System.out.println(DateUtil.sdfAll.format(DateUtil.addDay(date1, 31)));

    }

    //非工作日开始时间
    public static Date NonBeginTime(Date date) {
        date.setHours(00);
        date.setMinutes(00);
        date.setSeconds(00);
        date = DateUtil.addDay(date, 1);
        return date;
    }

    public static Date NonBeginTimes(Date date) {
        date.setHours(00);
        date.setMinutes(00);
        date.setSeconds(00);
        return date;
    }

    //非工作日结束时间
    public static Date NonEndTime(Date date) {
        date.setDate(date.getDate());
        date.setHours(00);
        date.setMinutes(00);
        date.setSeconds(00);
        return date;
    }

    //非工作日结束时间
    public static Date NonEndTimes(Date date) {
        date.setHours(23);
        date.setMinutes(59);
        date.setSeconds(59);
        return date;
    }


    public static Date date(String date, String firstStartTime) throws ParseException {
        String format = date + " " + firstStartTime + ":00";
        return DateUtil.sdf.parse(format);
    }

    public static Map hourfWeek(Date date1, Date date2) {
        List list = new ArrayList();
        Map nonWorkingDayMap = new HashMap();
        Date nonDate1 = date1;
        Date nonDate2 = NonEndTime(date1);

        nonWorkingDayMap.put("beginDate", DateUtil.sdf.format(nonDate1));
        nonWorkingDayMap.put("endDate", DateUtil.sdf.format(nonDate2));
        return nonWorkingDayMap;
    }

    public static Map hourflWeek(Date date1, Date date2) {
        List list = new ArrayList();
        Map nonWorkingDayMap = new HashMap();
        Date nonDate1 = NonBeginTime(date1);
        Date nonDate2 = date2;
        nonWorkingDayMap.put("beginDate", DateUtil.sdf.format(nonDate1));
        nonWorkingDayMap.put("endDate", DateUtil.sdf.format(nonDate2));
        return nonWorkingDayMap;
    }

    public static Map isNon(int hourfWeek, int hourflWeek, int j, int i, Date beginTime, Date endTime,Date hourfDate) {
        Map nonWorkingDayMap = null;
        if (hourfWeek == 1 && hourflWeek == 1) {
                /*if(beginTime.after(hourfDate)) {
                    nonWorkingDayMap = new HashMap();
                    nonWorkingDayMap.put("beginDate", DateUtil.sdf.format(hourfDate));
                    nonWorkingDayMap.put("endDate", DateUtil.sdf.format(endTime));
                    return nonWorkingDayMap;
                }*/
                if (j == i) {
                    nonWorkingDayMap = new HashMap();
                    nonWorkingDayMap.put("beginDate", DateUtil.sdf.format(beginTime));
                    nonWorkingDayMap.put("endDate", DateUtil.sdf.format(endTime));
                } else {
                    nonWorkingDayMap = new HashMap();
                    if(j!=0) {
                        beginTime = NonBeginTimes(beginTime);
                    }
                    endTime = DateUtil.addDay(NonEndTime(endTime), 1);
                    nonWorkingDayMap.put("beginDate", DateUtil.sdf.format(beginTime));
                    nonWorkingDayMap.put("endDate", DateUtil.sdf.format(endTime));
                }
        }
        return nonWorkingDayMap;
    }

    public static Map addDayMap(Date date1, Date date2) {
        Map dayMap = new HashMap();
        dayMap.put("beginDate", DateUtil.sdf.format(date1));
        dayMap.put("endDate", DateUtil.sdf.format(date2));
        return dayMap;
    }

    public static Map addNightMap(Date date1, Date date2) {
        Map nightMap = new HashMap();
        nightMap.put("beginDate", DateUtil.sdf.format(date1));
        nightMap.put("endDate", DateUtil.sdf.format(date2));
        return nightMap;
    }

    public static Map dateList(Date beginTimes, Date endTimes, int i, ChargeByHours chargeByHours) throws ParseException {
        Map maps = new HashMap();
        List dayList = new ArrayList();//白天集合
        List nightList = new ArrayList();//黑夜集合
        List nonWorkingDayList = new ArrayList();//非工作日集合
        for (int j = 0; j <= i; j++) {
            Date date1 = new Date();
            Date date2 = new Date();
            Map dayMap = new HashMap();
            Map nightMap = new HashMap();
            Map nonWorkingDayMap = new HashMap();
            int day = j - i;
            Date beginTime = DateUtil.addDay(beginTimes, j);
            Date endTime = DateUtil.addDay(endTimes, day);
            int hourfWeek = ChargingByUtil.getWeekOfDate(beginTime);
            int hourflWeek = ChargingByUtil.getWeekOfDate(endTime);
            System.out.println(DateUtil.sdfAll.format(beginTime) + "===================" + DateUtil.sdfAll.format(endTime));
            Date hourfDate = date(DateUtil.sdfAll.format(beginTime), chargeByHours.getFirstStartTime());
            Date hourllDate = date(DateUtil.sdfAll.format(endTime), chargeByHours.getFirstEndTime());
            if( j>0 && hourfWeek == 1 && hourflWeek == 1){
                beginTime=NonBeginTimes(beginTime);
            }
            Map map = isNon(hourfWeek, hourflWeek, j, i, beginTime, endTime,hourfDate);
            if (map != null) {
                nonWorkingDayList.add(map);
                continue;
            }

            if (beginTime.after(hourfDate) && endTime.before(hourllDate)) {//开始时间大于白天开始时间 并且  结束时间小于黑夜时间
                date1 = j == 0 ? beginTime : hourfDate;
                ///-----------------------------------------
                if(endTime.before(hourllDate)) {
                    date2=endTime;
                }else{
                    date2 = i > 0 && 1 != j ? hourllDate : endTime;
                }
                ///-----------------------------------------
                map = isNon(hourfWeek, hourflWeek, j, i, date1, date2,hourfDate);
                if (map != null) {
                    nonWorkingDayList.add(map);
                    continue;
                }

                if (hourfWeek == 1) {
                    nonWorkingDayList.add(hourfWeek(date1, date2));
                    date1 = NonEndTime(date1);
                }
                if (hourflWeek == 1) {
                    //nonWorkingDayList.add(hourflWeek(date1, date2));
                    date1 = date(DateUtil.sdfAll.format(date1), chargeByHours.getFirstEndTime());
                    date2 = NonEndTime(date2);
                }
                dayMap = addDayMap(date1, date2);
                dayList.add(dayMap);
                if (j != i) {
                    date1 = hourllDate;
                    date2 = (i == 0 || i == j) && endTime.after(hourllDate) ? endTime : DateUtil.addDay(hourfDate, 1);
                    hourfWeek = ChargingByUtil.getWeekOfDate(date1);
                    hourflWeek = ChargingByUtil.getWeekOfDate(date2);
                    map = isNon(hourfWeek, hourflWeek, j, i, date1, date2,hourfDate);
                    if (map != null) {
                        nonWorkingDayList.add(map);
                        continue;
                    }
                    if (hourfWeek == 1) {
                        nonWorkingDayList.add(hourfWeek(date1, date2));
                        date1 = NonEndTime(date1);
                    }
                    if (hourflWeek == 1) {
                        //nonWorkingDayList.add(hourflWeek(date1, date2));
                        date1 = date(DateUtil.sdfAll.format(date1), chargeByHours.getFirstEndTime());
                        date2 = NonEndTime(date2);
                    }
                    nightMap = addNightMap(date1,date2);
                    nightList.add(nightMap);
                }
            } else if ((beginTime.before(hourfDate)) && (endTime.after(hourllDate) || DateUtil.sdf.format(endTime).equals(DateUtil.sdf.format(hourllDate)))) { //开始时间小于白天开始时间 并且  结束时间大于黑夜时间
                //算出白天集合
                date1 = hourfDate;
                hourfWeek = ChargingByUtil.getWeekOfDate(DateUtil.addDay(beginTime,-1));
                hourflWeek = ChargingByUtil.getWeekOfDate(DateUtil.addDay(endTime,-1));
                if(hourfWeek==1 && hourflWeek==1) {
                    nightMap=addNightMap(NonBeginTimes(beginTime),hourfDate);
                    nightList.add(nightMap);
                }
                date2 = hourllDate;
                hourfWeek = ChargingByUtil.getWeekOfDate(date1);
                hourflWeek = ChargingByUtil.getWeekOfDate(date2);
                map = isNon(hourfWeek, hourflWeek, j, i, date1, date2,hourfDate);
                if (map != null) {
                    nonWorkingDayList.add(map);
                    continue;
                }
                if (hourfWeek == 1) {
                    nonWorkingDayList.add(hourfWeek(date1, date2));
                    date1 = NonEndTime(date1);
                }
                if (hourflWeek == 1) {
                   // nonWorkingDayList.add(hourflWeek(date1, date2));
                    date1 = date(DateUtil.sdfAll.format(date1), chargeByHours.getFirstEndTime());
                    date2 = NonEndTime(date2);
                }

                dayMap = addDayMap(date1,date2);
                dayList.add(dayMap);
                //计算黑夜集合
                date1 = hourllDate;
                date2 = (i == 0 || i == j) && endTime.after(hourllDate) ? endTime : DateUtil.addDay(hourfDate, 1);

                hourfWeek = ChargingByUtil.getWeekOfDate(date1);
                hourflWeek = ChargingByUtil.getWeekOfDate(date2);
                map = isNon(hourfWeek, hourflWeek, j, i, date1, date2,hourfDate);
                if (map != null) {
                    nonWorkingDayList.add(map);
                    continue;
                }
                if (hourfWeek == 1) {
                    nonWorkingDayList.add(hourfWeek(date1, date2));
                    date1 = NonEndTime(date1);
                }
                if (hourflWeek == 1) {
                   // nonWorkingDayList.add(hourflWeek(date1, date2));
                    date1 = date(DateUtil.sdfAll.format(date1), chargeByHours.getFirstEndTime());
                    date2 = NonEndTime(date2);
                }

                ///=======================
                if (j > 0 || endTime.after(hourllDate)) {
                    nightMap = addNightMap(date1,date2);
                    nightList.add(nightMap);
                }
                if (j == 0) {
                    //计算黑夜集合
                    date1 = beginTime;
                    date2 = hourfDate;
                    hourfWeek = ChargingByUtil.getWeekOfDate(date1);
                    hourflWeek = ChargingByUtil.getWeekOfDate(date2);
                    map = isNon(hourfWeek, hourflWeek, j, i, date1, date2,hourfDate);
                    if (map != null) {
                        nonWorkingDayList.add(map);
                        continue;
                    }
                    if (hourfWeek == 1) {
                        nonWorkingDayList.add(hourfWeek(date1, date2));
                        date1 = NonEndTime(date1);
                    }
                    if (hourflWeek == 1) {
                       //nonWorkingDayList.add(hourflWeek(date1, date2));
                        date1 = date(DateUtil.sdfAll.format(date1), chargeByHours.getFirstEndTime());
                        date2 = NonEndTime(date2);
                    }

                    nightMap = addNightMap(date1,date2);
                    nightList.add(nightMap);
                }
            } else if (beginTime.before(hourfDate) && endTime.before(hourllDate)) { //开始时间小于白天开始时间 并且  结束时间小于黑夜时间
                //算出白天集合
                date1 = hourfDate;
                date2 = j == i ? endTime : hourllDate;
                hourfWeek = ChargingByUtil.getWeekOfDate(date1);
                hourflWeek = ChargingByUtil.getWeekOfDate(date2);
                map = isNon(hourfWeek, hourflWeek, j, i, date1, date2,hourfDate);
                if (map != null) {
                    nonWorkingDayList.add(map);
                    continue;
                }
                if (hourfWeek == 1) {
                    nonWorkingDayList.add(hourfWeek(date1, date2));
                    date1 = NonEndTime(date1);
                }
                if (hourflWeek == 1) {
                    //nonWorkingDayList.add(hourflWeek(date1, date2));
                    date1 = date(DateUtil.sdfAll.format(date1), chargeByHours.getFirstEndTime());
                    date2 = NonEndTime(date2);
                }

                dayMap = addDayMap(date1,date2);
                dayList.add(dayMap);
                //计算黑夜集合
                if (j == 0) {
                    date1 = beginTime;
                    if(i == 0 || i == j) {
                        if(endTime.before(hourfDate)) {
                            date2 = endTime;
                        }else{
                            date2 = hourfDate;
                        }
                    }else{
                        date2=hourfDate;
                    }
                    hourfWeek = ChargingByUtil.getWeekOfDate(date1);
                    hourflWeek = ChargingByUtil.getWeekOfDate(date2);
                    map = isNon(hourfWeek, hourflWeek, j, i, date1, date2,hourfDate);
                    if (map != null) {
                        nonWorkingDayList.add(map);
                        continue;
                    }
                    if (hourfWeek == 1) {
                        nonWorkingDayList.add(hourfWeek(date1, date2));
                        date1 = NonEndTime(date1);
                    }
                    if (hourflWeek == 1) {
                        //nonWorkingDayList.add(hourflWeek(date1, date2));
                        date1 = date(DateUtil.sdfAll.format(date1), chargeByHours.getFirstEndTime());
                        date2 = NonEndTime(date2);
                    }
                    nightMap = addNightMap(date1,date2);
                    nightList.add(nightMap);
                }
                if (i > 0 && j != i) {
                    date1=hourllDate;
                    date2=DateUtil.addDay(hourfDate, 1);
                    hourfWeek = ChargingByUtil.getWeekOfDate(date1);
                    hourflWeek = ChargingByUtil.getWeekOfDate(date2);
                    map = isNon(hourfWeek, hourflWeek, j, i, date1, date2,hourfDate);
                    if (map != null) {
                        nonWorkingDayList.add(map);
                        continue;
                    }
                    if (hourfWeek == 1) {
                        nonWorkingDayList.add(hourfWeek(date1, date2));
                        date1 = NonEndTime(date1);
                    }
                    if (hourflWeek == 1) {
                        //nonWorkingDayList.add(hourflWeek(date1, date2));
                        date1 = date(DateUtil.sdfAll.format(date1), chargeByHours.getFirstEndTime());
                        date2 = NonEndTime(date2);
                    }
                    nightMap = addNightMap(date1,date2);
                    nightList.add(nightMap);
                }

            }else if(beginTime.after(hourllDate) && endTime.after(hourllDate)){

                if(i==j) {
                    if(endTime.getTime()>hourllDate.getTime()) {
                        date1 = beginTime;
                        date2 = endTime;
                    }else{
                        date1 = hourllDate;
                        date2 = endTime;
                    }
                }else{
                    date1=beginTime;
                    date2 =DateUtil.addDay(hourfDate,1);
                }
                hourfWeek = ChargingByUtil.getWeekOfDate(date1);
                hourflWeek = ChargingByUtil.getWeekOfDate(date2);
                map = isNon(hourfWeek, hourflWeek, j, i, date1, date2,hourfDate);
                if (map != null) {
                    nonWorkingDayList.add(map);
                    continue;
                }
                if (hourfWeek == 1) {
                    nonWorkingDayList.add(hourfWeek(date1, date2));
                    date1 = NonEndTime(date1);
                }
                if (hourflWeek == 1) {
                    //nonWorkingDayList.add(hourflWeek(date1, date2));
                    date1 = date(DateUtil.sdfAll.format(date1), chargeByHours.getFirstEndTime());
                    date2 = NonEndTime(date2);
                }
                nightMap = addNightMap(date1,date2);
                nightList.add(nightMap);
                if(j>0) {
                    //算出白天集合
                    date1 = hourfDate;
                    date2 = hourllDate;
                    hourfWeek = ChargingByUtil.getWeekOfDate(date1);
                    hourflWeek = ChargingByUtil.getWeekOfDate(date2);
                    map = isNon(hourfWeek, hourflWeek, j, i, date1, date2, hourfDate);
                    if (map != null) {
                        nonWorkingDayList.add(map);
                        continue;
                    }
                    if (hourfWeek == 1) {
                        nonWorkingDayList.add(hourfWeek(date1, date2));
                        date1 = NonEndTime(date1);
                    }
                    if (hourflWeek == 1) {
                        //nonWorkingDayList.add(hourflWeek(date1, date2));
                        date1 = date(DateUtil.sdfAll.format(date1), chargeByHours.getFirstEndTime());
                        date2 = NonEndTime(date2);
                    }
                    dayMap = addDayMap(date1, date2);
                    dayList.add(dayMap);
                }
            }else if (beginTime.after(hourfDate) && endTime.after(hourllDate)) {//开始时间大于白天开始时间 并且  结束时间大于黑夜时间
                //算出白天集合
                if(beginTime.before(hourfDate)) {
                    date1 = beginTime;
                }else{
                    if(!DateUtil.sdfAll.format(beginTime).equals(DateUtil.sdfAll.format(endTime))) {
                        date1 = hourfDate;
                        hourfWeek = ChargingByUtil.getWeekOfDate(DateUtil.addDay(beginTime, -1));
                        hourflWeek = ChargingByUtil.getWeekOfDate(DateUtil.addDay(endTime, -1));
                        if (hourfWeek == 1 && hourflWeek == 1) {
                            nightMap = addNightMap(NonBeginTimes(beginTime), hourfDate);
                            nightList.add(nightMap);
                        }
                    }
                }
                date2 = hourllDate;
                hourfWeek = ChargingByUtil.getWeekOfDate(date1);
                hourflWeek = ChargingByUtil.getWeekOfDate(date2);
                map = isNon(hourfWeek, hourflWeek, j, i, date1, date2,hourfDate);
                if (map != null) {
                    nonWorkingDayList.add(map);
                    continue;
                }
                if (hourfWeek == 1) {
                    nonWorkingDayList.add(hourfWeek(date1, date2));
                    date1 = NonEndTime(date1);
                }
                if (hourflWeek == 1) {
                    //nonWorkingDayList.add(hourflWeek(date1, date2));
                    date1 = date(DateUtil.sdfAll.format(date1), chargeByHours.getFirstEndTime());
                    date2 = NonEndTime(date2);
                }
                dayMap = addDayMap(date1, date2);
                dayList.add(dayMap);


                hourfWeek = ChargingByUtil.getWeekOfDate(date1);
                hourflWeek = ChargingByUtil.getWeekOfDate(date2);
                map = isNon(hourfWeek, hourflWeek, j, i, date1, date2,hourfDate);
                if (map != null) {
                    nonWorkingDayList.add(map);
                    continue;
                }
                if (hourfWeek == 1) {
                    nonWorkingDayList.add(hourfWeek(date1, date2));
                    date1 = NonEndTime(date1);
                }
                if (hourflWeek == 1) {
                    //nonWorkingDayList.add(hourflWeek(date1, date2));
                    date1 = date(DateUtil.sdfAll.format(date1), chargeByHours.getFirstEndTime());
                    date2 = NonEndTime(date2);
                }

                //计算黑夜集合
                date1 = hourllDate;
                date2 = (i == 0 || i == j) && endTime.after(hourllDate) ? endTime : DateUtil.addDay(hourfDate, 1);
                hourfWeek = ChargingByUtil.getWeekOfDate(date1);
                hourflWeek = ChargingByUtil.getWeekOfDate(date2);
                map = isNon(hourfWeek, hourflWeek, j, i, date1, date2,hourfDate);
                if (map != null) {
                    nonWorkingDayList.add(map);
                    continue;
                }
                if (hourfWeek == 1) {
                    nonWorkingDayList.add(hourfWeek(date1, date2));
                    date1 = NonEndTime(date1);
                }
                if (hourflWeek == 1) {
                    //nonWorkingDayList.add(hourflWeek(date1, date2));
                    date1 = date(DateUtil.sdfAll.format(date1), chargeByHours.getFirstEndTime());
                    date2 = NonEndTime(date2);
                }

                nightMap = addNightMap(date1,date2);
                nightList.add(nightMap);

            } else if (beginTime.after(hourfDate) || DateUtil.sdf.format(beginTime).equals(DateUtil.sdf.format(hourfDate))) {
                if (endTime.before(hourllDate) && j == i) {
                    dayMap = addDayMap(beginTime,endTime);
                    dayList.add(dayMap);
                } else {
                    dayMap = addDayMap(beginTime,hourllDate);
                    dayList.add(dayMap);
                }
                hourfWeek = ChargingByUtil.getWeekOfDate(DateUtil.addDay(beginTime,-1));
                hourflWeek = ChargingByUtil.getWeekOfDate(DateUtil.addDay(endTime,-1));
                if(hourfWeek==1 && hourflWeek==1) {
                    nightMap=addNightMap(NonBeginTimes(beginTime),hourfDate);
                    nightList.add(nightMap);
                }
                if (endTime.after(hourllDate)) {
                    if (j == i) {
                        nightMap = addNightMap(hourllDate,endTime);
                        nightMap.put("beginDate", DateUtil.sdf.format(hourllDate));
                        nightMap.put("endDate", DateUtil.sdf.format(endTime));
                        nightList.add(nightMap);
                    } else {
                        nightMap = new HashMap();
                        nightMap.put("beginDate", DateUtil.sdf.format(hourllDate));
                        nightMap.put("endDate", DateUtil.sdf.format(DateUtil.addDay(hourfDate, 1)));
                        nightList.add(nightMap);
                    }
                } else if (j != i) {
                    date1 = hourllDate;
                    date2 = DateUtil.addDay(hourfDate, 1);
                    hourfWeek = ChargingByUtil.getWeekOfDate(date1);
                    hourflWeek = ChargingByUtil.getWeekOfDate(date2);
                    map = isNon(hourfWeek, hourflWeek, j, i, date1, date2,hourfDate);
                    if (map != null) {
                        nonWorkingDayList.add(map);
                        continue;
                    }
                    if (hourfWeek == 1) {
                        nonWorkingDayList.add(hourfWeek(date1, date2));
                        date1 = NonEndTime(date1);
                    }
                    if (hourflWeek == 1) {
                        //nonWorkingDayList.add(hourflWeek(date1, date2));
                        date1 = date(DateUtil.sdfAll.format(date1), chargeByHours.getFirstEndTime());
                        date2 = NonEndTime(date2);
                    }
                    nightMap = new HashMap();
                    nightMap.put("beginDate", DateUtil.sdf.format(date1));
                    nightMap.put("endDate", DateUtil.sdf.format(date2));
                    nightList.add(nightMap);
                }
            } else if (beginTime.after(hourllDate)) { //开始时间大于结束时间
                Date hourfDates = DateUtil.addDay(hourfDate, j);
                if (endTime.before(hourfDates)) {
                    //算出白天集合
                    date1 = hourfDate;
                    date2 = endTime;
                    hourfWeek = ChargingByUtil.getWeekOfDate(date1);
                    hourflWeek = ChargingByUtil.getWeekOfDate(date2);
                    map = isNon(hourfWeek, hourflWeek, j, i, date1, date2,hourfDate);
                    if (map != null) {
                        nonWorkingDayList.add(map);
                        continue;
                    }
                    if (hourfWeek == 1) {
                        nonWorkingDayList.add(hourfWeek(date1, date2));
                        date1 = NonEndTime(date1);
                    }
                    if (hourflWeek == 1) {
                       // nonWorkingDayList.add(hourflWeek(date1, date2));
                        date1 = date(DateUtil.sdfAll.format(date1), chargeByHours.getFirstEndTime());
                        date2 = NonEndTime(date2);
                    }

                    dayMap = new HashMap();
                    dayMap.put("beginDate", DateUtil.sdf.format(date1));
                    dayMap.put("endDate", DateUtil.sdf.format(date2));
                    dayList.add(dayMap);
                    //计算黑夜集合
                    date1 = hourllDate;
                    date2 = (i == 0 || i == j) && endTime.after(hourllDate) ? endTime : DateUtil.addDay(hourfDate, 1);

                    hourfWeek = ChargingByUtil.getWeekOfDate(date1);
                    hourflWeek = ChargingByUtil.getWeekOfDate(date2);
                    map = isNon(hourfWeek, hourflWeek, j, i, date1, date2,hourfDate);
                    if (map != null) {
                        nonWorkingDayList.add(map);
                        continue;
                    }
                    if (hourfWeek == 1) {
                        nonWorkingDayList.add(hourfWeek(date1, date2));
                        date1 = NonEndTime(date1);
                    }
                    if (hourflWeek == 1) {
                        //nonWorkingDayList.add(hourflWeek(date1, date2));
                        date1 = date(DateUtil.sdfAll.format(date1), chargeByHours.getFirstEndTime());
                        date2 = NonEndTime(date2);
                    }

                    nightMap = new HashMap();
                    nightMap.put("beginDate", DateUtil.sdf.format(date1));
                    nightMap.put("endDate", DateUtil.sdf.format(date2));
                    nightList.add(nightMap);
                } else {
                    //计算黑夜集合
                    date1 = hourllDate;
                    date2 = endTime;
                    hourfWeek = ChargingByUtil.getWeekOfDate(date1);
                    hourflWeek = ChargingByUtil.getWeekOfDate(date2);
                    map = isNon(hourfWeek, hourflWeek, j, i, date1, date2,hourfDate);
                    if (map != null) {
                        nonWorkingDayList.add(map);
                        continue;
                    }
                    if (hourfWeek == 1) {
                        nonWorkingDayList.add(hourfWeek(date1, date2));
                        date1 = NonEndTime(date1);
                    }
                    if (hourflWeek == 1) {
                        //nonWorkingDayList.add(hourflWeek(date1, date2));
                        date1 = date(DateUtil.sdfAll.format(date1), chargeByHours.getFirstEndTime());
                        date2 = NonEndTime(date2);
                    }

                    nightMap = new HashMap();
                    nightMap.put("beginDate", DateUtil.sdf.format(date1));
                    nightMap.put("endDate", DateUtil.sdf.format(date2));
                    nightList.add(nightMap);
                }
            }
        }
        maps.put("dayList", dayList);
        maps.put("nightList", nightList);
        maps.put("nonWorkingDayList", nonWorkingDayList);
        return maps;
    }


    public static Map datesList(Date beginTimes, Date endTimes, int i, ChargeByHours chargeByHours) throws ParseException {
        Map maps = new HashMap();
        List dayList = new ArrayList();//白天集合
        List nightList = new ArrayList();//黑夜集合
        //List nonWorkingDayList = new ArrayList();//非工作日集合
        for (int j = 0; j <= i; j++) {
            Date date1 = new Date();
            Date date2 = new Date();
            Map dayMap = new HashMap();
            Map nightMap = new HashMap();
            int day = j - i;
            Date beginTime = DateUtil.addDay(beginTimes, j);
            Date endTime = DateUtil.addDay(endTimes, day);
            System.out.println(DateUtil.sdfAll.format(beginTime) + "===================" + DateUtil.sdfAll.format(endTime));
            Date hourfDate = date(DateUtil.sdfAll.format(beginTime), chargeByHours.getFirstStartTime());
            Date hourllDate = date(DateUtil.sdfAll.format(endTime), chargeByHours.getFirstEndTime());


            if (beginTime.after(hourfDate) && endTime.before(hourllDate)) {//开始时间大于白天开始时间 并且  结束时间小于黑夜时间
                date1 = j == 0 ? beginTime : hourfDate;
                date2 = i > 0 && 1 != j ? hourllDate : endTime;
                dayMap = addDayMap(date1, date2);
                dayList.add(dayMap);
                if (j != i) {
                    date1 = hourllDate;
                    date2 = (i == 0 || i == j) && endTime.after(hourllDate) ? endTime : DateUtil.addDay(hourfDate, 1);
                    nightMap = addNightMap(date1,date2);
                    nightList.add(nightMap);
                }
            } else if ((beginTime.before(hourfDate)) && (endTime.after(hourllDate) || DateUtil.sdf.format(endTime).equals(DateUtil.sdf.format(hourllDate)))) { //开始时间小于白天开始时间 并且  结束时间大于黑夜时间
                //算出白天集合
                date1 = hourfDate;
                date2 = hourllDate;
                dayMap = addDayMap(date1,date2);
                dayList.add(dayMap);
                //计算黑夜集合
                date1 = hourllDate;
                date2 = (i == 0 || i == j) && endTime.after(hourllDate) ? endTime : DateUtil.addDay(hourfDate, 1);
                ///=======================
                if (j > 0 || endTime.after(hourllDate)) {
                    nightMap = addNightMap(date1,date2);
                    nightList.add(nightMap);
                }
                if (j == 0) {
                    //计算黑夜集合
                    date1 = beginTime;
                    date2 = hourfDate;
                    nightMap = addNightMap(date1,date2);
                    nightList.add(nightMap);
                }
            } else if (beginTime.before(hourfDate) && endTime.before(hourllDate)) { //开始时间小于白天开始时间 并且  结束时间小于黑夜时间
                //算出白天集合
                date1 = hourfDate;
                date2 = j == i ? endTime : hourllDate;
                dayMap = addDayMap(date1,date2);
                dayList.add(dayMap);
                //计算黑夜集合
                if (j == 0) {
                    date1 = beginTime;
                    if(i == 0 || i == j) {
                        if(endTime.before(hourfDate)) {
                            date2 = endTime;
                        }else{
                            date2 = hourfDate;
                        }
                    }else{
                        date2=hourfDate;
                    }
                    nightMap = addNightMap(date1,date2);
                    nightList.add(nightMap);
                }
                if (i > 0 && j != i) {
                    date1=hourllDate;
                    date2=DateUtil.addDay(hourfDate, 1);
                    nightMap = addNightMap(date1,date2);
                    nightList.add(nightMap);
                }

            }else if(beginTime.after(hourllDate) && endTime.after(hourllDate)){

                if(i==j) {
                    date1=hourllDate;
                    date2=endTime;
                }else{
                    date1=beginTime;
                    date2 =DateUtil.addDay(hourfDate,1);
                }
                nightMap = addNightMap(date1,date2);
                nightList.add(nightMap);
                if(j>0) {
                    //算出白天集合
                    date1 = hourfDate;
                    date2 = hourllDate;
                    dayMap = addDayMap(date1, date2);
                    dayList.add(dayMap);
                }
            }else if (beginTime.after(hourfDate) && endTime.after(hourllDate)) {//开始时间大于白天开始时间 并且  结束时间大于黑夜时间
                //算出白天集合
                if(beginTime.before(hourfDate)) {
                    date1 = beginTime;
                }else{
                    date1 = hourfDate;
                }
                date2 = hourllDate;
                dayMap = addDayMap(date1, date2);
                dayList.add(dayMap);

                //计算黑夜集合
                date1 = hourllDate;
                date2 = (i == 0 || i == j) && endTime.after(hourllDate) ? endTime : DateUtil.addDay(hourfDate, 1);
                nightMap = addNightMap(date1,date2);
                nightList.add(nightMap);

            } else if (beginTime.after(hourfDate) || DateUtil.sdf.format(beginTime).equals(DateUtil.sdf.format(hourfDate))) {
                if (endTime.before(hourllDate) && j == i) {
                    dayMap = addDayMap(beginTime,endTime);
                    dayList.add(dayMap);
                } else {
                    dayMap = addDayMap(beginTime,hourllDate);
                    dayList.add(dayMap);
                }
                if (endTime.after(hourllDate)) {
                    if (j == i) {
                        nightMap = addNightMap(hourllDate,endTime);
                        nightMap.put("beginDate", DateUtil.sdf.format(hourllDate));
                        nightMap.put("endDate", DateUtil.sdf.format(endTime));
                        nightList.add(nightMap);
                    } else {
                        nightMap = new HashMap();
                        nightMap.put("beginDate", DateUtil.sdf.format(hourllDate));
                        nightMap.put("endDate", DateUtil.sdf.format(DateUtil.addDay(hourfDate, 1)));
                        nightList.add(nightMap);
                    }
                } else if (j != i) {
                    date1 = hourllDate;
                    date2 = DateUtil.addDay(hourfDate, 1);
                    nightMap = new HashMap();
                    nightMap.put("beginDate", DateUtil.sdf.format(date1));
                    nightMap.put("endDate", DateUtil.sdf.format(date2));
                    nightList.add(nightMap);
                }
            } else if (beginTime.after(hourllDate)) { //开始时间大于结束时间
                Date hourfDates = DateUtil.addDay(hourfDate, j);
                if (endTime.before(hourfDates)) {
                    //算出白天集合
                    date1 = hourfDate;
                    date2 = endTime;

                    dayMap = new HashMap();
                    dayMap.put("beginDate", DateUtil.sdf.format(date1));
                    dayMap.put("endDate", DateUtil.sdf.format(date2));
                    dayList.add(dayMap);
                    //计算黑夜集合
                    date1 = hourllDate;
                    date2 = (i == 0 || i == j) && endTime.after(hourllDate) ? endTime : DateUtil.addDay(hourfDate, 1);
                    nightMap = new HashMap();
                    nightMap.put("beginDate", DateUtil.sdf.format(date1));
                    nightMap.put("endDate", DateUtil.sdf.format(date2));
                    nightList.add(nightMap);
                } else {
                    //计算黑夜集合
                    date1 = hourllDate;
                    date2 = endTime;
                    nightMap = new HashMap();
                    nightMap.put("beginDate", DateUtil.sdf.format(date1));
                    nightMap.put("endDate", DateUtil.sdf.format(date2));
                    nightList.add(nightMap);
                }
            }
        }
        maps.put("dayList", dayList);
        maps.put("nightList", nightList);
        return maps;
    }
}

