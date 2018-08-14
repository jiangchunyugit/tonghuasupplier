package cn.thinkfree.service.constants;

public enum  UserJobs {

    /**
     * 管家
     */
    Steward(1,"管家"),
    /**
     * 项目经理
     */
    ProjectManager(2,"项目经理"),
    /**
     * 工长
     */
    Foreman(3,"工长"),
    /**
     * 质检
     */
    QualityInspector(4,"质检");

    public Integer code;
    public String mes;

    UserJobs(Integer code, String mes) {
        this.code = code;
        this.mes = mes;
    }
}
