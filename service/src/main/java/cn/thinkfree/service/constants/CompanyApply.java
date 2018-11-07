package cn.thinkfree.service.constants;

/**
 * @author ying007
 *
 * 公司审批相关常量
 */
public class CompanyApply {
    /**
     *  申请类型
     */
    public enum applyTpye {
        /**
         * 公司在app申请
         */
        APPAPPLY(0,"公司在app申请"),
        /**
         * 后台运营申请
         */
        PCAPPLY(1,"后台运营申请");
        public final Integer code;
        public final String msg;

        applyTpye(Integer code ,String msg){
            this.code = code;
            this.msg = msg;
        }
        public Short shortVal(){
            return code.shortValue();
        }
    }

    /**
     *  申请事项
     */
    public enum applyThinkType {
        /**
         * 入驻申请
         */
        APPLYJOIN(0,"入驻申请"),
        /**
         * 资质变更
         */
        APPLYCHANGE(1,"资质变更"),

        /**
         * 续签
         */
        APPLYCOMEON(2,"续签");

        public final Integer code;
        public final String msg;

        applyThinkType(Integer code ,String msg){
            this.code = code;
            this.msg = msg;
        }
        public Short shortVal(){
            return code.shortValue();
        }
    }
    /**
     *  审批角色
     */
    public enum auditType {
        /**
         * 运营审批
         */
        OPERATIONALAPPROVAL(0,"运营审批"),
        /**
         * 财务审批
         */
        FINANCIALAPPROVAL(1,"财务审批");

        public final Integer code;
        public final String msg;

        auditType(Integer code ,String msg){
            this.code = code;
            this.msg = msg;
        }
        public Short shortVal(){
            return code.shortValue();
        }
    }


}
