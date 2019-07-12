package cn.tonghua.core.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public static Date localDate2Date(LocalDate localDate){
        return Date.from(localDate.atStartOfDay().atZone( ZoneId.systemDefault()).toInstant());
    }

    /**
     * 日期类型互转
     * @param date
     * @return
     */
    public static LocalDate date2LocalDate(Date date){
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * 获取周天数
     * @return
     */
    public static List<String> getWeekDays(){
        List<String> list = new ArrayList<>();
        for(int i=1;i<8;i++){
            LocalDate d = LocalDate.now().with(ChronoField.DAY_OF_WEEK, i);
            list.add(d.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
        }
        return list;
    }

    /**
     * 获取月天数
     * @return
     */
    public static List<String> getMonthDays(){
        List<String> list = new ArrayList<>();
        LocalDate date = LocalDate.now();
        for(int i=0;;i++){
            LocalDate td = date.with(TemporalAdjusters.firstDayOfMonth()).plusDays(i);
            list.add(td.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
            if(td.equals(date.with(TemporalAdjusters.lastDayOfMonth())))break;
        }
        return list;

    }

    public static String dateToDateTime(Date date) {
    	 String DATE_FORMAT = "yyyy-MM-dd";
        String datestr = null;
        if (date == null) {
            return datestr;
        }
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
        datestr = df.format(date);
        return datestr;
      }


}
