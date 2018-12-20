package cn.thinkfree.service.constants;

public class BrandConstants {
    /**
     * 0：运营审核中 1：运营审核不通过 2：运营审核通过 3：品牌入驻成功
     */
    public enum AuditStatus{
        /**
         * 0：运营审核中
         */
        AUDITING(0,"运营审核中"),
        /**
         * 1：运营审核不通过
         */
        AUDITFAIL(1,"运营审核不通过"),
        /**
         * 2：运营审核通过
         */
        AUDITSUCCESS(2,"运营审核通过"),
        /**
         * 3：品牌入驻成功
         */
        JOIN(3,"品牌入驻成功"),
        /**
         * 4:品牌作废
         */
        DISCARD(4,"品牌作废");

        public final Integer code;
        public final String mes;

        AuditStatus(Integer code ,String mes){
            this.code = code;
            this.mes = mes;
        }
        public String stringVal(){
            return code.toString();
        }
    }
}
