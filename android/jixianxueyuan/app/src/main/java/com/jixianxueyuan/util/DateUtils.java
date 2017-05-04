package com.jixianxueyuan.util;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by pengchao on 2017/04/29.
 */
public class DateUtils {
    public static final String FORMAT_ALL="yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_ALL_EXCEPT_SS="yyyy-MM-dd HH:mm";
    public static final String FORMAT_DATE="yyyy-MM-dd";
    public static final String FORMAT_TIME_ALL="HH:mm:ss";
    public static final String FORMAT_TIME_EXCEPT_SS="HH:mm";
    public static final String FORMAT_TIME_YYYY_MM = "yyyyMM";
    public static final String FORMAT_IN_ENTRANCE_TIEM = "yyyy.MM.dd HH:mm";
    public static final String FORMAT_ALL_MS="yyyy-MM-dd HH:mm:ss.SSS";


    public static String formatDate(Date date, String format)  {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static Date parse(String strDate, String format)   {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(strDate);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new Date();
    }

    public static String format(long date, String format){
        return formatDate(new Date(date), format);
    }

    public static Date getLastMonth(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, -1);
        return cal.getTime();
    }
}
