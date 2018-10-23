package cn.thinkfree.service.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * TODO 长度不对
 * 用户账号辅助类
 * 用户账号按25位定制
 *  标识位 2位
 *  混淆位 1位
 *  留空位 2位
 *  时间戳 13位
 *  随机位 5位
 *  流水位 5位
 *  如下
 *      PA 0 00 1540196572 X21P6 00001
 *      PC 0 00 1540196583 AQCV0 EF62Q
 */
public class UserNumberHelper {

    private static Integer length = 25;

    private static Long MAX_SEQUENCE = 99999L;

    private static String[] CHAR_TABLE = new String[62];

    private static AtomicLong SEQUENCE = new AtomicLong(0);

    static {
        // 初始化可用字符
        for (int i=0;i<=9;i++){
            CHAR_TABLE[i] = String.valueOf(i);
        }
        for(int i=0;i< 26;i++){
            CHAR_TABLE[i+10] = String.valueOf((char) (65+i));
            CHAR_TABLE[i+36]=String.valueOf((char)(97+i));
        }
    }


    /**
     * 创建编号
     * @param prefix
     * @return
     */
    public static String createUserNo(String prefix){
        return createUserNo(prefix,false);
    }

    /**
     * 创建用户编号
     * @param prefix  前戳
     * @param isSalt  是否加盐
     * @return
     */
    public static String createUserNo(String prefix,Boolean isSalt){
        StringBuffer userNo = new StringBuffer(25);
        userNo.append(prefix);
        if(!isSalt){
            // 不加盐
            userNo.append(BigInteger.ZERO);
            userNo.append(Instant.now().toEpochMilli());
            userNo.append(getSequence());
        }else{
            String salt = random(1);
            userNo.append(salt);
            userNo.append(salt(Integer.valueOf(salt),Instant.now().toEpochMilli()));
            userNo.append(getSequence());
        }
        userNo.append(randomCode(5));

        return userNo.toString();
    }

    /**
     * 获取序列
     * @return
     */
    private static String getSequence() {
        long now = SEQUENCE.getAndIncrement();
        if(MAX_SEQUENCE == now){
            now = 0;
        }
        return StringUtils.leftPad(String.valueOf(now),5,"0");
    }

    /**
     * 获取随机字符
     * @param size
     * @return
     */
    private static String randomCode(int size) {
        SecureRandom random = new SecureRandom();
        StringBuffer sb = new StringBuffer(size);
        for(int i=0;i< size;i++){
            sb.append(CHAR_TABLE[random.nextInt(32)]);
        }
        return sb.toString();
    }

    /**
     * 获取随机数
     * @param size
     * @return
     */
    private static String random(int size){
        StringBuffer sb = new StringBuffer(size);
        SecureRandom random = new SecureRandom();
        for(int i = 0;i< size;i++){
            sb.append(random.nextInt(16));
        }
        return sb.toString();
    }

    /**
     * 加盐混淆
     * @param salt
     * @param target
     * @return
     */
    private static String salt(Integer salt,Long target){
        return String.valueOf(salt ^ target);
    }

    public static void main(String[] args) {
        ExecutorService ex = Executors.newFixedThreadPool(5);
        ex.execute(()->{
            for (int i =0;i<50;i++){
                System.out.println( UserNumberHelper.createUserNo("PC",true));
            }
        });
        ex.execute(()->{
            for (int i =0;i<50;i++){
                System.out.println( UserNumberHelper.createUserNo("PA",true));
            }
        });
//        ex.execute(()->{
//            for (int i =0;i<100;i++){
//                System.out.println( UserNumberHelper.createUserNo("PB",false));
//            }
//        }); ex.execute(()->{
//            for (int i =0;i<100;i++){
//                System.out.println( UserNumberHelper.createUserNo("PD",true));
//            }
//        }); ex.execute(()->{
//            for (int i =0;i<100;i++){
//                System.out.println( UserNumberHelper.createUserNo("PE",true));
//            }
//        });

    }

}
