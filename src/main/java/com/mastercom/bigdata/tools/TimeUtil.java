package com.mastercom.bigdata.tools;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Kwong on 2017/9/22.
 */
public final class TimeUtil {

    public static int dayNoInOneWeek()
    {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(System.currentTimeMillis()));
        int dayOfWeek = c.get(c.DAY_OF_WEEK);
        return dayOfWeek;
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

        }
        return "错误";
    }
}
