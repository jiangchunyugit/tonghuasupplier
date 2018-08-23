package cn.thinkfree.service.constants;

/**
 * 项目状态
 */
public enum ProjectStatus {

    /**
     * 未上线
     */
    NotOnLine(0,"待上线"),
    /**
     * 未开始
     */
    WaitStart(1,"未开始"),
    /**
     * 进行中
     */
    Working(2,"进行中"),
    /**
     * 停工
     */
    StopTheWork(3,"停工"),
    /**
     * 竣工
     */
    Complete(4,"竣工");

    public final Integer code;
    public final String mes;

    ProjectStatus(Integer code ,String mes){
        this.code = code;
        this.mes = mes;
    }
    public Short shortVal(){
        return code.shortValue();
    }

}
