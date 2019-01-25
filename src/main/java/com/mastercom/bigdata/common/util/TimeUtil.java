package com.mastercom.bigdata.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Kwong on 2017/9/22.
 */
public final class TimeUtil {

    private TimeUtil(){}

    public static int dayNoInOneWeek()
    {
        calendar.setTime(new Date(System.currentTimeMillis()));
        return calendar.get(calendar.DAY_OF_WEEK);
    }

    public static int dayNoInOneMonth()
    {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(System.currentTimeMillis()));
         return c.get(5);
    }

    public static String returnWeekWord(int day)
    {
        switch (day)
        {
            case 1:
                return "星期日";
            case 2:
                return "星期一";
            case 3:
                return "星期二";
            case 4:
                return "星期三";
            case 5:
                return "星期四";
            case 6:
                return "星期五";
            case 7:
                return "星期六";
            default:
                return "错误";
        }
    }

    /**
     * 在输入日期基础上增加多少个小时
     * @param date 日期
     * @param hours 小时数，当为负数时即减操作
     * @return 结果
     */
    public static Date addHours(Date date, int hours){

        calendar.setTime(date);

        calendar.add(Calendar.HOUR, hours);

        return calendar.getTime();
    }

    /**
     * 在输入日期基础上增加多少天
     * @param date 日期
     * @param days 小时数，当为负数时即减操作
     * @return 结果
     */
    public static Date addDays(Date date, int days){

        calendar.setTime(date);

        calendar.add(Calendar.DAY_OF_MONTH, days);

        return calendar.getTime();
    }

    /**
     * 尽最大努力 按要求格式化
     * @param dateStr
     * @param dateFormat
     * @return
     */
    public static Date parse(String dateStr, String dateFormat){
        if(dateFormat == null || dateFormat.length() == 0){
            return null;
        }
        try{
            //去掉一些影响匹配的字符
            df.applyPattern(dateFormat.replaceAll("[^ ymdhsYMDHS/\\-:\\.]+","_"));
            return df.parse(dateStr.replaceAll("[^ 0-9/\\-:\\.]+","_"));
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 尽最大努力 按要求格式化
     * @param date
     * @param dateFormat
     * @return
     */
    public static String format(Date date, String dateFormat){
        if(dateFormat == null || dateFormat.length() == 0){
            return null;
        }

        try{
            //去掉一些影响匹配的字符
//            df.applyPattern(dateFormat.replaceAll("[^ ymdhsYMDHS/\\-:\\.]+","_"));
            df.applyPattern(dateFormat);
            return df.format(date);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 尽最大努力 按要求格式化
     * @param date
     * @param dateFormat
     * @return
     */
    public static String format(long date, String dateFormat){
        if(dateFormat == null || dateFormat.length() == 0){
            return null;
        }

        try{
            //去掉一些影响匹配的字符
//            df.applyPattern(dateFormat.replaceAll("[^ ymdhsYMDHS/\\-:\\.]+","_"));
            df.applyPattern(dateFormat);
            return df.format(date);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取星期几
     * @param date 日期
     * @return 星期几
     */
    public static int dayOfWeek(Date date){
        calendar.setTime(date);

        return calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }

    public static int dayOfWeek(long millis) {
        calendar.setTimeInMillis(millis);

        return calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }

    /**
     * 单例日历，仅供计算用
     * PS. 线程不安全！线程不安全！线程不安全！
     */
    private static final Calendar calendar = Calendar.getInstance();

    /**
     * 单例日期格式化，仅供计算用
     */
    private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
}
