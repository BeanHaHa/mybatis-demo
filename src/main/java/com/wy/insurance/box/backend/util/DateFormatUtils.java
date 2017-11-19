package com.wy.insurance.box.backend.util;

import org.apache.commons.lang.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 日期工具
 * @author daobin<wdb@winbaoxian.com>
 * Motify by daobin on 2016/03/29
 * @date 2015/9/8.
 */
public class DateFormatUtils {

    public static final String MMDDYYYY = "MM/dd/yyyy";
    public static final String YYYYMMDD = "yyyy/MM/dd";
    public static final String MM_DD_YYYY = "MM-dd-yyyy";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYYMMDD_HH_MM_SS = "yyyy/MM/dd HH:mm:ss";
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String HH_MM_SS = "HH:mm:ss";
    public static final String HH_MM = " HH:mm";
    public static final String MM_DD_CHINA = "MM月dd日";
    public static final String MM_DD_HH_MMCHINA = "MM月dd日 HH:mm";
    public static final String YYYYMMDD_NAT = "yyyyMMdd";

    /**
     * 用输入的模板参数，格式化输入的日期参数，并返回格式化后的字符串
     *
     * @param date    需要格式化的日期
     * @param pattern 格式化模板
     * @return 格式化后的日期字串
     */
    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * 将string对象转换成date对象
     *
     * @param date
     * @param pattern
     * @return
     */
    public static Date convertStringToDate(String date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    /**
     * 用输入的模板参数，格式化当前日期
     *
     * @param pattern
     * @return
     */
    public static String formatCurrentDate(String pattern) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * 在指定的日期增加或减少几天，并返回格式化后的日期
     *
     * @param date
     * @param days
     * @param pattern
     * @return
     */
    public static String addFormatDays(Date date, int days, String pattern) {
        Date newDate = DateUtils.addDays(date, days);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(newDate);
    }

    public static Date addDays(Date date, int days) {
        return DateUtils.addDays(date, days);
    }

    public static Date addYears(Date date, int years) {
        return DateUtils.addYears(date, years);
    }

    public static Date parseDate(String dateStr) {
        Date date = null;
        SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            date = sFormat.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;

    }

    public static int compare(Date firstDate, Date secondDate) {

        Calendar firstCalendar = null;
        /**使用给定的 Date 设置此 Calendar 的时间。**/
        if (firstDate != null) {
            firstCalendar = Calendar.getInstance();
            firstCalendar.setTime(firstDate);
        }

        Calendar secondCalendar = null;
        /**使用给定的 Date 设置此 Calendar 的时间。**/
        if (firstDate != null) {
            secondCalendar = Calendar.getInstance();
            secondCalendar.setTime(secondDate);
        }

        try {
            /**
             * 比较两个 Calendar 对象表示的时间值（从历元至现在的毫秒偏移量）。
             * 如果参数表示的时间等于此 Calendar 表示的时间，则返回 0 值；
             * 如果此 Calendar 的时间在参数表示的时间之前，则返回小于 0 的值；
             * 如果此 Calendar 的时间在参数表示的时间之后，则返回大于 0 的值
             * **/
            return firstCalendar.compareTo(secondCalendar);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException(e);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 返回上月
     * @param date
     * @return
     */
    public static Date getLastMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, -1);
        return cal.getTime();
    }
    /**
     * 比较两个时间是否在同一个月
     * @param startDate
     * @param endDate
     * @return
     */
    public static boolean isInSameMonth(Date startDate, Date endDate) {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);
        return startCal.get(Calendar.MONTH) == endCal.get(Calendar.MONTH);
    }

    /**
     * 返回该日期所在月的第一天
     * @param date
     * @return
     */
    public static Date getCurrentMonthFirstDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH,   1);
        return cal.getTime();
    }
    /**
     * 返回该日期所在月的最后一天日期
     * @param date
     * @return
     */
    public static Date getCurrentMonthLastDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    /**
     * 返回一段时间内的日期列表 [startDate, endDate)
     * @param startDate
     * @param endDate
     * @param pattern
     * @return
     */
    public static List<String> getDateIntervalDays(Date startDate, Date endDate,String pattern) {
        boolean boo = false;
        List<String> list = new ArrayList<String>();
        String startDay = formatDate(startDate, pattern);
        String endDay = formatDate(endDate, pattern);
        list.add(startDay);
        Date nextDay = startDate;
        while (!boo) {
            nextDay = addDays(nextDay, 1);
            String day = formatDate(nextDay, pattern);
            if(endDay.equals(day)) {
                boo = true;
            } else {
                list.add(day);
            }
        }
        return  list;
    }

    public static void main(String args[]) {
        Date now = convertStringToDate("2016-10-29", YYYY_MM_DD);
        Date end = convertStringToDate("2016-11-05", YYYY_MM_DD);
        System.out.println(getDateIntervalDays(now, end, YYYY_MM_DD));
    }
}
