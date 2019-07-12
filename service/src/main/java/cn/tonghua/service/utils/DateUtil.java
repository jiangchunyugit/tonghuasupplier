package cn.tonghua.service.utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author gejiaming
 */
public class DateUtil {
    public static final String FORMAT_YYMMDD_HHmmss = "yyyy-MM-dd HH:mm:ss";

    public static final String FORMAT_YYMMDD = "yyyy-MM-dd";
    /**
     * 保留2位小数
     */
    private static final int SCALE = 2;
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat SDF_TWO = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 时间格式化
     * @param time
     * @param formart
     * @return
     */
    public static String getStringDate(Date time,String formart){
        SimpleDateFormat format = new SimpleDateFormat(formart);
        return format.format(time);
    }

    /**
     * 判断时间是否在时间段内
     *
     * @param nowTime
     * @param beginTime
     * @param endTime
     * @return
     */
    public static boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * @param time      任意一个时间
     * @param pointTime 需要的时间点
     * @return
     * @throws ParseException
     * @Description 传入一个时间, 一个时间点, 获取这个时间代表天的这个时间点的  时间格式的  时间
     */
    public static synchronized Date getOneDateToIndate(Date time, String pointTime) throws ParseException {
        Date newDate = null;
        try {
            String strTime = SDF.format(time);
            String newTime = strTime + " " + pointTime;
            newDate = SDF_TWO.parse(newTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newDate;

    }

    /**
     * @param date
     * @return
     * @throws ParseException
     * @Description 传入一个 "2017-10-12 00:00:00"格式的时间点,返回一个 "2017-10-12"的时间
     */
    public static Date getNewDate(Date date) throws ParseException {
        Date newDate = SDF.parse(SDF.format(date));
        return newDate;
    }

    /**
     * @param strDate
     * @return
     * @throws ParseException
     * @Description 传入一个 "2017-10-12"格式的时间点,返回一个 "2017-10-12"的时间
     */
    public static Date getNewDate(String strDate){
        Date newDate = null;
        try {
           newDate = SDF.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return newDate;
    }

    /**
     * @param strDate
     * @return
     * @throws ParseException
     * @Description 传入一个 "2017-10-12 00:00:00"格式的时间点字符串,返回一个 "2017-10-12"的字符串
     */
    public static synchronized String getNewStrDate(String strDate) throws ParseException {
        Date toDate = SDF_TWO.parse(strDate);
        String strNewDate = SDF.format(toDate);
        return strNewDate;
    }

    /**
     * @param time long类型数(毫秒)
     * @return
     * @Description 通过一个long类型时间, 获取相应的小时
     */
    public static Double getDoubleTime(Long time) {
        if (time == null) {
            return 0.0;
        }
        BigDecimal bigDecimalDivide = (new BigDecimal(time)).divide((new BigDecimal(1000 * 60 * 60)), SCALE, BigDecimal.ROUND_HALF_UP);
        Double hOnWorkTime = bigDecimalDivide.doubleValue();
        return hOnWorkTime;
    }

    /**
     * @param time long类型数(毫秒)
     * @return
     * @Description 通过一个int类型时间, 获取相应的小时
     */
    public static Double getDoubleTime(int time) {
        if (time == 0) {
            return 0.0;
        }
        BigDecimal bigDecimalDivide = (new BigDecimal(time)).divide((new BigDecimal(60)), SCALE, BigDecimal.ROUND_HALF_UP);
        Double hOnWorkTime = bigDecimalDivide.doubleValue();
        return hOnWorkTime;
    }

    /**
     * @param strDate
     * @return
     * @throws ParseException
     * @Description 传入一个 "2017-10-12 00:00:00"类型的字符串,返回一个时间类型的时间
     */
    public static Date getDateToStr(String strDate) throws ParseException {
        if (strDate == null || "".equals(strDate.trim())) {
            return new Date(0);
        }
        return SDF_TWO.parse(strDate);
    }

    /**
     * @param startDate
     * @param endDate
     * @return
     * @Description 传入两个时间, 获得两个时间差, 返回结果为long类型的(毫秒)
     */
    public static long getLongTime(Date startDate, Date endDate) {
        long longTime = 0;
        if (startDate != null && endDate != null) {
            longTime = endDate.getTime() - startDate.getTime();
        }
        return longTime;
    }

    /**
     * @return
     * @throws ParseException
     * @Description 获取某段时间内的所有日期
     */
    public static List<String> findDates(String strBegin, String strEnd) throws ParseException {
        Date dBegin = getDateToStr(strBegin);
        Date dEnd = getDateToStr(strEnd);
        List<String> lDate = new ArrayList<String>();
        while (dBegin.getTime() < dEnd.getTime()) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            lDate.add(SDF.format(dBegin));
            dBegin = new Date(dBegin.getTime() + 1000 * 24 * 60 * 60);
        }
        return lDate;
    }

    /**
     * 传入一个时间点和一个间隔时间值,获取一个累加后的时间点
     *
     * @param beginTime 开始时间点
     * @param time      累加的时间点
     * @return
     */
    public static Date getDate(Date beginTime, Double time) {
        long allTime = beginTime.getTime() + (new BigDecimal(time * 24 * 60 * 60 * 1000)).longValue();
        return new Date(allTime);
    }

    /**
     * 传入一个时间点和一个间隔时间值,获取一个累加后的时间点
     *
     * @param beginTime 开始时间点
     * @param time      累加的时间点
     * @return
     */
    public static Date getDate(Date beginTime, Integer time) {
        long allTime = beginTime.getTime() + (new BigDecimal(time * 24 * 60 * 60 * 1000)).longValue();
        return new Date(allTime);
    }


    /**
     * 两个时间比较大小并转换成天
     *
     * @param dateOne
     * @param dateTwo
     * @return
     */
    public static int daysCalculate(Date dateOne, Date dateTwo) {

        String date1 = formartDate(dateOne, "yyyy-MM-dd HH:mm:ss");
        String date2 = formartDate(dateTwo, "yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        long timeOne = dateOne.getTime();
        long timeTwo = dateTwo.getTime();

        return (int) ((timeTwo - timeOne) / (1000 * 60 * 60 * 24));
    }

    /**
     * 将date类型转换成字符串
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String formartDate(Date date, String pattern) {
        SimpleDateFormat fmt = new SimpleDateFormat(pattern);
        return fmt.format(date);
    }

    /**
     * 将字符串格式转Date
     *
     * @param timeStr
     * @param pattern
     * @return
     */
    public static Date formateToDate(String timeStr, String pattern) {
        SimpleDateFormat fmt = new SimpleDateFormat(pattern);
        try {
            return fmt.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 返回两个时间相差的天数
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDaysByMillisecond(Date date1, Date date2) {
        BigDecimal bigDecimalOne = new BigDecimal(date2.getTime() - date1.getTime());
        BigDecimal bigDecimalTwo = new BigDecimal(1000 * 3600 * 24);
        BigDecimal i = bigDecimalOne.divide(bigDecimalTwo, BigDecimal.ROUND_DOWN);
        return i.intValue();
    }

    /**
     * 返回两个时间在除去天数的基础上相差的天数
     * @param date1
     * @param date2
     * @return
     */
    public static int differentHoursByMillisecond(Date date1,Date date2){
        if(date1 == null || date2 == null){
            return 0;
        }
        BigDecimal bigDecimalOne = new BigDecimal(date2.getTime() - date1.getTime());
        BigDecimal bigDecimalTwo = new BigDecimal(1000 * 3600 * 24);
        if(date2.getTime()<date1.getTime()){
            return 0;
        }
        int i = bigDecimalOne.divide(bigDecimalTwo, 0, BigDecimal.ROUND_UP).intValue();
        return i;
    }

    public static void main(String[] args) {
        Date date1 = formateToDate("2018-08-09 00:00:00", FORMAT_YYMMDD_HHmmss);
        Date date2 = formateToDate("2018-08-01 00:00:01", FORMAT_YYMMDD_HHmmss);
        int i = differentHoursByMillisecond(date1, date2);
        System.out.println(i);
    }

}
