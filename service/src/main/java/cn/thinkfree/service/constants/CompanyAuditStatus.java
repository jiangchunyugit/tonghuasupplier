package cn.thinkfree.service.constants;

/**
 * @author ying007
 * 公司入驻审批状态 0待激活1已激活2财务审核中3财务审核成功4财务审核失败5待交保证金6已交保证金 7入驻成功
 */
public enum CompanyAuditStatus {
    /**
     * 待激活
     */
    NOTACTIVATION(0,"待激活"),
    /**
     * 已激活
     */
    ACTIVATION(1,"已激活"),
    /**
     * 财务审核中
     */
    CHECKING(2,"财务审核中"),
    /**
     * 财务审核成功
     */
    SUCCESSCHECK(3,"财务审核成功"),
    /**
     * 财务审核失败
     */
    FAILCHECK(4,"财务审核失败"),
    /**
     * 待交保证金
     */
    NOTPAYBAIL(5,"待交保证金"),
    /**
     * 已交保证金
     */
    PAYBAIL(6,"已交保证金"),
    /**
     * 入驻成功
     */
    SUCCESSJOIN(7,"入驻成功");

    public final Integer code;
    public final String mes;

    CompanyAuditStatus(Integer code ,String mes){
        this.code = code;
        this.mes = mes;
    }
    public String stringVal(){
        return code.toString();
    }

}
