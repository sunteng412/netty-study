package com.netty.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalUnit;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Mr.Xihua
 * @email xihua.zh@raycloud.com
 * @date 2021-07-27 14:00
 * @description
 */
public class LocalDateTimeUtils {

    /**
     * 日期格式  年-月-日 时:分:秒
     */
    public final static String DEFAULT_LOCAL_DATE_FORMAT_YEAR_MONTH = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式  年-月-日 时
     */
    public final static String LOCAL_DATE_FORMAT_YEAR_MONTH_DAY_HOURS = "yyyy-MM-dd HH:mm";

    /**
     * 日期格式  年-月
     */
    public final static String LOCAL_DATE_FORMAT_YEAR_MONTH = "yyyy-MM";

    /**
     * 日期格式  年-月-日
     */
    public final static String LOCAL_DATE_FORMAT_YEAR_MONTH_DAY = "yyyy-MM-dd";

    /**
     * 日期格式  年.月.日
     */
    public final static String LOCAL_DATE_FORMAT_YEAR_MONTH_DAY_CN = "yyyy.MM.dd";

    /**
     * 日期格式-中文  年-月
     */
    public final static String LOCAL_DATE_FORMAT_YEAR_MONTH_CN = "yyyy年MM月";

    /**
     * 日期格式化对象  年-月-日 时:分:秒
     */
    public final static DateTimeFormatter DEFAULT_FORMAT = DateTimeFormatter.ofPattern(DEFAULT_LOCAL_DATE_FORMAT_YEAR_MONTH);

    /**
     * 默认过期时间  2200-01-01 00:00:00
     */
    public final static Long DEFAULT_EXPIRE_DATE = 7258089600000L;

    /**
     * 一天的毫秒数
     * */
    public final static Long DAY_BY_MILL = 86400000L;

    //public final static Long ONE_DAY_TIMESTAMP = 24 * 60 * 60 * 1000L;

    LocalDateTimeUtils() {
    }

    /******
     * Date转换为LocalDateTime
     * @param date 时间
     * @return LocalDateTime
     *****/
    public static LocalDateTime convertDateToLDT(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }


    /******
     * Date转换为格式化成String
     * @param date 时间
     * @param dateTimeFormatter 时间格式
     * @return LocalDateTime
     *****/
    public static String convertDateToString(DateTimeFormatter dateTimeFormatter, Date date) {
        return dateTimeFormatter.format(LocalDateTimeUtils.convertDateToLDT(date));
    }

