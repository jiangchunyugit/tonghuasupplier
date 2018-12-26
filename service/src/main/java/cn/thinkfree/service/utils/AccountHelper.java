package cn.thinkfree.service.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 用户账号辅助类
 * 用户账号按30位定制
 *  标识位 2位
 *  混淆位 1位
 *  预留位 4位
 *  时间戳 13位
 *  随机位 5位
 *  流水位 5位
 *  如下
 *      PA 0 0000 1540196572 X21P6 00001
 *      PC b 0000 1540196583 AQCV0 00004
 */
public class AccountHelper {



    /**
     * 用户类型
     */
    public enum UserType{
        /**
         * 后台用户
         */
        PC(1,"PC"),
        /**
         * APP用户
         */
        APP(2,"AP"),
        /**
         * 后台企业用户
         */
        PE(3,"PE");
        public Integer code;
        public String prefix;
        UserType(Integer code,String prefix){
            this.code = code;
            this.prefix = prefix;
        }
    }


    /**
     * 编号长度
     */
    private final static Integer NO_LENGTH = 30;

    /**
     * 序列最大值
     */
    private final static Long MAX_SEQUENCE = 99000L;


    /**
     * 可用字符列表
     */
    private static String[] CHAR_TABLE = new String[62];

    /**
     * 序列
     */
    private static AtomicLong SEQUENCE = new AtomicLong(0);
    /**
     * 预留位长度
     */
    private final static Integer PRESET_LENGTH = 4;

    /**
     * 随机位长度
     */
    private final static Integer RANDOM_LENGTH =5;

    /**
     * 填充物
     */
    private final static String FILLER = "0";

    /**
     * 默认密码长度
     */
    private final static Integer DEFAULT_PASSWORD_SIZE = 8;

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
     * 创建用户密码
     * @return
     */
    public static String createUserPassWord(){
        return randomCode(DEFAULT_PASSWORD_SIZE);
    }

    /**
     * 创建用户编号
     * @param prefix  前戳
     * @param isSalt  是否加盐
     * @return
     */
    public static String createUserNo(String prefix,Boolean isSalt){
        StringBuffer userNo = new StringBuffer(NO_LENGTH);
        userNo.append(prefix);
        if(!isSalt){
            // 不加盐
            userNo.append(BigInteger.ZERO);
            userNo.append(StringUtils.leftPad("0",PRESET_LENGTH,FILLER));
            userNo.append(System.currentTimeMillis());
        }else{
            String salt = random(1);
            userNo.append(Long.toHexString(Integer.valueOf(salt)).toUpperCase());
            userNo.append(StringUtils.leftPad("0",PRESET_LENGTH,FILLER));
            userNo.append(salt(Integer.valueOf(salt),System.currentTimeMillis()));
        }
        userNo.append(randomCode(RANDOM_LENGTH));
        userNo.append(getSequence());

        return userNo.toString();
    }

    /**
     * 获取序列
     * @return
     */
    private static String getSequence() {
        long now = SEQUENCE.getAndIncrement();
        if(MAX_SEQUENCE == now){
            SEQUENCE.set(0);
        }
        return StringUtils.leftPad(String.valueOf(now),5,FILLER);
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
            sb.append(CHAR_TABLE[random.nextInt(62)]);
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
        SecureRandom random = getRandom();

        for(int i = 0;i< size;i++){
            sb.append(random.nextInt(16));
        }
        return sb.toString();
    }

    public static SecureRandom getRandom() {
        SecureRandom random = null;
        try {
            random = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            random = new SecureRandom();
        }
        return random;
    }

    /**
     * 经销商合同编号后三位随机数
     * @param size
     * @return
     */
    public static String randomAgencyContract(int size){
        StringBuffer sb = new StringBuffer(size);
        SecureRandom random = getRandom();
        for(int i = 0;i< size;i++){
            sb.append(random.nextInt(9));
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



}
