package com.example.sharding.utils;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author: wujiankai
 * @description:
 * @date: 15:28 2019/10/8
 */
@Slf4j
public class TimeUtil {

    public static final String YEAR = "yyyy";
    public static final String YEAR_MONTH = "yyyy-MM";
    public static final String YEAR_MONTH_DAY = "yyyy-MM-dd";
    public static final String WHOLE_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String DAY_BEGIN = "yyyy-MM-dd 00:00:00";
    public static final String DAY_END = "yyyy-MM-dd 23:59:59";
    public static final String EIGHT_CLOCK = "20:00:00";
    public static final String YEAR_MONTH_DAY_L = "yyyy/MM/dd";
    public static final String YEAR_MONTH_DAY_NOT_LINE = "yyyyMMdd";
    public static final String YEAR_MONTH_CHINESE = "yyyy年MM月";
    public static final String YEAR_MONTH_DAY_CHINESE = "yyyy年MM月dd日";
    public static final String HOUR_MINUTE_SECOND = "HH:mm:ss";

    /**
     * 将时间戳格式转为 yyyy-MM-dd HH:mm:ss 的字符串
     * TODO: 切换成【timestampToString】配合【TimeConstant】使用
     *
     * @param timestamp 时间戳
     * @return 时间的字符串
     */
    public static String getDateTimeString(Timestamp timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(timestamp.getTime());
        return sdf.format(date);
    }

    /**
     * 时间戳转换成特定的时间类型字符串
     *
     * @param timestamp 时间戳
     * @param timeType  时间格式
     * @return string
     */
    public static String timestampToString(Timestamp timestamp, String timeType) {
        SimpleDateFormat sdf = new SimpleDateFormat(timeType);
        Date date = new Date(timestamp.getTime());
        return sdf.format(date);
    }

