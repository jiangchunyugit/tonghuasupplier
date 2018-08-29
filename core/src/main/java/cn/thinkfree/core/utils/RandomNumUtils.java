package cn.thinkfree.core.utils;

import java.security.SecureRandom;

public class RandomNumUtils {

    /**
     * 获取指定位数随机数
     * @param length
     * @return
     */
    public static String random(int length){
        SecureRandom random = new SecureRandom();
        StringBuffer sb = new StringBuffer(length);
        sb.append(random.nextInt(9)+1);
        for(int i=1;i<length;i++){
            sb.append(random.nextInt(9));
        }

        return sb.toString();
    }


}
