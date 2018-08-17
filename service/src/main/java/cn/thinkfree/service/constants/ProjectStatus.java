package cn.thinkfree.service.constants;

/**
 * 项目状态
 */
public enum ProjectStatus {

    /**
     * 未上线
     */
    NotOnLine(1,"待上线"),
    /**
     * 未开始
     */
    WaitStart(2,"未开始"),
    /**
     * 进行中
     */
    Working(3,"进行中"),
    /**
     * 停工
     */
    StopTheWork(4,"停工"),
    /**
     * 竣工
     */
    Complete(5,"竣工");

    public Integer code;
    public String mes;

    ProjectStatus(Integer code ,String mes){
        this.code = code;
        this.mes = mes;
    }
    public Short shortVal(){
        return code.shortValue();
    }

}
