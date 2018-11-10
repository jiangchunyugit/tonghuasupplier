package cn.thinkfree.core.constants;

/**
 * 常量
 */
public class SysConstants {

    /**
     *  是否值
     */
    public enum YesOrNo {
        YES("1"), NO("0");
        public final Byte val;

        YesOrNo(String val) {
            this.val = Byte.valueOf(val);
        }

        public Boolean getBoolVal() {
            return this.val == 0 ? false : true;
        }
        public Short shortVal(){
            return this.val.shortValue();
        }
    }

    /**
     *  是否值
     */
    public enum YesOrNoSp {
        YES("1"), NO("2");
        public final Byte val;

        YesOrNoSp(String val) {
            this.val = Byte.valueOf(val);
        }

        public Boolean getBoolVal() {
            return this.val == 0 ? false : true;
        }
        public Short shortVal(){
            return this.val.shortValue();
        }
    }

    /**
     *  是否值
     */
    public enum NoOrYes {
        YES("0"), NO("1");
        public final Byte val;

        NoOrYes(String val) {
            this.val = Byte.valueOf(val);
        }

        public Boolean getBoolVal() {
            return this.val == 0 ? false : true;
        }
        public Short shortVal(){
            return this.val.shortValue();
        }
    }

    /**
     * 平台类型
     */
    public enum PlatformType {
        Enterprise(0),
        Platform(1);
        public Integer code ;
        PlatformType(Integer val){
            this.code  = val;
        }
    }

    /**
     * 邮件模板
     */
    public enum EmailTemplate{
        /**
         * 用户注册
         */
        register("001"),
        /**
         * 激活邮箱
         */
        activation("002"),
        join("003"),
        resetPwd("004");

        public String code;
        EmailTemplate(String code) {
            this.code = code;
        }
    }


}
