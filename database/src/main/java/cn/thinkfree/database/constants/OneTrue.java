package cn.thinkfree.database.constants;

public class OneTrue {

    /**
     *  是否值:1是 2否
     */
    public enum YesOrNo {
        YES("1"), NO("2");
        public final Byte val;

        YesOrNo(String val) {
            this.val = Byte.valueOf(val);
        }

        public Short shortVal(){
            return this.val.shortValue();
        }
    }
}
