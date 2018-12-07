package cn.thinkfree.service.constants;


public class AgencyConstants {
    /**
     * 合同状态 0待提交1运营审核中2运营审核通过3运营审核不通4财务审核中 5财务审核通过 6财务审核不通过7待签约8生效中9冻结10过期11作废
     */
    public enum AgencyType {
        /**
         * 待提交
         */
        NOT_SUBMITTED(0,"待提交"),
        /**
         * 运营审核中
         */
        OPERATING_AUDIT_ING(1,"运营审核中"),
        /**
         * 运营审核通过
         */
        OPERATING_AUDIT_THROUGH(2,"运营审核通过"),
        /**
         * 运营审核不通过
         */
        OPERATING_AUDIT_REFUSED(3,"运营审核不通过"),
        /**
         * 财务审核中
         */
        FINANCIAL_AUDIT_ING(4,"财务审核中"),

        /**
         * 财务审核通过
         */
        FINANCIAL_AUDIT_THROUGH(5,"财务审核通过"),

        /**
         * 财务审核不通过
         */
        FINANCIAL_AUDIT_REFUSED(6,"财务审核不通过"),

        /**
         * 生效中
         */
        SIGN_UP(7,"待签约"),
        /**
         * 生效中
         */
        EFFECT_ING(8,"生效中"),



        /**
         * 财务审核通过
         */
        FREEZE_ING(9,"冻结"),


        /**
         * 财务审核通过
         */
        OVERDUE_ING(10,"过期"),

        /**
         * 作废
         */
        INVALID_ING(11,"作废");

        public final Integer code;
        public final String mes;

        AgencyType(Integer code ,String mes){
            this.code = code;
            this.mes = mes;
        }

        public static String getDesc(Integer value) {
            AgencyType[] companySharesTypes = values();
            for (AgencyType companySharesType : companySharesTypes) {
                if (companySharesType.code.equals(value)) {
                    return companySharesType.mes;
                }
            }
            return null;
        }
    }






}

