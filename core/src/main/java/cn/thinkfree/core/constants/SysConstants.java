package cn.thinkfree.core.constants;

/**
 * 常量
 */
public class SysConstants {

    /**
     *  是否值
     */
    public enum YESORNO {
        YES("1"), NO("0");
        public Byte val;

        YESORNO(String val) {
            this.val = Byte.valueOf(val);
        }

        Boolean getBoolVal() {
            return this.val == 0 ? false : true;
        }
    }


}
