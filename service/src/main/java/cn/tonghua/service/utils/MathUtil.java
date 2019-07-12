package cn.tonghua.service.utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author gejiaming
 */
public class MathUtil {

    private static final Integer ONE_HUNDRED = 100;

    /**
     * 由于Java的简单类型不能够精确的对浮点数进行运算，这个工具类提供精 确的浮点数运算，包括加减乘除和四舍五入。
     */

    /**
     * 默认除法运算精度
     */
    private static final int DEF_DIV_SCALE = 2;
    /**
     * 标准日期时间格式，精确到秒：yyyy-MM-dd HH:mm:ss
     */
    public static final String NORM_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 这个类不能实例化
     */
    private MathUtil() {
    }

    /**
     * 加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        //取两位小数四舍五入
        return b1.add(b2).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double div(double v1, double v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The   scale   must   be   a   positive   integer   or   zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The   scale   must   be   a   positive   integer   or   zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 传入开始+结束+现在时间,获取当前时间占时间段的比例
     *
     * @param startTime
     * @param endTime
     * @param nowTime
     * @return
     */
    public static Integer getPercentage(Date startTime, Date endTime, Date nowTime) {
        if (nowTime.getTime() < startTime.getTime()) {
            return 0;
        }
        if (nowTime.getTime() > endTime.getTime()) {
            return 100;
        }
        BigDecimal b1 = new BigDecimal(nowTime.getTime() - startTime.getTime());
        BigDecimal b2 = new BigDecimal(endTime.getTime() - startTime.getTime());
        Double aa = b1.divide(b2, DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP).doubleValue() * 100;
        return aa.intValue();
    }

    /**
     * 传入两个Int值 获取百分数
     *
     * @param allNum      总任务数
     * @param completeNum 完成任务数+1
     * @return
     */
    public static Integer getPercentage(Integer allNum, Integer completeNum) {
        if (allNum == null || allNum == 0 || completeNum == null || completeNum == 0 || allNum < completeNum) {
            return 0;
        }
        BigDecimal b1 = new BigDecimal(completeNum);
        BigDecimal b2 = new BigDecimal(allNum);
        Double aa = b1.divide(b2, DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP).doubleValue() * 100;
        return aa.intValue();
    }


    /**
     * 获取进度
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static BigDecimal getProgress(Date startTime, Date endTime) {
        //计划花费时间
        long total = subtractDate(endTime, startTime);
        //已用时间
        long trueTotal = subtractDate(new Date(), startTime);
        if (total == 0) {
            return BigDecimal.valueOf(ONE_HUNDRED);
        }
        double pg = trueTotal / (double) ONE_HUNDRED / total * (double) 10000;
        BigDecimal bd = BigDecimal.valueOf(pg).setScale(2, BigDecimal.ROUND_HALF_UP);
        if (bd.compareTo(BigDecimal.valueOf(ONE_HUNDRED)) > 0) {
            return BigDecimal.valueOf(ONE_HUNDRED);
        } else if (bd.compareTo(BigDecimal.valueOf(0)) < 0) {
            return BigDecimal.valueOf(0);
        }
        return bd;
    }

    /**
     * 将时间转化为毫秒数比较大小
     */
    public static long subtractDate(Date dateOne, Date dateTwo) {

        String date1 = formartDate(dateOne, NORM_DATETIME_PATTERN);
        String date2 = formartDate(dateTwo, NORM_DATETIME_PATTERN);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(NORM_DATETIME_PATTERN);

        long timeOne = 0;
        long timeTwo = 0;

        try {
            timeOne = simpleDateFormat.parse(date1).getTime();
            timeTwo = simpleDateFormat.parse(date2).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeOne - timeTwo;
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
        }
        return null;
    }

    /**
     * 元转分
     *
     * @param money
     * @return
     */
    public static Long getFen(String money) {
        BigDecimal b1 = new BigDecimal(money);
        BigDecimal b2 = new BigDecimal(100);
        long result = b1.multiply(b2).longValue();
        return result;
    }

    /**
     * 分转元
     *
     * @param money
     * @return
     */
    public static String getYuan(Long money) {
        BigDecimal b1 = new BigDecimal(money);
        BigDecimal b2 = new BigDecimal(100);
        String result = b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP).toString();
        return result;
    }

    public static void main(String[] args) {

        System.out.println(getPercentage(3,2));
    }

}
