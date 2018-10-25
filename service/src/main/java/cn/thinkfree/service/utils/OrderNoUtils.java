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
        return UserNoUtils.getUserNo("DO") + UserNoUtils.getRandomString(5).toUpperCase();
    }

    /**
     * 获取设计订单编号
     *
     * @param prefix 根据指定前缀获取一个编号
     * @return
     */
    public static String getNo(String prefix) {
        return UserNoUtils.getUserNo("DO") + UserNoUtils.getRandomString(5).toUpperCase();
    }
}
