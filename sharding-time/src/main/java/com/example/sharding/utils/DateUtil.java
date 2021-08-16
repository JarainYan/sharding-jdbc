package com.example.sharding.utils;


import com.alibaba.druid.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
public class DateUtil {

    public static final String DATE_FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_NUMBER = "yyyyMMddHHmmss";
    public static final String YEAR_MONTH_DAY_NUMBER = "yyyyMMdd";
    public static final String YEAR_MONTH_NUMBER = "yyyyMM";
    public static final String DATE_FORMAT_DAY_PATTERN = "yyyy-MM-dd";
    public static final String YEAR_MONTH_DAY_EN_SECOND = "yyyy/MM/dd HH:mm:ss";
    public static final String YEAR_MONTH_DAY_CN_SECOND = "yyyy年MM月dd日 HH时mm分ss秒";
    public static final String YEAR_MONTH_DAY_CN = "yyyy年MM月dd日";

    /**
     * 采用 ThreadLocal 避免 SimpleDateFormat 非线程安全的问题
     * <p>
     * Key —— 时间格式
     * Value —— 解析特定时间格式的 SimpleDateFormat
     */
    private static ThreadLocal<Map<String, SimpleDateFormat>> sThreadLocal = new ThreadLocal<>();


    /**
     * 获取解析特定时间格式的 SimpleDateFormat
     *
     * @param pattern 时间格式
     */
    private static SimpleDateFormat getDateFormat(String pattern) {
        if (StringUtils.isEmpty(pattern)) {
            pattern = DATE_FORMAT_DEFAULT;
        }
        Map<String, SimpleDateFormat> strDateFormatMap = sThreadLocal.get();
        if (strDateFormatMap == null) {
            strDateFormatMap = new HashMap<>();
        }

        SimpleDateFormat simpleDateFormat = strDateFormatMap.get(pattern);
        if (simpleDateFormat == null) {
            simpleDateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
            strDateFormatMap.put(pattern, simpleDateFormat);
            sThreadLocal.set(strDateFormatMap);
        }
        return simpleDateFormat;
    }