    /******
     * String转换为LocalDateTime
     * @param date 时间
     * @param format 默认
     * @return LocalDateTime
     *****/
    public static LocalDateTime convertStringToLDT(String date, String format) {
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(format));
    }

    /******
     * String转换为LocalDateTime
     * @param date 时间
     * @return LocalDateTime
     *****/
    public static LocalDateTime defaultConvertStringToLDT(String date) {
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(DEFAULT_LOCAL_DATE_FORMAT_YEAR_MONTH));
    }

    /******
     * String转换为LocalDateTime
     * @param localDateTime 时间
     * @return LocalDateTime
     *****/
    public static String defaultConvertLocalDateTimeToString(LocalDateTime localDateTime) {
        return DEFAULT_FORMAT.format(localDateTime);
    }

    /******
     * String转换为LocalDateTime
     * @param localDateTime 时间
     * @return LocalDateTime
     *****/
    public static String convertLocalDateTimeToString(LocalDateTime localDateTime,String fmt) {
        return DateTimeFormatter.ofPattern(fmt).format(localDateTime);
    }

    /******
     * String转换为Date(yyyy-MM-dd HH:mm:ss)
     * @param date 时间
     * @return LocalDateTime
     *****/
    public static Date defaultStringToDate(String date) {
        return convertLDTToDate(defaultConvertStringToLDT(date));
    }


    /******
     * LocalDateTime转换为Date
     * @param time 时间
     * @return Date
     *****/
    public static Date convertLDTToDate(LocalDateTime time) {
        return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
    }

    /******
     * 获取指定日期的毫秒
     * @param time 时间
     * @return Long
     *****/
    public static Long getMilliByTime(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /******
     * 毫秒值转时间
     * @param mill 毫秒值
     * @return LocalDateTime
     *****/
    public static LocalDateTime getTimeByMill(Long mill) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(mill), ZoneId.systemDefault());
    }

    /******
     *获取指定日期的秒
     * @param time
     * @return Long
     *****/
    public static Long getSecondsByTime(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
    }


    /******
     * 获取指定时间的指定格式
     * @param time 时间
     * @param pattern 时间格式,比如yyyy年MM月dd日 HH:mm:ss"
     * @return string
     *****/
    public static String formatTime(LocalDateTime time, String pattern) {
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }


    /******
     * 格式化当前时间
     *
     * @param pattern 时间格式,比如yyyy年MM月dd日 HH:mm:ss"
     * @return string
     *****/
    public static String formatNow(String pattern) {
        return formatTime(LocalDateTime.now(), pattern);
    }


    /******
     * 日期加上一个数,根据field不同加不同值,field为ChronoUnit.*
     * @param time 时间
     * @param field 时间单位
     * @param number 减去的时间数
     * @return LocalDateTime
     *****/
    public static LocalDateTime plus(LocalDateTime time, long number, TemporalUnit field) {
        return time.plus(number, field);
    }


    /******
     * 日期减去一个数,根据field不同减不同值,field参数为ChronoUnit.*
     * @param time 时间
     * @param field 时间单位
     * @param number 减去的时间数
     * @return LocalDateTime
     *****/
    public static LocalDateTime minu(LocalDateTime time, long number, TemporalUnit field) {
        return time.minus(number, field);
    }

    /**
     * 获取两个日期的差--天
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return long
     */
    public static long betweenTwoTimeByDay(LocalDateTime startTime, LocalDateTime endTime) {
        Duration duration = Duration.between(startTime,endTime );
        return duration.toDays();
    }

    /**
     * 获取两个日期的差--天
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return long
     */
    public static long betweenTwoTimeByDay(Date startTime, Date endTime) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(startTime);
        cal2.setTime(endTime);
        if(cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)){
            //同一年
            return cal1.get(Calendar.DAY_OF_YEAR) - cal2.get(Calendar.DAY_OF_YEAR);
        }else {
            return  (startTime.getTime() - endTime.getTime()) / DAY_BY_MILL;
        }
    }



    /**
     * 获取两个日期的差  field参数为ChronoUnit.*
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param field     单位(年月日时分秒)
     * @return long
     */
    public static long betweenTwoTime(LocalDateTime startTime, LocalDateTime endTime, ChronoUnit field) {
        if(field == ChronoUnit.MILLIS){
            return LocalDateTimeUtils.getMilliByTime(endTime) - LocalDateTimeUtils.getMilliByTime(startTime);
        }

        if(field == ChronoUnit.SECONDS){
            return LocalDateTimeUtils.getSecondsByTime(endTime) - LocalDateTimeUtils.getSecondsByTime(startTime);
        }

        Period period = Period.between(LocalDate.from(startTime), LocalDate.from(endTime));
        if (field == ChronoUnit.YEARS) {
            return period.getYears();
        }

        if (field == ChronoUnit.MONTHS) {
            Long months = new Long(period.getMonths());
            return period.getYears() * 12 + months;
        }

        if (field == ChronoUnit.DAYS) {
            Long day = new Long(period.getDays());
            return (period.getYears() * 12 + new Long(period.getMonths())) * 30 + day;
        }

        if (field == ChronoUnit.WEEKS) {
            Long day = new Long(period.getDays());
            return ((period.getYears() * 12 + new Long(period.getMonths())) * 30 + day)/7;
        }


        return field.between(startTime, endTime);
    }


    /******
     * 获取一天的开始时间，2017,7,22 00:00
     * @param time
     * @return LocalDateTime
     *****/
    public static LocalDateTime getDayStart(LocalDateTime time) {
        return time.withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }

    /******
     * 获取某个月的开始时间，2017,7,22 00:00
     * @param time
     * @return LocalDateTime
     *****/
    public static LocalDateTime getMonthStart(LocalDateTime time) {
        return time.withDayOfMonth(1)
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }


    /**
     * 获取一天的开始时间
     * @param date 某一天时间
     * @return 某一天的开始时间,比如2005-01-01 00:00:00.000
     */
    public static Date getDayStartByDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取一天的开始时间
     * @param date 某一天时间
     * @return 某一天的开始时间,比如2005-01-01 00:00:00.000
     */
    public static Date getDayEndByDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999999999);
        return calendar.getTime();
    }


    /******
     * //获取一天的结束时间，2017,7,22 23:59:59.999999999
     * @param time 时间
     * @return LocalDateTime
     *****/
    public static LocalDateTime getDayEnd(LocalDateTime time) {
        return time.withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withNano(0);
    }

    /******
     * //获取该月最后一天的结束时间，2017,7,31 23:59:59.999999999
     * @param time 时间
     * @return LocalDateTime
     *****/
    public static LocalDateTime getMonthEnd(LocalDateTime time) {
        return getDayEnd(time.with(TemporalAdjusters.lastDayOfMonth()));
    }

    public static void main(String[] args) {
        LocalDateTime monthEnd = getMonthEnd(LocalDateTime.now());
        System.out.println(monthEnd);

        Date date = LocalDateTimeUtils.convertLDTToDate(monthEnd);
        System.out.println(LocalDateTimeUtils
                .convertDateToString(DateTimeFormatter.ofPattern(LocalDateTimeUtils.DEFAULT_LOCAL_DATE_FORMAT_YEAR_MONTH),
                        date));

        System.out.println(LocalDateTimeUtils.convertLDTToDate(LocalDateTimeUtils.getMonthEnd(monthEnd)));
    }

    /**
     * 获取一段时间后的日期
     *
     * @param timeUnit 时间单位： 0天 1周 2月 3年  默认天
     * @param value    时间值
     * @return
     */
    public static Date expireDate(Integer timeUnit, Integer value) {
        if(null == timeUnit || timeUnit < 0){
            timeUnit = 0;
        }
        if(null == value){
            value = 0;
        }
        //如果时间值小于等于0，代表永不过期
        if (value <= 0) {
            return new Date(DEFAULT_EXPIRE_DATE);
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireDate;
        switch (timeUnit) {
            case 1:
                expireDate = now.plus(value, ChronoUnit.WEEKS);
                break;
            case 2:
                expireDate = now.plus(value, ChronoUnit.MONTHS);
                break;
            case 3:
                expireDate = now.plus(value, ChronoUnit.YEARS);
                break;
            default:
                expireDate = now.plus(value, ChronoUnit.DAYS);
                break;
        }
        return convertLDTToDate(expireDate);
    }
}
