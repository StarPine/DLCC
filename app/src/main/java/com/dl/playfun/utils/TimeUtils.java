package com.dl.playfun.utils;

import android.annotation.SuppressLint;

import com.blankj.utilcode.util.StringUtils;
import com.dl.playfun.app.AppContext;
import com.dl.playfun.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * @author wulei
 */
public class TimeUtils {

    public static final int MSEC = 1;
    public static final int SEC = 1000;
    public static final int MIN = 60000;
    public static final int HOUR = 3600000;
    public static final int DAY = 86400000;
    public static final int WEEK = 604800000;
    public static final long MONTH = 2592000000L;
    public static final long YEAR = 31536000000L;
    private static final int month = Calendar.getInstance().get(Calendar.MONTH) + 1;

    public static String getFriendlyTimeSpan(String time) {
        if (time != null) {
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                long millis = format.parse(time).getTime();
                long now = System.currentTimeMillis();
                long span = now - millis;
                if (span < 1000) {
                    return StringUtils.getString(R.string.playfun_just);
                } else if (span < MIN) {
                    return String.format(Locale.getDefault(), StringUtils.getString(R.string.playfun_seconds_ago), span / SEC);
                } else if (span < HOUR) {
                    return String.format(Locale.getDefault(), StringUtils.getString(R.string.playfun_minutes_ago), span / MIN);
                } else if (span < DAY) {
                    return String.format(Locale.getDefault(), StringUtils.getString(R.string.playfun_hours_ago), span / HOUR);
                } else if (span < WEEK) {
                    return String.format(Locale.getDefault(), StringUtils.getString(R.string.playfun_daily_ago), span / DAY);
                } else if (span < MONTH) {
                    return String.format(Locale.getDefault(), StringUtils.getString(R.string.playfun_weeks_ago), span / WEEK);
                } else if (span < YEAR) {
                    return String.format(Locale.getDefault(), StringUtils.getString(R.string.playfun_month_ago), span / MONTH);
                } else {
                    return String.format(Locale.getDefault(), StringUtils.getString(R.string.playfun_year_ago), span / YEAR);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return StringUtils.getString(R.string.playfun_unknown);
    }

    /**
     * 获得当前日期 yyyy-MM-dd HH:mm:ss
     *
     * @return 2019-08-27 14:12:40
     */
    public static String getCurrentTime() {
        // 小写的hh取得12小时，大写的HH取的是24小时
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return df.format(date);
    }

    /**
     * 获取系统当前时间戳
     *
     * @return 1566889186583
     */
    public static String getSystemTime() {
        String current = String.valueOf(System.currentTimeMillis());
        return current;
    }

    /**
     * 获取当前日期 yy-MM-dd
     *
     * @return 2019-08-27
     */
    public static String getDateByString() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    /**
     * 得到两个时间差  格式yyyy-MM-dd HH:mm:ss
     *
     * @param start 2019-06-27 14:12:40
     * @param end   2019-08-27 14:12:40
     * @return 5270400000
     */
    public static long dateSubtraction(String start, String end) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date1 = df.parse(start);
            Date date2 = df.parse(end);
            return date2.getTime() - date1.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 得到两个时间差
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return
     */
    public static long dateTogether(Date start, Date end) {
        return end.getTime() - start.getTime();
    }

    /**
     * 转化long值的日期为yyyy-MM-dd  HH:mm:ss.SSS格式的日期
     *
     * @param millSec 日期long值  5270400000
     * @return 日期，以yyyy-MM-dd  HH:mm:ss.SSS格式输出 1970-03-03  08:00:00.000
     */
    public static String transferLongToDate(String millSec) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss.SSS");
        Date date = new Date(Long.parseLong(millSec));
        return sdf.format(date);
    }

    /**
     * 获得当前日期 yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getOkDate(String date) {
        try {
            if (StringUtils.isEmpty(date)) {
                return null;
            }
            Date date1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH).parse(date);
            //格式化
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.format(date1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前日期是一个星期的第几天
     *
     * @return 2
     */
    public static int getDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return cal.get(Calendar.DAY_OF_WEEK) - 1;
    }

    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     *
     * @param nowTime     当前时间
     * @param dateSection 时间区间   2018-01-08,2019-09-09
     * @return
     * @author jqlin
     */
    public static boolean isEffectiveDate(Date nowTime, String dateSection) {
        try {
            String[] times = dateSection.split(",");
            String format = "yyyy-MM-dd";
            Date startTime = new SimpleDateFormat(format).parse(times[0]);
            Date endTime = new SimpleDateFormat(format).parse(times[1]);
            if (nowTime.getTime() == startTime.getTime()
                    || nowTime.getTime() == endTime.getTime()) {
                return true;
            }
            Calendar date = Calendar.getInstance();
            date.setTime(nowTime);

            Calendar begin = Calendar.getInstance();
            begin.setTime(startTime);

            Calendar end = Calendar.getInstance();
            end.setTime(endTime);

            if (isSameDay(date, begin) || isSameDay(date, end)) {
                return true;
            }
            return date.after(begin) && date.before(end);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 != null && cal2 != null) {
            return cal1.get(0) == cal2.get(0) && cal1.get(1) == cal2.get(1) && cal1.get(6) == cal2.get(6);
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }
    }

    public static long getTimeByDate(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(time);
            //日期转时间戳（毫秒）
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取当前小时 ：2019-08-23 17
     *
     * @return 2019-08-27 17
     */
    public static String getCurrentHour() {
        GregorianCalendar calendar = new GregorianCalendar();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour < 10) {
            return TimeUtils.getCurrentTime() + " 0" + hour;
        }
        return TimeUtils.getDateByString() + " " + hour;
    }

