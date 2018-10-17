package cn.thinkfree.service.constants;


public class CompanyConstants{
    /**
     * 公司类型0:有限责任公司,1:股份有限公司,2:个人独资企业,3:合伙企业,4:全民所有制企业,5:集体所有制企业
     */
    public enum CompanySharesType {
        /**
         * 有限责任公司
         */
        NOTACTIVATION(0,"有限责任公司"),
        /**
         * 股份有限公司
         */
        ACTIVATION(1,"股份有限公司"),
        /**
         * 个人独资企业
         */
        CHECKING(2,"个人独资企业"),
        /**
         * 合伙企业
         */
        SUCCESSCHECK(3,"合伙企业"),
        /**
         * 全民所有制企业
         */
        FAILCHECK(4,"全民所有制企业"),
        /**
         * 集体所有制企业
         */
        NOTPAYBAIL(5,"集体所有制企业");


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
        NOTACTIVATION(0,"正常"),
        /**
         * 冻结
         */
        ACTIVATION(1,"冻结"),
        /**
         * 下架
         */
        CHECKING(2,"下架");

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

}

