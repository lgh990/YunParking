package com.yuncitys.smart.parking.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

/**
 * 日期工具类
 *
 *
 */
public class DateUtil {
    private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

    public static final String DEFAULT_FORMAT = "yyyy-MM-dd";
    public static final String DATE_INTERVAL_DD = "dd";

    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String LONG_DATE_FMT = "yyyyMMddHHmmss";
    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat sdfAll = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat sdf_yMdhms = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
    private static ThreadLocal<Calendar> calInstance = new ThreadLocal<Calendar>();

    private static final ThreadLocal<SimpleDateFormat> sdf_hms = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };
    private static final ThreadLocal<SimpleDateFormat> sdf_hm = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm");
        }
    };

    private DateUtil() {

    }

    /**
     * 返回两个日期的分钟数差
     *
     * @param d1
     * @param d2
     * @return
     */
    public static int minuteDiff(Date d1, Date d2) {
        if (d1 == null || d2 == null) {
            return 0;
        }
        Long diff = (d1.getTime() - d2.getTime()) / 1000 / 60;
        return diff.intValue();
    }

    /**
     * 格式-- yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String formatDateHMS(Long date) {
        if (date == null) {
            return null;
        }
        return sdf_hms.get().format(toRealDate(date));
    }

    public static String formatDateHM(Date date) {
        if (date == null) {
            return null;
        }
        return sdf_hm.get().format(date);
    }

    public static String formatDate(Date date, String format) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdff = new SimpleDateFormat(format);
        return sdff.format(date);
    }

    public static String formatDate(Long date, String format) {
        if (date == null) {
            return null;
        }
        return formatDate(toRealDate(date), format);
    }

    public static int getSpecialTime(Date date, int type) {
        Calendar calendar = getCalendar();
        calendar.setTime(date);
        return calendar.get(type);
    }

    /**
     * 为了避免线程冲突，每个线程保存一个Calendar实例 -- by PanJun
     *
     * @return 线程级Calendar实例
     */
    private static Calendar getCalendar() {
        if (calInstance.get() == null) {
            calInstance.set(Calendar.getInstance());
        }
        Calendar ret = calInstance.get();
        ret.clear();
        return ret;
    }

    /**
     * 将日期转换为长整型数字
     *
     * @param date
     * @param formatStr
     * @return
     */
    public static Long toLongDate(Date date, String formatStr) {
        if (date == null) {
            return null;
        }
        if (formatStr == null) {
            formatStr = "yyyyMMddHHmmss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        return Long.valueOf(sdf.format(date));
    }

    /**
     * 获取13周前的日期(不算本周，每周第一天是以周一开始算)
     *
     * @param stage 是否本期
     * @return
     */
    public static String prevWeek(String stage) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        int week = 13; // 取13周前的日期
        Calendar cal = Calendar.getInstance();
        // 不是本期，取上期的
        if (!"Y".equals(stage)) {
            cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 1);
        }
        // 获取当前日期是星期几
        int wd = cal.get(Calendar.DAY_OF_WEEK);
        // 如果不是等于星期一
        if (wd != 2) {
            if (wd < 2) {// 如果是星期日。加到星期一再做计算
                cal.add(Calendar.DAY_OF_MONTH, 1);
            } else {// 如果是星期二到星期六，减到星期一再做计算
                cal.add(Calendar.DAY_OF_MONTH, -(wd - 2));
            }
        }
        // 减掉13周
        cal.add(Calendar.DAY_OF_MONTH, -(week * 7));
        return sdf.format(cal.getTime()) + "000000";
    }

    /**
     * 当前周的周一
     *
     * @param stage 是否本期
     * @return
     */

    public static String curWeek(String stage) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        // 不是本期，取上期的
        if (!"Y".equals(stage)) {
            cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 1);
        }
        // 获取当前日期是星期几
        int wd = cal.get(Calendar.DAY_OF_WEEK);
        // 如果不是等于星期一
        if (wd != 2) {
            if (wd < 2) {// 如果是星期日。加到星期一再做计算
                cal.add(Calendar.DAY_OF_MONTH, 1);
            } else {// 如果是星期二到星期六，减到星期一再做计算
                cal.add(Calendar.DAY_OF_MONTH, -(wd - 2));
            }
        }
        return sdf.format(cal.getTime()) + "000000";
    }

    /**
     * 获取当前时间的周
     *
     * @param dateStr 时间：格式yyyyMMddHHmmss
     * @return
     * @throws ParseException
     */
    public static int getWeek(String dateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = sdf.parse(dateStr);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week = cal.get(Calendar.WEEK_OF_YEAR);
        return week;
    }

    /**
     * 得到指定日期是星期几
     *
     * @param date
     * @return
     */
    public static int getDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static int getDayOfWeek2(Date date) {
        int week = getDayOfWeek(date);
        if (week == 1) {
            return 7;
        } else {
            return week - 1;
        }
    }

    public static int getDayOfWeekForLong(long date) {
        Date newDate = toRealDate(date);
        int week = getDayOfWeek(newDate);
        if (week == 1) {
            return 7;
        } else {
            return week - 1;
        }
    }

    /**
     * 把日期加天数
     *
     * @param date
     * @param day
     * @return
     */
    public static Date addDay(Date date, int day) {
        if (date == null) {
            return null;
        }

        if (day == 0) {
            return date;
        }
        Calendar calendar = getCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }

    /**
     * 把日期加小时
     *
     * @param date
     * @param hour
     * @return
     */
    public static Date addHour(Date date, int hour) {
        if (date == null) {
            return null;
        }

        Calendar calendar = getCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, hour);
        return calendar.getTime();
    }

    /**
     * 加分钟
     *
     * @param date
     * @param minute
     * @return
     */
    public static Date addMinute(Date date, int minute) {
        if (date == null) {
            return null;
        }

        Calendar calendar = getCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    /**
     * 把日期加秒
     *
     * @param date
     * @param second
     * @return
     */
    public static Date addSecond(Date date, int second) {
        if (date == null) {
            return null;
        }

        Calendar calendar = getCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, second);
        return calendar.getTime();
    }

    /**
     * 把日期加天数，返回结果
     *
     * @param date
     * @param day
     * @return
     */
    public static Long addDay(Long date, int day) {
        if (date == null) {
            return null;
        }

        Date realDate = addDay(toRealDate(date), day);
        return toLongDate(realDate);
    }

    /**
     * 请参看Calender.get(field)函数
     *
     * @param date
     * @param field
     * @return
     */
    public static int get(Date date, int field) {
        if (date == null) {
            return -1;
        }

        Calendar cal = getCalendar();
        cal.setTime(date);
        return cal.get(field);
    }

    /**
     * 请参看Calender.get(field)函数
     *
     * @param date
     * @param field
     * @return
     */
    public static int get(Long date, int field) {
        return get(toRealDate(date), field);
    }

    /**
     * 把长整形日期转换成真实日期
     *
     * @param date
     * @return
     */
    public static Date toRealDate(Long date) {
        if (date == null) {
            return null;
        }

        Calendar calendar = getCalendar();
        int ss = (int) (date - (date / 100) * 100);
        calendar.set(Calendar.SECOND, ss);

        int mi = (int) ((date - (date / 10000) * 10000) / 100);
        calendar.set(Calendar.MINUTE, mi);

        int hh = (int) ((date - (date / 1000000) * 1000000) / 10000);
        calendar.set(Calendar.HOUR_OF_DAY, hh);

        int dd = (int) ((date - (date / 100000000) * 100000000) / 1000000);
        calendar.set(Calendar.DAY_OF_MONTH, dd);

        int yy = (int) (date / 10000000000l);
        calendar.set(Calendar.YEAR, yy);

        int noYear = (int) (date - yy * 10000000000l);
        int mm = noYear / 100000000;
        calendar.set(Calendar.MONTH, mm - 1);
        return calendar.getTime();
    }

    /**
     * Date类型转换成Long型日期
     *
     * @param date
     * @return
     */
    public static Long toLongDate(Date date) {
        if (date == null) {
            return null;
        }
        return Long.valueOf(new SimpleDateFormat(LONG_DATE_FMT).format(date));
    }

    /**
     * Date类型转换成String型日期
     *
     * @param date
     * @return
     */
    public static String toStrDate(Long date, String type) {
        Date newDate = toRealDate(date);
        if (YYYY_MM_DD.equals(type)) {
            return dateToStr(newDate);
        } else {
            return dateToStr(newDate, YYYY_MM_DD_HH_MM_SS);
        }
    }

    /**
     * Date类型转换成String型日期
     *
     * @param date
     * @return
     */
    public static String toStrDateDef(Long date) {
        Date newDate = toRealDate(date);
        return dateToStr(newDate);
    }

    public static Long toLongDateStr(String date) {
        if (date == null) {
            return null;
        }

        Date newDate = strToDate(date);
        return toLongDate(newDate);
    }

    public static Long toLongDateStrWithMin(String date) {
        if (date == null) {
            return null;
        }

        Date newDate = strToDate(date, YYYY_MM_DD_HH_MM_SS);
        return toLongDate(newDate, null);
    }

    /**
     * 把日期加月份
     *
     * @param date
     * @param month
     * @return
     */
    public static Date addMonth(Date date, int month) {
        if (date == null) {
            return null;
        }

        Calendar calendar = getCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, month);
        return calendar.getTime();
    }

    /**
     * 把日期加月份
     *
     * @param date
     * @param month
     * @return
     */
    public static Long addMonth(Long date, int month) {
        if (date == null) {
            return null;
        }

        Date realDate = addMonth(toRealDate(date), month);
        return toLongDate(realDate);
    }

    /**
     * 日期加年
     *
     * @param date
     * @param year
     * @return
     */
    public static Date addYear(Date date, int year) {
        if (date == null) {
            return null;
        }

        Calendar calendar = getCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, year);
        return calendar.getTime();
    }

    /**
     * 把日期的时分秒去除只留年月日
     *
     * @param date
     * @return 只留年月日的日期
     */
    public static Date clearTime(Date date) {
        if (date == null) {
            return null;
        }

        Calendar calendar = getCalendar();
        calendar.setTime(date);
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH);
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.clear();
        calendar.set(Calendar.YEAR, y);
        calendar.set(Calendar.MONTH, m);
        calendar.set(Calendar.DAY_OF_MONTH, d);
        return calendar.getTime();
    }

    /**
     * 把日期的时分秒去除只留年月日 20101230115511 == >20101230115511
     *
     * @param date
     * @return 只留年月日的日期
     */
    public static Long clearTime(Long date) {
        if (date == null) {
            return null;
        }

        return (date / 1000000) * 1000000;
    }

    /**
     * 日期转化为字串
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String dateToStr(Date date, String pattern) {
        if (date == null || pattern == null) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * 格式化时间
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String dateToStr(Long date, String pattern) {
        if (date == null || pattern == null) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(toRealDate(date));
    }

    /**
     * 日期时间转化为字串yyyy-MM-dd HH:mm:SS
     *
     * @param date
     * @return
     */
    public static String dateTimeToStr(Date date) {
        return dateToStr(date, YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 把日期转化成"yyyy-MM-dd"格式的字串
     *
     * @param date
     * @return
     */
    public static String dateToStr(Date date) {
        return dateToStr(date, YYYY_MM_DD);
    }

    /**
     * 字串转化成日期
     *
     * @param date
     * @param pattern
     * @return
     */
    public static Date strToDate(String date, String pattern) {
        if (StringUtil.isBlank(date) || pattern == null) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 返回两个日期之间的天数差
     *
     * @param d1
     * @param d2
     * @return
     */
    public static int dateDiff(Date d1, Date d2) {
        if (d1 == null || d2 == null) {
            throw new NullPointerException("dateDiff方法两个参数不能为null!");
        }

        Long diff = (d1.getTime() - d2.getTime()) / 1000 / 60 / 60 / 24;
        return diff.intValue();
    }

    /**
     * 返回两个日期之间的天数差
     *
     * @param d1
     * @param d2
     * @return
     */
    public static int dateDiff(Long d1, Long d2) {
        return dateDiff(toRealDate(d1), toRealDate(d2));
    }

    /**
     * 把"yyyy-MM-dd"格式的字串转化成日期
     *
     * @param date
     * @return
     */
    public static Date strToDate(String date) {
        return strToDate(date, YYYY_MM_DD);
    }

    /**
     * 判断给定日期是否为当天
     *
     * @return
     */
    public static boolean isTodayDate(Long datel) {
        Date dt = DateUtil.toRealDate(datel);

        Calendar calendar = getCalendar();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long beginTime = calendar.getTime().getTime();

        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        long endTime = calendar.getTime().getTime();

        if (dt.getTime() >= beginTime && dt.getTime() <= endTime) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 以指定格式格式化日期，并输出字符串
     */
    public static String format(Date date, String formatStr) {
        if (date == null) {
            return "";
        }
        if (formatStr == null) {
            formatStr = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        return sdf.format(date);
    }

    /**
     * 获取一个月的第一天
     *
     * @return
     */
    public static Date getMonthFirstDay(Date date) {
        Calendar calendar = getCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * 获取指定星期的日期
     *
     * @param date
     * @return
     */
    public static Date getWeekDate(Date date, int week) {
        Calendar calendar = getCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, week);
        return calendar.getTime();
    }

    /**
     * 获取每年的一月1号
     *
     * @param date
     * @return
     */
    public static Date getYearFirstDay(Date date) {
        Calendar calendar = getCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        return calendar.getTime();
    }

    public static Long getTomorrowDay(Long now) {
        String nows = now.toString();
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date dnow = null;
        try {
            dnow = sf.parse(nows);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dnow);
            cal.add(Calendar.DAY_OF_MONTH, 1);
            Date tomoDate = cal.getTime();
            return toLongDate(tomoDate);
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        }
        return 0L;
    }

    public static Long getYesterdayDay(Long now) {
        String nows = now.toString();
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date dnow = null;
        try {
            dnow = sf.parse(nows);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dnow);
            cal.add(Calendar.DAY_OF_MONTH, -1);
            Date tomoDate = cal.getTime();
            return toLongDate(tomoDate);
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        }
        return 0L;
    }

    public static boolean isOneDay(Long l1, Long l2) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        String s1 = l1.toString();
        Date d1 = null;
        try {
            d1 = sf.parse(s1);
            Calendar cal = Calendar.getInstance();
            cal.setTime(d1);
            cal.add(Calendar.DAY_OF_MONTH, -1);
            Date d3 = cal.getTime();
            Long l3 = toLongDate(d3);
            if (l2.toString().equals(l3.toString())) {
                return true;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    public static Date parse(String source, String formatStr) {
        if (source == null) {
            return null;
        }
        if (formatStr == null) {
            formatStr = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        Date date = null;
        try {
            date = sdf.parse(source);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 日期运算
     *
     * @param field
     * @param amount
     * @param date
     * @return
     */
    public static Date addDate(String field, int amount, Date date) {
        if (amount == 0) {
            return date;
        }
        Calendar calendar = getCalendar();
        calendar.setTime(date);
        if ("yyyy".equals(field)) {
            calendar.add(Calendar.YEAR, amount);
        }
        if ("MM".equals(field)) {
            calendar.add(Calendar.MONTH, amount);
        }
        if ("dd".equals(field)) {
            calendar.add(Calendar.DATE, amount);
        }
        return calendar.getTime();
    }

    public static String format(Date date) {
        return format(date, null);
    }

    public static String dateAdd(String item, int amount, String startDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        if (startDate != null) {
            if (startDate.length() < 11) {
                startDate = (new StringBuilder(String.valueOf(startDate))).append(" 00:00:00").toString();
            }
            date = sdf.parse(startDate);
        } else {
            date = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if ("yyyy".equals(item)) {
            calendar.add(1, amount);
        }
        if ("MM".equals(item)) {
            calendar.add(2, amount);
        }
        if ("dd".equals(item)) {
            calendar.add(5, amount);
        }
        return sdf.format(calendar.getTime());
    }

    /**
     * 获取后一年,只精确到天
     *
     * @param date
     * @return
     */
    public static long getLastYear(String date) {
        if (null == date || "".equals(date)) {
            date = DateUtil.format(new Date(), DEFAULT_FORMAT);
        }
        Long d = DateUtil.toLongDate(DateUtil.parse(date, DEFAULT_FORMAT));
        return d.longValue() + 10000000000L;
    }

    /**
     * 获取前一年,只精确到天
     *
     * @param date
     * @return
     */
    public static long getBeforYear(String date) throws Exception {
        if (null == date || "".equals(date)) {
            date = DateUtil.format(new Date(), DEFAULT_FORMAT);
        }
        Long d = DateUtil.toLongDate(DateUtil.parse(date, DEFAULT_FORMAT));
        return d.longValue() - 10000000000L;

    }

    /**
     * 获取当前日期,只精确到天
     *
     * @return
     */
    public static long getNowDate() throws Exception {
        String d = DateUtil.format(new Date(), DEFAULT_FORMAT);
        Long date = DateUtil.toLongDate(DateUtil.parse(d, DEFAULT_FORMAT));
        return date.longValue();
    }

    /**
     * 计算日期相差的天数
     *
     * @param start
     * @param end
     * @return
     */
    public static long getDiff(Long start, Long end) {
        String fr = "yyyy-MM-dd";
        if (start == null) {
            String d = DateUtil.format(new Date(), DEFAULT_FORMAT);
            start = DateUtil.toLongDate(DateUtil.parse(d, DEFAULT_FORMAT));
        }
        if (end == null) {
            String d = DateUtil.format(new Date(), DEFAULT_FORMAT);
            end = DateUtil.toLongDate(DateUtil.parse(d, DEFAULT_FORMAT));
        }
        Date startD = DateUtil.toRealDate(start);
        Date endD = DateUtil.toRealDate(end);
        return (endD.getTime() - startD.getTime()) / (1000 * 60 * 60 * 24);
    }

    /**
     * 计算日期相差的时间戳
     *
     * @param start
     * @param end
     * @return
     */
    public static long getDiffTimeStamp(String start, String end) {
        long startLong = DateUtil.parse(start, YYYY_MM_DD_HH_MM_SS).getTime();
        long endLong = 0;
        if (end == null) {
            String d = DateUtil.format(new Date(), YYYY_MM_DD_HH_MM_SS);
            endLong = DateUtil.parse(d, YYYY_MM_DD_HH_MM_SS).getTime();
        } else {
            endLong = DateUtil.parse(end, YYYY_MM_DD_HH_MM_SS).getTime();
        }
        return (endLong - startLong) / 1000;
    }

    /**
     * 获得当前日期和时间，以字符串形式返回
     *
     * @return 日期时间字符串
     */
    public static String getDateTime() {
        return format(Calendar.getInstance().getTime(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获得当前日期以字符串形式返回
     *
     * @return 日期字符串
     */
    public static String getDate() {
        return format(Calendar.getInstance().getTime(), "yyyy-MM-dd");
    }

    /**
     * 获得当前时间以字符串形式返回
     *
     * @return 时间字符串
     */
    public static String getTime() {
        return format(Calendar.getInstance().getTime(), "HH:mm:ss");
    }

    /**
     * 日期时间差
     *
     * @param item      差项，可以是yyyy,MM,dd,HH,mm,ss
     * @param startDate 起点时间
     * @param endDate   末点时间
     * @return 起点与末点时间这间的差值，字符串
     * @throws ParseException 如果日期时间转换失败时抛出
     */
    public static int dateDiff(String item, String startDate, String endDate){

        int result = 0;
        Date date = null;

        Calendar calendar = Calendar.getInstance();
        if (startDate != null) {
            if (startDate.length() < 11) {
                startDate += " 00:00:00";
            }
            try {
                date = sdf_yMdhms.parse(startDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            date = new Date();
        }
        calendar.setTime(date);
        long n1 = calendar.getTimeInMillis();

        if (endDate != null) {
            if (endDate.length() < 11) {
                endDate += " 00:00:00";
            }
            try {
                date = sdf_yMdhms.parse(endDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            date = new Date();
        }
        calendar.setTime(date);
        long n2 = calendar.getTimeInMillis();

        if ("yyyy".equals(item)) {
        }
        if ("MM".equals(item)) {
        }
        if ("dd".equals(item)) {
            result = (int) ((n2 - n1) / (1000 * 60 * 60 * 24));
        }
        if ("HH".equals(item)) {
            result = (int) ((n2 - n1) / (1000 * 60 * 60));
        }
        if ("mm".equals(item)) {
            result = (int) ((n2 - n1) / (1000 * 60) % 60);
        }
        return result;

    }

    public static XMLGregorianCalendar convertToXMLGregorianCalendar(Date date) {

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        XMLGregorianCalendar gc = null;
        try {
            gc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return gc;
    }

    /**
     * 获取当期日期的周一(不算本周，每周第一天是以周一开始算)
     *
     * @param stage 是否本期
     * @return
     */
    public static String currWeek(String stage) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        // 不是本期，取上期的
        if (!"Y".equals(stage)) {
            cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 1);
        }
        // 获取当前日期是星期几
        int wd = cal.get(Calendar.DAY_OF_WEEK);
        // 如果不是等于星期一
        if (wd != 2) {
            if (wd < 2) {// 如果是星期日。加到星期一再做计算
                cal.add(Calendar.DAY_OF_MONTH, 1);
            } else {// 如果是星期二到星期六，减到星期一再做计算
                cal.add(Calendar.DAY_OF_MONTH, -(wd - 2));
            }
        }
        return sdf.format(cal.getTime());
    }

    /**
     * 获取本周的周一日期
     */
    // 获得当前周- 周一的日期
    public static String getCurrentMonday() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        int day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 2;
        cal.add(Calendar.DATE, -day_of_week);
        return sdf.format(cal.getTime());
    }

    /**
     * 获取一个月的第一天
     *
     * @return
     */
    public static String currMonth(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = getCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return sdf.format(calendar.getTime());
    }

    // 获得当前时间一个月前的开始时间
    public static String getLastMonthStartTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(cal.MONTH, -1);
        int minDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), minDay, 00, 00, 00);
        return sdf.format(cal.getTime());
    }

    // 获得当前时间一个月前的结束时间
    public static String getLastMonthEndTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(cal.MONTH, -1);
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), maxDay, 23, 59, 59);
        return sdf.format(cal.getTime());
    }

    /**
     * 获取当前年的第一天
     *
     * @return
     */
    public static String currYear(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = getCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        return sdf.format(calendar.getTime());
    }

    /**
     * 获取字符串相差时间
     *
     * @return
     */
    public static String getDateDiff(String beginDate, String endDate) {
        if (!endDate.equals("0") && endDate != null) {
            long t = DateUtil.parse(endDate, null).getTime()
                    - DateUtil.parse(beginDate, null).getTime();
            long d = t / (24 * 60 * 60 * 1000);// 天
            long h = (t - d * 24 * 60 * 60 * 1000) / (60 * 60 * 1000);
            long m = (t - d * 24 * 60 * 60 * 1000 - h * 60 * 60 * 1000) / (60 * 1000);

            return (d + "天" + h + "小时" + m + "分钟").toString();
        } else {
            return "-";
        }
    }

    /**
     * 获取字符串相差时间
     *
     * @return
     */
    public static String getDateDiffs(String beginDate, String endDate) {
        if (!endDate.equals("0") && endDate != null) {
            long t = DateUtil.parse(endDate, null).getTime()
                    - DateUtil.parse(beginDate, null).getTime();
            long d = t / (24 * 60 * 60 * 1000);// 天
            long h = (t - d * 24 * 60 * 60 * 1000) / (60 * 60 * 1000);
            long m = (t - d * 24 * 60 * 60 * 1000 - h * 60 * 60 * 1000) / (60 * 1000);

            return (d + "天" + h + "时" + m + "分").toString();
        } else {
            return "-";
        }
    }

    /**
     * 返回指定时间的年份
     *
     * @param date
     * @return
     */
    public static String getYear(Date date) {
        SimpleDateFormat formatStr = new SimpleDateFormat("yyyy");
        return formatStr.format(date);
    }

    /**
     * 返回指定时间的月份
     *
     * @param date
     * @return
     */
    public static String getMonth(Date date) {
        SimpleDateFormat formatStr = new SimpleDateFormat("MM");
        return formatStr.format(date);
    }

    /**
     * 返回指定时间的日期
     *
     * @param date
     * @return
     */
    public static String getDay(Date date) {
        SimpleDateFormat formatStr = new SimpleDateFormat("dd");
        return formatStr.format(date);
    }

    /**
     * 获取某月的最后一天
     *
     * @throws
     * @Title:getLastDayOfMonth
     * @Description:
     * @param:@param year
     * @param:@param month
     * @param:@return
     * @return:String
     */
    public static String getLastDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month - 1);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String lastDayOfMonth = sdf.format(cal.getTime());

        return lastDayOfMonth;
    }

    /**
     * * 获取指定日期是星期几
     * 参数为null时表示获取当前日期是星期几
     *
     * @param date
     * @return
     */
    public static String getWeekOfDate(Date date) {
        String[] weekOfDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }

        if (weekOfDays[w].equals("星期六") || weekOfDays[w].equals("星期日")) {
            return "非工作日";
        } else {
            return "工作日";
        }

    }
    /**
     * * 获取指定日期是星期几
     * 参数为null时表示获取当前日期是星期几
     *
     * @param date
     * @return
     */
    public static int getWeekNumOfDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        return calendar.get(Calendar.DAY_OF_WEEK);

    }
    /**
     * * 获取指定日期是星期几
     * 参数为null时表示获取当前日期是星期几
     *
     * @return
     */
    public static Timestamp getCurrTimestamp()
    {
        Date date =new Date();
        return new Timestamp(date.getTime());
    }
    public static String getOrderNum() {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String num1 = dateFormat.format(now);
        Random ran = new Random();
        int s = ran.nextInt(9999);
        String num = num1 + s;

        return num;
    }
    public static boolean getDatePoor(Date date){
        long s = (System.currentTimeMillis() - date.getTime()) / (1000 * 60);
        if(s>=3){
            return false;
        }
        return true;
    }
}
