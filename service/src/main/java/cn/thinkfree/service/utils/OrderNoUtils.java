package cn.thinkfree.service.utils;

/**
 * @author xusonghui
 * 设计订单编号生成规则
 */
public class OrderNoUtils {
    /**
     * 获取设计订单编号
     *
     * @return
     */
    public static String getNo() {
        return UserNoUtils.getUserNo("DO") + UserNoUtils.getRandomString(2).toUpperCase();
    }

    /**
     * 获取设计订单编号
     *
     * @param prefix 根据指定前缀获取一个编号
     * @return
     */
    public static String getNo(String prefix) {
        return UserNoUtils.getUserNo(prefix) + UserNoUtils.getRandomString(2).toUpperCase();
    }

    /**
     * 获取指定位数的随机字符串(包含小写字母、大写字母、数字,0<length)
     * @return String
     */
    public static String getCode(int length) {
        //随机字符串的随机字符库
        String KeyString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuffer sb = new StringBuffer();
        int len = KeyString.length();
        for (int i = 0; i < length; i++) {
            sb.append(KeyString.charAt((int) Math.round(Math.random() * (len - 1))));
        }
        return sb.toString();
    }
}
