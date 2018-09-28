package cn.thinkfree.database.constants;

/**
 * 公司级别：0：一级公司 1：二级公司（子公司，分站）2：三级公司（入驻公司）
 */
public enum  CompanyClassify {
    /**
     * 一级公司 居然
     */
    PRIMARY_COMPANY(0,"一级公司"),
    /**
     * 二级公司（子公司，分站）
     */
    SECONDARY_COMPANY(1,"二级公司"),
    /**
     *2：三级公司（入驻公司）
     */
    TERTIARY_COMPANY(2,"三级公司");

    public final Integer code;
    public final String desc;

    CompanyClassify(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public Short shortVal(){
        return code.shortValue();
    }
}