    public static Date toDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static String format(LocalDateTime dateTime, String format) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ofPattern(format));
    }

    public static LocalDateTime parse(String dateTimeStr, String format) {
        if (StringUtils.isEmpty(format)) {
            format = DATE_FORMAT_DEFAULT;
        }
        if (dateTimeStr == null) {
            return null;
        }
        DateTimeFormatter sf = DateTimeFormatter.ofPattern(format);
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, sf);
        return dateTime;
    }

    public static String format(LocalDateTime dateTime) {
        return format(dateTime, DATE_FORMAT_DEFAULT);
    }

    public static LocalDateTime parse(String dateTimeStr) {
        return parse(dateTimeStr, DATE_FORMAT_DEFAULT);
    }

    // #############################################################################

    /**
     * 时间格式转换为字符串格式
     *
     * @param date   时间
     * @param format 格式 如("yyyy-MM-dd hh:mm:ss")
     * @return String
     */
    public static String date2Str(Date date, String format) {
        if (StringUtils.isEmpty(format)) {
            format = DATE_FORMAT_DEFAULT;
        }
        if (date == null) {
            return null;
        }
        return getDateFormat(format).format(date);
    }

    /**
     * 字符串格式转换为时间格式
     *
     * @param dateStr 字符串
     * @param format  格式 如("yyyy-MM-dd HH:mm:ss")
     * @return Date
     */
    public static Date str2Date(String dateStr, String format) {
        if (StringUtils.isEmpty(format)) {
            format = DATE_FORMAT_DEFAULT;
        }
        if (StringUtils.isEmpty(dateStr)) {
            return null;
        }
        try {
            return getDateFormat(format).parse(dateStr);
        } catch (ParseException pe) {
            log.error("str2Date ParseException error", pe);
        }
        return null;
    }

    public static Date dateFormat(Date date, String format) {
        if (StringUtils.isEmpty(format)) {
            format = DATE_FORMAT_DEFAULT;
        }
        if (date == null) {
            return null;
        }
        return str2Date(date2Str(date, format), format);
    }

    /**
     * @param timestamp：时间
     */
    public static Date parse(long timestamp) {
        return new Date(timestamp);
    }

    /**
     * 获取当前日期
     *
     * @param pattern 格式如:yyyy-MM-dd
     * @return
     */
    public static String getCurrentDate(String pattern) {
        return getDateFormat(pattern).format(new Date());
    }

    /**
     * get cuurent Date return java.util.Date type
     */
    public static Date getCurrentDate() {
        return new Date();
    }

    /**
     * get current Date return java.sql.Date type
     *
     * @return
     */
    public static java.sql.Date getNowSqlDate() {
        return new java.sql.Date(System.currentTimeMillis());
    }

    /**
     * get the current timestamp return java.sql.Timestamp
     *
     * @return
     */
    public static Timestamp getNowTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static String timestamp2Str(Timestamp time, String format) {
        return getDateFormat(format).format(time);
    }

    public static Date timestamp2Date(Timestamp time, String format) throws ParseException {
        return str2Date(timestamp2Str(time, format), format);
    }

    public static Timestamp str2Timestamp(Timestamp time, String format) throws ParseException {
        Date date = str2Date(timestamp2Str(time, format), format);
        return new Timestamp(date.getTime());
    }

    public static Timestamp date2Timestamp(Date date, String format) {
        Date result = str2Date(date2Str(date, format), format);
        return new Timestamp(result.getTime());
    }

    /**
     * time1>time2 返回正数
     *
     * @param time1
     * @param time2
     * @return
     */
    public static long getOffsetBetweenTimes(String time1, String time2) {
        return str2Date(time1, DATE_FORMAT_DEFAULT).getTime() - str2Date(time2, DATE_FORMAT_DEFAULT).getTime();
    }


    /**
     * 返回时间差
     *
     * @param start
     * @param end
     * @param timeUnit 天d, 时h, 分m, 秒s
     * @return 根据timeUnit返回
     */
    public static long getOffsetBetweenDate(Date start, Date end, char timeUnit) {
        long startT = start.getTime(); //定义上机时间
        long endT = end.getTime();  //定义下机时间

        long ss = (startT - endT) / (1000); //共计秒数
        int mm = (int) ss / 60;   //共计分钟数
        int hh = (int) ss / 3600;  //共计小时数
        int dd = hh / 24;   //共计天数
        System.out.println("######################## 共" + dd + "天 准确时间是：" + hh + " 小时 " + mm + " 分钟" + ss + " 秒 共计：" + ss * 1000 + " 毫秒");

        long result = 0L;
        if ('d' == timeUnit) {
            result = dd;
        } else if ('h' == timeUnit) {
            result = hh;
        } else if ('m' == timeUnit) {
            result = mm;
        } else if ('s' == timeUnit) {
            result = ss;
        }
        return result;
    }

    /**
     * 对指定日期滚动指定天数,负数时,则往前滚,正数时,则往后滚
     *
     * @param date Date
     * @param days int
     * @return String
     */
    public static String rollDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return date2Str(calendar.getTime(), DATE_FORMAT_DEFAULT);
    }

    /**
     * 对指定日期滚动指定分钟,负数时,则往前滚,正数时,则往后滚
     *
     * @param date    Date
     * @param minutes int
     * @return String
     */
    public static String rollMinutes(Date date, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minutes);
        return date2Str(calendar.getTime(), DATE_FORMAT_DEFAULT);
    }

    /**
     * 对指定日期滚动指定分钟,负数时,则往前滚,正数时,则往后滚
     *
     * @param date    Date
     * @param seconds int
     * @return String
     */
    public static String rollSeconds(Date date, int seconds, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, seconds);
        return date2Str(calendar.getTime(), format);
    }

    /**
     * 返回  2013-01-16T06:24:26.829Z 时间
     *
     * @param date
     * @return
     * @throws Exception
     */
    public static XMLGregorianCalendar getXmlDatetime(Date date) throws Exception {
        if (null == date) {
            date = new Date();
        }
        GregorianCalendar nowGregorianCalendar = new GregorianCalendar();
        XMLGregorianCalendar xmlDatetime = DatatypeFactory.newInstance().newXMLGregorianCalendar(nowGregorianCalendar);
        // XMLGregorianCalendar ->GregorianCalendar
        nowGregorianCalendar = xmlDatetime.toGregorianCalendar();
        nowGregorianCalendar.setTime(date);
        return xmlDatetime;
    }

    public static boolean checkDateBettwenBoth(Date checkDate, Date date1, Date date2) {
        boolean temp = false;
        if (checkDate == null || date1 == null || date2 == null) {
            temp = false;
            return temp;
        }

        if (checkDate.equals(date1) || checkDate.equals(date2)) {
            temp = true;
        }

        if (checkDate.after(date1) && checkDate.before(date2)) {
            temp = true;
        }

        return temp;
    }

    public static String getFormatDatetime() throws Exception {
        GregorianCalendar gCalendar = new GregorianCalendar();
        String strDateTime;
        try {
            strDateTime = getDateFormat(DATE_FORMAT_DEFAULT).format(gCalendar.getTime());
        } catch (Exception ex) {
            System.out.println("Error Message:".concat(String.valueOf(String.valueOf(ex.toString()))));
            String s = null;
            return s;
        }
        return strDateTime;
    }


    /**
     * 判断给定的日期是一年中的第几天
     *
     * @param dateTimeStr
     * @return
     */
    public static int dayOfYear(String dateTimeStr) {
        int day = 0;
        if (StringUtils.isEmpty(dateTimeStr)) {
            return day;
        }
        Date dateTime = str2Date(dateTimeStr, DATE_FORMAT_DEFAULT);
        return dayOfYear(dateTime);
    }


    /**
     * 判断给定的日期是一年中的第几天
     *
     * @param dateTime
     * @return
     */
    public static int dayOfYear(Date dateTime) {
        int year = 0, month = 0, date = 0, day = 0;
        if (null == dateTime) {
            return day;
        }
        String dateTimeStr = date2Str(dateTime, null);
        LocalDateTime localDateTime = parse(dateTimeStr);
        year = localDateTime.getYear();
        month = localDateTime.getMonthValue();
        date = localDateTime.getDayOfMonth();
        return dayOfYear(year, month, date);
    }


    /**
     * 判断给定的日期是一年中的第几天
     *
     * @param year
     * @param month
     * @param date
     * @return
     */
    public static int dayOfYear(int year, int month, int date) {
        int[] days = {0, 31, 0, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int day = 0;
        //判断是不是闰年，然后设置二月的天数
        if ((year % 400 == 0) || ((year % 100 != 0) && (year % 4 == 0))) {
            days[2] = 29;
        } else {
            days[2] = 28;
        }
        for (int i = 1; i < month; i++) {
            day += days[i];
        }
        day += date;
        return day;
    }

    /**
	 * 
	    * @Title: morrowZero
	    * @Description:获取明天零点时间
	    * @param @return    参数
	    * @return Date    返回类型
	    * @throws
	 */
	public static Date getTomorrowZero() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		Date time = calendar.getTime();
		return time;
	}

	/**
	 * 
	    * @Title: getEndOfDay
	    * @Description:获得某天最大时间 2020-02-19 23:59:59
	    * @param @param date
	    * @param @return    参数
	    * @return Date    返回类型
	    * @throws
	 */
	 
    public static Date getEndOfDay(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());;
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 
        * @Title: getStartOfDay
        * @Description: 获得某天最小时间 2020-02-17 00:00:00
        * @param @param date
        * @param @return    参数
        * @return Date    返回类型
        * @throws
     */
    public static Date getStartOfDay(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }
 
    
	/**
	 * 
	    * @Title: formatTime
	    * @Description: 将秒 换算成( 时 分 秒 )
	    * @param @param ms
	    * @param @return    参数
	    * @return String    返回类型
	    * @throws
	 */
    public static String formatTime(Long param) {
		Integer ss = 1;
		Integer mi = ss * 60;
		Integer hh = mi * 60;

		Long hour = (param) / hh;
		Long minute = (param - hour * hh) / mi;
		Long second = (param - hour * hh - minute * mi) / ss;
		StringBuffer sb = new StringBuffer();
		if (hour > 0) {
			sb.append(hour + "小时");
		}
		if (minute > 0) {
			sb.append(minute + "分");
		}
		if (second > 0) {
			sb.append(second + "秒");
		}
		return sb.toString();
	}
    
    /**
     * 
        * @Title: formatTime2
        * @Description: 将毫秒 换算成(天 时 分 秒 毫秒)
        * @param @param ms
        * @param @return    参数
        * @return String    返回类型
        * @throws
     */
    public static String formatTime2(Long ms) {
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;
 
        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
        Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;
        
        StringBuffer sb = new StringBuffer();
        if(day > 0) {
            sb.append(day+"天");
        }
        if(hour > 0) {
            sb.append(hour+"小时");
        }
        if(minute > 0) {
            sb.append(minute+"分");
        }
        if(second > 0) {
            sb.append(second+"秒");
        }
        if(milliSecond > 0) {
            sb.append(milliSecond+"毫秒");
        }
        return sb.toString();
    }
   
}
