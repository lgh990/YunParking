package com.smart.iot.charge.util;

import com.yuncitys.smart.parking.common.util.DateUtil;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ChargingByUtil {

    /**
     * 计算停车总共时长
     * @param list 按天分割成集合
     * @return
     * @throws ParseException
     */
    public static int CalculatingMinutes(List list) throws ParseException {
        int minute=0;
        for(int i=0;i<list.size();i++){
            Map map= (Map) list.get(i);
            Date beginDate= DateUtil.sdf.parse(String.valueOf(map.get("beginDate")));
            Date endDate=DateUtil.sdf.parse(String.valueOf(map.get("endDate")));
            minute+=DateUtil.minuteDiff(endDate,beginDate);
        }
        return minute;
    }


    /**
     * date2比date1多的天数
     * @param date1 开始时间
     * @param date2 结束时间
     * @return
     */
    public static int differentDays(Date date1, Date date2)
    {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1= cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if(year1 != year2)   //同一年
        {
            int timeDistance = 0 ;
            for(int i = year1 ; i < year2 ; i ++)
            {
                if(i%4==0 && i%100!=0 || i%400==0)    //闰年
                {
                    timeDistance += 366;
                }
                else    //不是闰年
                {
                    timeDistance += 365;
                }
            }

            return timeDistance + (day2-day1) ;
        }
        else    //不同年
        {
            System.out.println("判断day2 - day1 : " + (day2-day1));
            return day2-day1;
        }
    }


    /**
     * * 获取指定日期是星期几
     * 参数为null时表示获取当前日期是星期几
     *
     * @param date
     * @return
     */
    public static int getWeekOfDate(Date date)
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
            //非工作日
            return 1;
        }
        else
        {
            //工作日
            return 2;
        }

    }
}
