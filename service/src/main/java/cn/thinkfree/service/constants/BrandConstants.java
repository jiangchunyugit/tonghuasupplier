package cn.thinkfree.service.constants;

public class BrandConstants {
    /**
     * 0：运营审核中 1：运营审核不通过 2：运营审核通过 4:品牌作废 5:品牌变更中6：品牌变更成功7：品牌变更失败
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
//        JOIN(3,"品牌入驻成功"),
        /**
         * 4:品牌作废
         */
        DISCARD(4,"品牌作废"),
        /**
         * 5:品牌变更中6
         */
        UPDATEING(5,"品牌变更中"),
        /**
         * 6：品牌变更成功
         */
        UPDATE_SUCCESS(6, "品牌变更成功"),
        /**
         * 7：品牌变更失败
         */
        UPDATE_FAIL(7,"品牌变更失败");


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
