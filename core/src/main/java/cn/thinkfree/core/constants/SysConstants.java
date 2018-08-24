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


}
