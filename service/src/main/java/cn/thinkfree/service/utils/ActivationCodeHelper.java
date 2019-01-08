package cn.thinkfree.service.utils;

import java.security.SecureRandom;
import java.util.function.Supplier;

public class ActivationCodeHelper {

    protected final static String FULL_CHART ="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    protected final static String CHART_TABLE ="ABCDEFGHJKLMNPQRSTUVWXYZ3456789";

    public enum ActivationCode{
        /**
         * 纯数字
         */
        PureNumber( ()->{
            SecureRandom random = new SecureRandom();
            StringBuffer result = new StringBuffer(6);
            for(int i=0;i<6;i++){
                result.append(random.nextInt(10));
            }
            return result.toString();
        }),
        /**
         * 纯字母
         */
        PureCharacter( ()->{
            SecureRandom random = new SecureRandom();
            StringBuffer result = new StringBuffer(6);
            for(int i=0;i<6;i++){
                result.append(FULL_CHART.charAt(random.nextInt(26)));
            }
            return result.toString();
        }),
        /**
         * 字母及数字
         */
        Mix( ()->{
            SecureRandom random = new SecureRandom();
            StringBuffer result = new StringBuffer(6);
            for(int i=0;i<6;i++){
                result.append(CHART_TABLE.charAt(random.nextInt(31)));
            }
            return result.toString();
        });

        public Supplier<String> code;

        ActivationCode( Supplier<String> action) {

            this.code = action;
        }
    }

}