    /**
     * 时间戳转换成特定的时间类型字符串
     *
     * @param time 时间
     * @param timeType  时间格式
     * @return string
     */
    public static Date stringToDate(String time, String timeType) {
        SimpleDateFormat sdf = new SimpleDateFormat(timeType);
        Date date = new Date();
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            log.error("parse time error:" + e.getMessage());
        }
        return date;
    }

    /**
     * 根据指定的事件类型将字符串改为时间戳
     *
     * @param time     字符串时间
     * @param timeType 时间格式
     * @return timestamp
     */
    public static Timestamp transferTime(String time, String timeType) {
        SimpleDateFormat sdf = new SimpleDateFormat(timeType);
        Date date = new Date();
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            log.error("parse time error:" + e.getMessage());
        }
        return new Timestamp(date.getTime());
    }

    /**
     * @param date Date格式的日期
     * @return Timestamp
     */
    public static Timestamp transferTime(Date date) {
        if (date == null) {
            return null;
        }
        return new Timestamp(date.getTime());
    }

    /**
     * 将Timestamp 转为 Date
     *
     * @param timestamp 时间
     * @return Date类时间
     */
    public static List<Date> transferTimeToDate(Timestamp timestamp) {
        List<Date> dates = new ArrayList<>();
        SimpleDateFormat fm = new SimpleDateFormat(WHOLE_TIME);
        SimpleDateFormat sdf = new SimpleDateFormat(YEAR_MONTH_DAY);
        try {
            Date startTime = fm.parse(fm.format(sdf.parse(String.valueOf(timestamp)).getTime()));
            Date endTime = fm.parse(fm.format(sdf.parse(String.valueOf(timestamp)).getTime() + 86400000 - 1));
            dates.add(startTime);
            dates.add(endTime);
        } catch (ParseException p) {
            p.printStackTrace();
        }
        return dates;
    }

    /**
     * 获取某一天的起始时间或者结束时间
     *
     * @param timestamp 某一天的时间戳
     * @param timeType  时间格式  开始时间 【DAY_BEGIN】 结束时间 【DAY_END】
     * @return Date
     */
    public static Date getDayBeginTimeAndEndTime(Timestamp timestamp, String timeType) {
        Date time = new Date();
        Date date = new Date(timestamp.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat(WHOLE_TIME);
        SimpleDateFormat endFormat = new SimpleDateFormat(timeType);
        try {
            time = sdf.parse(endFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static Date getDayBeginTime(Timestamp timestamp) {
        Date beginTime = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(WHOLE_TIME);
        SimpleDateFormat startFormat = new SimpleDateFormat(DAY_BEGIN);
        try {
            beginTime = dateFormat.parse(startFormat.format(timestamp));
        } catch (Exception e) {
            log.error("获取某天开始时间失败");
            e.printStackTrace();
        }
        return beginTime;
    }

    public static Date getDayEndTime(Timestamp timestamp) {
        Date endTime = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(WHOLE_TIME);
        SimpleDateFormat endFormat = new SimpleDateFormat(DAY_END);
        try {
            endTime = dateFormat.parse(endFormat.format(timestamp));
        } catch (Exception e) {
            log.error("获取某天结束时间失败");
            e.printStackTrace();
        }
        return endTime;
    }

    /**
     * 根据传入时间获取月份开始时间
     *
     * @param date 传入时间
     * @return Date
     */
    public static Date getMonthBegin(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date currentMonth = calendar.getTime();
        return getBeginDate(currentMonth);
    }

    private static Date getBeginDate(Date currentMonth) {
        SimpleDateFormat format = new SimpleDateFormat(DAY_BEGIN);
        try {
            return format.parse(format.format(currentMonth));
        } catch (ParseException e) {
            log.error(e.getMessage());
            throw new ServiceException("出错时间");
        }
    }

    /**
     * 根据传入时间获取月份结束时间
     *
     * @param date 传入时间
     * @return Date
     */
    public static Date getMonthEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date currentMonth = calendar.getTime();
        return getEndDate(currentMonth);
    }


    private static Date getEndDate(Date currentMonth) {
        SimpleDateFormat format = new SimpleDateFormat(WHOLE_TIME);
        SimpleDateFormat endFormat = new SimpleDateFormat(DAY_END);
        try {
            return format.parse(endFormat.format(currentMonth));
        } catch (ParseException e) {
            log.error(e.getMessage());
            throw new ServiceException("时间错误");
        }
    }

    public static Timestamp getDaysLater(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);

        return new Timestamp(calendar.getTime().getTime());
    }

    /**
     * 判断是否在20点之前
     *
     * @return Boolean
     */
    public static Boolean beforeTwenty() {
        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat(YEAR_MONTH_DAY);
        DateFormat timeFormat = new SimpleDateFormat(WHOLE_TIME);
        String dateString = dateFormat.format(currentDate);
        try {
            String checkinTime = EIGHT_CLOCK;
            dateString = dateString + " " + checkinTime;
            Date dateAtTwenty = timeFormat.parse(dateString);
            return currentDate.before(dateAtTwenty);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Boolean beforeSystemClose(String systemCloseTime) {
        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat(YEAR_MONTH_DAY);
        DateFormat timeFormat = new SimpleDateFormat(WHOLE_TIME);
        String dateString = dateFormat.format(currentDate);
        try {
            dateString = dateString + " " + systemCloseTime;
            Date dateAtTwenty = timeFormat.parse(dateString);
            return currentDate.before(dateAtTwenty);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Date getMinuteLaterTime(Timestamp timestamp, Integer minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timestamp);
        calendar.add(Calendar.MINUTE, minute);

        return calendar.getTime();
    }

    private static Date getMinuteBeforeTime(Timestamp timestamp, Integer minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timestamp);
        calendar.add(Calendar.MINUTE, -minute);

        return calendar.getTime();
    }

    /**
     * 获取上一个月开始的时间
     */
    public static Date getPreviousMonthBegin() {
        return getMonthBeginTimeAndEndTime(null, 0, true);
    }

    /**
     * 获取本月开始时间
     */
    public static Date getCurrentMonthBegin() {
        return getMonthBeginTimeAndEndTime(null, 1, true);
    }

    /**
     * 根据传入时间获取月份开始时间
     */
    public static Date getMonthBeginByParameter(Date date) {
        return getMonthBeginTimeAndEndTime(date, 2, true);
    }

    /**
     * 获取上个月结束的时间
     */
    public static Date getPreviousMonthEnd() {
        return getMonthBeginTimeAndEndTime(null, 0, false);
    }

    /**
     * 获取当月结束时间
     */
    public static Date getCurrentMonthEnd() {
        return getMonthBeginTimeAndEndTime(null, 1, false);
    }

    /**
     * 某个月份的结束时间
     */
    public static Date getMonthEndByParameter(Date date) {
        return getMonthBeginTimeAndEndTime(date, 2, false);
    }

    /**
     * 获取一个月之前的时间
     *
     * @param date
     * @return
     */
    public static Date getOneMonthBefore(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);
        return calendar.getTime();
    }

    /**
     * 获取某个月的结束时间
     *
     * @param date 时间
     * @param code 0:上个月 1:本月 2:特定某个月
     * @param type true 开始时间  false  结束时间
     * @return date
     */
    public static Date getMonthBeginTimeAndEndTime(Date date, Integer code, Boolean type) {
        Calendar calendar = Calendar.getInstance();
        switch (code) {
            case 0:
                calendar.add(Calendar.MONTH, -1);
                break;
            case 2:
                calendar.setTime(date);
                break;
            default:
                break;
        }
        if (type) {
            return getBeginDate(calendar);
        } else {
            return getEndDate(calendar);
        }
    }

    public static void main(String[] args) {
        Date monthBeginTimeAndEndTime = TimeUtil.getMonthBeginTimeAndEndTime(new Date(), 0, true);
        System.out.println(monthBeginTimeAndEndTime);
    }
    /**
     * 将Timestamp 转为 Date
     *
     * @param timestamp 时间
     * @return Date类时间
     */
    public static Date transferToDate(Timestamp timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat(WHOLE_TIME);
        Date date = null;
        try {
            date = sdf.parse(sdf.format(sdf.parse(String.valueOf(timestamp)).getTime()));
        } catch (ParseException p) {
            p.printStackTrace();
        }
        return date;
    }

    static Timestamp transferToTimestamp(String time) {
        DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            try {
                date = format.parse(time);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        }
        return new Timestamp(date.getTime());
    }

    static boolean isValidDate(String str) {
        boolean convertSuccess = true;
        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        try {
            // 设置lenient为false.
            // 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            format.setLenient(false);
            format.parse(str);
        } catch (ParseException e) {
            convertSuccess = false;
        }
        return convertSuccess;
    }

    private static Date getBeginDate(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date val = calendar.getTime();
        return getBeginDate(val);
    }

    private static Date getEndDate(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date val1 = calendar.getTime();
        return getEndDate(val1);
    }

    /**
     * 获取当前月份前pageSize个月份
     *
     * @param pageSize 显示多少月份
     * @param pageNum  起始月
     * @return
     */
    public static List<String> getTimeRangeByMonth(Integer pageNum, Integer pageSize) {
        List<String> result = new ArrayList<>(pageSize);

        Calendar beginMonth = Calendar.getInstance();
        beginMonth.setTimeInMillis(1575129600000L);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1 - pageNum * pageSize);
        for (int i = 0; i < pageSize; i++) {
            calendar.add(Calendar.MONTH, -1);
            if (judgeMonth(calendar, beginMonth)) {
                result.add(calendar.get(Calendar.YEAR) + "-" + fillZero(calendar.get(Calendar.MONTH) + 1));
            } else {
                result.add(beginMonth.get(Calendar.YEAR) + "-" + fillZero(beginMonth.get(Calendar.MONTH) + 1));
                break;
            }
        }
        return result;
    }


    /**
     * 比较月份大小
     *
     * @param compare
     * @param compared
     * @return
     */
    private static boolean judgeMonth(Calendar compare, Calendar compared) {
        return compare.get(Calendar.YEAR) > compared.get(Calendar.YEAR)
                || (compare.get(Calendar.YEAR) == compared.get(Calendar.YEAR)
                && compare.get(Calendar.MONTH) > compared.get(Calendar.MONTH));
    }

    /**
     * 格式化月份
     */
    private static String fillZero(int i) {
        String month;
        if (i < Calendar.HOUR) {
            month = "0" + i;
        } else {
            month = String.valueOf(i);
        }
        return month;
    }

    /**
     * 获取时间集合
     *
     * @param timePattern
     * @return
     */
    public static List<String> getTimeStr(String timePattern) {
        List<String> result = new ArrayList<>();

        SimpleDateFormat format = new SimpleDateFormat(WHOLE_TIME);
        SimpleDateFormat timeFormat = new SimpleDateFormat(timePattern);
        String startTimeStr = "2019-11-01 00:00:00";
        String startYearStr = "2019-01-01 00:00:00";
        Date startDate = new Date();
        Date startYear = new Date();
        Date date = new Date();
        try {
            startDate = format.parse(startTimeStr);
            startYear = format.parse(startYearStr);
        } catch (Exception e) {
            log.error("时间转换失败");
            e.printStackTrace();
        }

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        if (YEAR.equals(timePattern)) {
            startCalendar.setTime(startYear);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        while (calendar.after(startCalendar)) {
            String time = timeFormat.format(calendar.getTime());
            result.add(time);
            switch (timePattern) {
                case YEAR_MONTH_DAY:
                    // yyyy-MM-dd
                    calendar.add(Calendar.DAY_OF_YEAR, -1);
                    break;
                case YEAR_MONTH:
                    // yyyy-MM
                    calendar.add(Calendar.MONTH, -1);
                    break;
                case YEAR:
                    // yyyy
                    calendar.add(Calendar.YEAR, -1);
                    break;
                default:
                    // 格式错误
                    return new ArrayList<>();
            }
        }

        return result;
    }

    /**
     * 获取时间集合
     *
     * @param timePattern
     * @return
     */
    public static List<String> getTimeStrAsc(String timePattern) {
        List<String> result = new ArrayList<>();

        SimpleDateFormat format = new SimpleDateFormat(WHOLE_TIME);
        SimpleDateFormat timeFormat = new SimpleDateFormat(timePattern);
        String startTimeStr = "2019-11-01 00:00:00";
        String startYearStr = "2019-01-01 00:00:00";
        Date startDate = new Date();
        Date startYear = new Date();
        Date date = new Date();
        try {
            startDate = format.parse(startTimeStr);
            startYear = format.parse(startYearStr);
        } catch (Exception e) {
            log.error("时间转换失败");
            e.printStackTrace();
        }

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        if (YEAR.equals(timePattern)) {
            startCalendar.setTime(startYear);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        while (calendar.after(startCalendar)) {
            String time = timeFormat.format(startCalendar.getTime());
            result.add(time);
            switch (timePattern) {
                case YEAR_MONTH_DAY:
                    // yyyy-MM-dd
                    startCalendar.add(Calendar.DAY_OF_YEAR, 1);
                    break;
                case YEAR_MONTH:
                    // yyyy-MM
                    startCalendar.add(Calendar.MONTH, 1);
                    break;
                case YEAR:
                    // yyyy
                    startCalendar.add(Calendar.YEAR, 1);
                    break;
                default:
                    // 格式错误
                    return new ArrayList<>();
            }
        }

        return result;
    }

    /**
     * 是否在系统关闭前一段时间前
     *
     * @param systemCloseTime
     * @param timeLimit
     * @return
     */
    public static Boolean beforeSystemCloseTimeTimeLimit(String systemCloseTime, Integer timeLimit) {
        boolean result = true;
        try {
            SimpleDateFormat format = new SimpleDateFormat(WHOLE_TIME);
            SimpleDateFormat currentDateFormat = new SimpleDateFormat(YEAR_MONTH_DAY);
            Date currentDate = new Date();
            String currentDateStr = currentDateFormat.format(currentDate);
            String systemCloseTimeStr = currentDateStr + " " + systemCloseTime;
            Date systemClose = format.parse(systemCloseTimeStr);
            Date limitTime = getMinuteBeforeTime(new Timestamp(systemClose.getTime()), timeLimit);
            result = currentDate.before(limitTime);
        } catch (Exception e) {
            log.error("时间转换失败");
        }
        return result;
    }

    /**
     * 判断时间是否在时间返回内
     * @param time
     * @param startTime
     * @param endTime
     * @return
     */
    public static Boolean judgeTimeBetween(Date time, Date startTime, Date endTime) {
        return time.after(startTime) && time.before(endTime);
    }


    /**
     * 判断时间是否在时间区间内
     * @param startTime
     * @param endTime
     * @return
     */
    public static Boolean judgeTimeBetween(String startTime, String endTime) {
        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat(YEAR_MONTH_DAY);
        DateFormat timeFormat = new SimpleDateFormat(WHOLE_TIME);
        String currentDateStr = dateFormat.format(currentDate);

        String startTimeStr =  currentDateStr + " " + startTime;
        String endTimeStr = currentDateStr + " " + endTime;
        try {
            Date dateStartTime = timeFormat.parse(startTimeStr);
            Date dateEndTime = timeFormat.parse(endTimeStr);

            return (currentDate.after(dateStartTime) && currentDate.before(dateEndTime));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    /**
	 * 
	    * @Title: getCurrentDateZero
	    * @Description: 获取当天开始时间字符串:2021-01-18 00:00:00
	    * @param @return    参数
	    * @return String    返回类型
	    * @throws
	 */
	public static String getCurrentDateZero() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date time = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return	sdf.format(time);
	 
	}
	
	
	
	/**
	 * 
	    * @Title: getCurrentDateFinal
	    * @Description: 获取当天结束时间字符串:2021-01-18 23:59:59
	    * @param @return    参数
	    * @return String    返回类型
	    * @throws
	 */
	public static String getCurrentDateFinal() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		Date time = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return	sdf.format(time);
	 
	}

    /**
     *
     * 两个时间间隔
     * @param startTime 开始时间
     * @param endTime 结束时间
     */
    public static String getDatePoor(Date startTime, Date endTime) {
        long d = 1000 * 24 * 60 * 60;
        long h = 1000 * 60 * 60;
        long m = 1000 * 60;
        long s = 1000;

        long diff = endTime.getTime() - startTime.getTime();

        long day = diff / d;
        long hour = diff % d / h;
        long min = diff % d % h / m;
        long sec = diff % d % h % m / s;


        // 格式化显示，不足10 前面补0
        String resTime = day + "-";

        if (hour <=  9) {
            resTime += "0" + hour + ":";
        } else {
            resTime += hour  + ":";
        }

        if (min <=  9) {
            resTime += "0" + min  + ":";
        } else {
            resTime +=  min  + ":";
        }

        if (sec <=  9) {
            resTime += "0" + sec;
        } else {
            resTime +=  sec;
        }

        return resTime;
    }



    /**
     * date转string
     *
     * @param date
     * @return
     */
    public static String dateToString(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat(WHOLE_TIME);
        String dateString = null;
        try {
            dateString = formatter.format(date);
        } catch (Exception e) {
            log.error("parse time error:" + e.getMessage());
            e.printStackTrace();
        }
        return dateString;
    }


}