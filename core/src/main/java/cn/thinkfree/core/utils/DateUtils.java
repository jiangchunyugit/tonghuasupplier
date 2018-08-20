package cn.thinkfree.core.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

public class DateUtils {


    /**
     * 获取本月第一天
     * @return
     */
    public static LocalDate firstDayOfMonth(){
        return LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * \获取本月最后一天
     * @return
     */
    public static LocalDate lastDayOfMonth(){
        return LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * 获取本周第一天
     * @return
     */
    public static LocalDate firstDayOfWeek(){
        return LocalDate.now().with(ChronoField.DAY_OF_WEEK,1);
    }

    /**
     * 获取本周最后一天
     * @return
     */
    public static LocalDate lastDayOfWeek(){
        return LocalDate.now().with(ChronoField.DAY_OF_WEEK,7);
    }


    /**
     * 日期类型转换
     * @param localDate
     * @return
     */
    public static Date LocalDate2Date(LocalDate localDate){
        return Date.from(localDate.atStartOfDay().atZone( ZoneId.systemDefault()).toInstant());
    }

    /**
     * 日期类型互转
     * @param date
     * @return
     */
    public static LocalDate Date2LocalDate(Date date){
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }




}
