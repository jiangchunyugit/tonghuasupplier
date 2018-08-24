package cn.thinkfree.service.constants;

public enum  UserJobs {

    /**
     * 管家
     */
    Steward(1,"管家","CS"),
    /**
     * 项目经理
     */
    ProjectManager(2,"项目经理","CP"),
    /**
     * 工长
     */
    Foreman(3,"工长","CM"),
    /**
     * 质检
     */
    QualityInspector(4,"质检","CQM");

    public final Integer code;
    public final String mes;
    public final String roleCode;
    UserJobs(Integer code, String mes,String roleCode) {
        this.code = code;
        this.mes = mes;
        this.roleCode = roleCode;
    }
}
