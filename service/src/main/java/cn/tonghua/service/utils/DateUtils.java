package cn.tonghua.service.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author xusonghui
 */
public class DateUtils {
    /**
     * 时间格式化
     * @param strDate
     * @param format
     * @return
     */
    public static Date strToDate(String strDate,String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date(0);
    }

    /**
     * 时间格式化
     * @param strDate
     * 格式：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static Date strToDate(String strDate){
        SimpleDateFormat sdf = null;
        try {
            sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
            return sdf.parse(strDate.replace("Z", " UTC"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            return sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            sdf = new SimpleDateFormat("yyyy-MM-dd HH");
            return sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date(0);
    }

    /**
     * 时间格式化
     * @param date
     * @param format
     * @return
     */
    public static String dateToStr(Date date,String format){
        if(date == null){
            return "未知";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 时间格式化
     * @param date
     * 格式：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String dateToStr(Date date){
        if(date == null){
            return "未知";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}
