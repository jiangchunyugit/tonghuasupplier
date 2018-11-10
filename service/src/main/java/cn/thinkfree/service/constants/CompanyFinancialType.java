package cn.thinkfree.service.constants;

/**
 * 公司财务信息 账号类型
 */
public enum  CompanyFinancialType {

    /**
     * 居然金融
     */
    JURAN(0,"居然金融"),
    /**
     * 银行账号
     */
    BANK(1,"银行账号");

    public Integer code;
    public String mes;
    CompanyFinancialType(Integer code, String s) {
        this.code = code;
        this.mes = s;
    }

    public Short shortVal(){
        return code.shortValue();
    }
}