    /**
     * 获取当前时间一个小时前
     *
     * @return 2019-08-27 16
     */
    public static String getCurrentHourBefore() {
        GregorianCalendar calendar = new GregorianCalendar();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour > 0) {
            hour = calendar.get(Calendar.HOUR_OF_DAY) - 1;
            if (hour < 10) {
                return TimeUtils.getDateByString() + " 0" + hour;
            }
            return TimeUtils.getDateByString() + " " + hour;
        }
        //获取当前日期前一天
        return TimeUtils.getBeforeDay() + " " + 23;
    }

    /**
     * 获取当前日期前一天
     *
     * @return 2019-08-26
     */
    public static String getBeforeDay() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date = calendar.getTime();
        return getSimpleDateFormat().format(date);
    }

    /**
     * 获取当前日期
     *
     * @return 2019-08-26
     */
    public static String getCurrentDay() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 0);
        date = calendar.getTime();
        return getSimpleDateFormat().format(date);
    }

    public static SimpleDateFormat getSimpleDateFormat() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + StringUtils.getString(R.string.playfun_year) + "M" + AppContext.instance().getResources().getString(R.string.playfun_month) + "d" + AppContext.instance().getResources().getString(R.string.playfun_daily));
        return sdf;
    }

    /**
     * 获取最近七天
     *
     * @return 2019-08-20
     */
    public static String getServen() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar c = Calendar.getInstance();

        c.add(Calendar.DATE, -7);

        Date monday = c.getTime();

        String preMonday = sdf.format(monday);

        return preMonday;
    }

    /**
     * 获取最近一个月
     *
     * @return 2019-07-27
     */
    public static String getOneMonth() {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + StringUtils.getString(R.string.year) + "M" + AppContext.instance().getResources().getString(R.string.month) + "d" + AppContext.instance().getResources().getString(R.string.daily));

        Calendar c = Calendar.getInstance();

        c.add(Calendar.MONTH, -1);

        Date monday = c.getTime();

        String preMonday = getSimpleDateFormat().format(monday);

        return preMonday;
    }

    /**
     * 获取最近三个月
     *
     * @return 2019-05-27
     */
    public static String getThreeMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar c = Calendar.getInstance();

        c.add(Calendar.MONTH, -3);

        Date monday = c.getTime();

        String preMonday = sdf.format(monday);

        return preMonday;
    }

    /**
     * 获取最近一年
     *
     * @return 2018-08-27
     */
    public static String getOneYear() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, -1);
        Date start = c.getTime();
        String startDay = sdf.format(start);
        return startDay;
    }

    /**
     * 获取今年月份数据
     * 说明 有的需求前端需要根据月份查询每月数据，此时后台给前端返回今年共有多少月份
     *
     * @return [1, 2, 3, 4, 5, 6, 7, 8]
     */
    public static List getMonthList() {
        List list = new ArrayList();
        for (int i = 1; i <= month; i++) {
            list.add(i);
        }
        return list;
    }

    /**
     * 返回当前年度季度list
     * 本年度截止目前共三个季度，然后根据1,2,3分别查询相关起止时间
     *
     * @return [1, 2, 3]
     */
    public static List getQuartList() {
        int quart = month / 3 + 1;
        List list = new ArrayList();
        for (int i = 1; i <= quart; i++) {
            list.add(i);
        }
        return list;
    }

    public static void main(String[] args) {
        System.out.println(TimeUtils.getQuartList());
    }

    /**
     * 返回当前年度季度list
     * 本年度截止目前共三个季度，然后根据1,2,3分别查询相关起止时间
     *
     * @return [1, 2, 3]
     */
    public static Long getLongTimr(String timeStr, SimpleDateFormat simpleDateFormat) {
//        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String time="2018-09-29 16:39:00";
        Date date = null;
        try {
            date = simpleDateFormat.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //日期转时间戳（毫秒）
        long time = date.getTime();
        return time;
//        System.out.print("Format To times:"+time);
    }

    /**
     * 将毫秒转化成固定格式的时间
     * 时间格式: yyyy-MM-dd-HHmmssSSS
     *
     * @param millisecond 时间毫秒
     * @return 时间格式: yyyy-MM-dd-HHmmssSSS
     */
    public static String getDateTimeFromMillisecond(Long millisecond) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HHmmssSSS");
        Date date = new Date(millisecond);
        return simpleDateFormat.format(date);
    }

    /**
     * 秒转时分秒
     *
     * @param second
     * @return
     */
    public static String getFormatTime(Integer second) {
        if (second != null) {
            String num0 = NumFormat(0);
            if (second < 60) {//秒
                return num0 + ":" + num0 + ":" + NumFormat(second);
            }
            if (second < 3600) {//分
                return num0 + ":" + NumFormat(second / 60) + ":" + NumFormat(second % 60);
            }
            if (second < 3600 * 24) {//时
                return NumFormat(second / 60 / 60) + ":" + NumFormat(second / 60 % 60) + ":" + NumFormat(second % 60);
            }
            if (second >= 3600 * 24) {//天
                return NumFormat(second / 60 / 60 / 24) + "天" + NumFormat(second / 60 / 60 % 24) + ":" + NumFormat(second / 60 % 60) + ":" + NumFormat(second % 60);
            }
        }
        return null;
    }

    /**
     * 格式化时间
     *
     * @param sec
     * @return
     */
    private static String NumFormat(int sec) {
        if (String.valueOf(sec).length() < 2) {
            return "0" + sec;
        } else {
            return String.valueOf(sec);
        }
    }


}
