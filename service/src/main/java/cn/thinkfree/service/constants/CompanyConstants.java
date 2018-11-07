package cn.thinkfree.service.constants;


public class CompanyConstants{
    /**
     * 公司类型0:有限责任公司,1:股份有限公司,2:个人独资企业,3:合伙企业,4:全民所有制企业,5:集体所有制企业
     */
    public enum CompanySharesType {
        /**
         * 有限责任公司
         */
        LIMITED_LIABILITY_COMPANY(0,"有限责任公司"),
        /**
         * 股份有限公司
         */
        COMPANY_LIMITED(1,"股份有限公司"),
        /**
         * 个人独资企业
         */
        PERSONAL_OWNED_ENTERPRISES(2,"个人独资企业"),
        /**
         * 合伙企业
         */
        PARTNERSHIP(3,"合伙企业"),
        /**
         * 全民所有制企业
         */
        OWNER_OWNED_ENTERPRISE(4,"全民所有制企业"),
        /**
         * 集体所有制企业
         */
        COLLECTIVELY_OWNED_ENTERPRISE(5,"集体所有制企业");


        public final Integer code;
        public final String mes;

        CompanySharesType(Integer code ,String mes){
            this.code = code;
            this.mes = mes;
        }

        public static String getDesc(Integer value) {
            CompanySharesType[] companySharesTypes = values();
            for (CompanySharesType companySharesType : companySharesTypes) {
                if (companySharesType.code.equals(value)) {
                    return companySharesType.mes;
                }
            }
            return null;
        }
    }

    /**
     * 平台状态：0正常，1冻结，2下架（冻结高于下架）
     */
    public enum PlatformType{
        /**
         * 正常
         */
        NORMAL(0,"正常"),
        /**
         * 冻结
         */
        FREEZE(1,"冻结"),
        /**
         * 下架
         */
        OBTAINED(2,"下架");

        public final Integer code;
        public final String mes;

        PlatformType(Integer code ,String mes){
            this.code = code;
            this.mes = mes;
        }
        public Short shortVal(){
            return code.shortValue();
        }
    }

    /**
     * 审核类型 0入驻 1合同 2变更 3续签
     */
    public enum AuditType{
        /**
         * 入驻
         */
        JOINON(0,"入驻"),
        /**
         * 合同
         */
        CONTRACT(1,"合同"),
        /**
         * 变更
         */
        CHANGE(2,"变更"),

        /**
         * 续签
         */
        RENEW(3,"续签");

        public final Integer code;
        public final String mes;

        AuditType(Integer code ,String mes){
            this.code = code;
            this.mes = mes;
        }
        public String stringVal(){
            return code.toString();
        }
    }

}

