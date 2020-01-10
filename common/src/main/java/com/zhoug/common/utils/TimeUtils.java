package com.zhoug.common.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 描述：时间工具
 * zhougan
 * 2019/9/6
 **/
public class TimeUtils {
    private static final String TAG = "TimeUtils";

    public static final String PATTERN = "yyyy-MM-dd HH:mm:ss";


    /**
     *根据pattern获取SimpleDateFormat对象
     * @param pattern 默认:yyyy-MM-dd HH:mm:ss
     * @return SimpleDateFormat
     */
    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat getSimpleDateFormat(String pattern){
        if (pattern == null) {
            pattern = PATTERN;
        }
        return new SimpleDateFormat(pattern);
    }

    /**
     * 获取当前时间
     *
     * @param pattern 默认 "yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static String getCurrentTime(String pattern) {
       return getSimpleDateFormat(pattern).format(new Date(System.currentTimeMillis()));
    }

    /**
     * 获取当前时间
     * 格式默认 "yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static String getCurrentTime() {
        return getCurrentTime(PATTERN);
    }

    /**
     * Date to String
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date,String pattern){
        return getSimpleDateFormat(pattern).format(date);
    }


    /**
     * long to String
     * @param times
     * @param pattern
     * @return
     */
    public static String format(long times,String pattern){
        return getSimpleDateFormat(pattern).format(times);
    }

    /**
     * String to date
     * @param date
     * @param pattern
     * @return
     */
    public static Date parse(String date,String pattern){
        try {
            return getSimpleDateFormat(pattern).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     *获取到从1900年到给定的年之间的年的集合
     * @param endYear >1900
     * @return
     */
    public static ArrayList<String> getYearList(String endYear){
        int endYearInt = Integer.parseInt(endYear);
        if(endYearInt<=1900){
            Log.e(TAG, "endYear must bigger 1900");
            return null;
        }

        ArrayList<String> years=new ArrayList<>();
        for(int i=1900;i<=endYearInt;i++){
            years.add(i+"");
        }
        return years;
    }

    /**
     * 获取12个月的集合{01，02....10,11,12}
     * @return
     */
    public static  ArrayList<String> getMonthList(){
        ArrayList<String> months=new ArrayList<>();
        for(int i=1;i<=12;i++){
            if(i<10){
                months.add("0"+i);
            }else{
                months.add(i+"");
            }
        }
        return months;
    }

    /**
     * 是否是闰年
     * @param year
     * @return
     */
    public static boolean isLeapYear(String year){
        return  Integer.parseInt(year)%4==0;
    }

    /**
     * 根据给定的年月获取当月的所有天的集合,数据传入第三个参数days中 ,如果days==null，则创建ArrayList
     * @param year 年
     * @param month  月
     * @param days  数据传入ArrayList
     * @return days
     */
    public static ArrayList<String> getDays(String year,String month,ArrayList<String> days){
        if(days==null){
            days=new ArrayList<>();
        }else{
            days.clear();
        }
        int yearInt=Integer.parseInt(year);
        int monthInt=Integer.parseInt(month);
        int maxDay=0;//最大天数
        if(monthInt==1||monthInt==3||monthInt==5||monthInt==7||monthInt==8||monthInt==10||monthInt==12){
            maxDay=31;
        }else if(monthInt==2){
            maxDay=yearInt%4==0 ? 29 : 28;
        }else{
            maxDay=30;
        }

        for(int i=1;i<=maxDay;i++){
            if(i<10){
                days.add("0"+i);
            }else{
                days.add(i+"");
            }
        }
        return days;
    }

    /**
     * 根据给定的年月获取当月的所有天的集合
     * @param year
     * @param month
     * @return
     */
    public static ArrayList<String> getDays(String year,String month){
        return getDays(year,month,null);
    }

    /**
     *把日期中的separator换成" ",并且最多保留到秒
     * @param date eg:2019-08-25T12:36:55
     * @param separator 年月日和时间之间的分隔符 默认"T"
     * @return dateString
     */
    public static String formatDateT(String date,String separator){
        if(date==null){
            return null;
        }
        if(separator==null){
            separator="T";
        }

        if(date.contains(separator)){
            date = date.replace(separator, " ");
        }
        if(date.length()>19){
            date=date.substring(0,19 );
        }

        return date;
    }

    /**
     *把日期中的T换成" ",并且最多保留到秒
     * @param date eg:2019-08-25T12:36:55
     * @return dateString
     */
    public static String formatDateT(String date){
        return formatDateT(date,"T");
    }

    /**
     *获取日期中的年月日
     * @param date eg:2019-08-25T12:36:55
     * @return
     */
    public static String formatDateYMD(String date){
        if (date == null) {
            return null;
        }
        if (date.length() >= 10) {
            return date.substring(0, 10);
        } else {
            return date;
        }
    }

    /**
     *获取日期中的时分秒
     * @param date eg:2019-08-25T12:36:55
     * @return
     */
    public static String formatDateHMS(String date){
        if (date == null) {
            return null;
        }
        if (date.length() >10) {
            //T12:36:55
            String time = date.substring(10);
            //T
            String first = time.substring(0, 1);
            try {
                //判断是否是数字
                int i=Integer.parseInt(first);
            } catch (NumberFormatException e) {
//                e.printStackTrace();
                //第一个不是数字
                time= time.replace(first, "").trim();
            }
            //12:36:55
            if(time.length()>8){
                return time.substring(0,8 );
            }else{
                return time;
            }
        }
        return null;
    }


}
