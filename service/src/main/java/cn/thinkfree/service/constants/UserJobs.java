package cn.thinkfree.service.constants;

import java.util.Arrays;
import java.util.Optional;

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
    QualityInspector(4,"质检","CQM"),
    /**
     * 设计师
     */
    Designer(5,"设计师","CD"),
    /**
     * 业主
     */
    Owner(6,"业主","CC");

    public final Integer code;
    public final String mes;
    public final String roleCode;
    UserJobs(Integer code, String mes,String roleCode) {
        this.code = code;
        this.mes = mes;
        this.roleCode = roleCode;
    }

    public static UserJobs findByCode(String code){
        Optional<UserJobs> op = Arrays.stream(UserJobs.values()).filter(j -> Short.valueOf(code).equals(j.code.shortValue())).findFirst();
       return op.isPresent() ? op.get():null;
    }
    public static UserJobs findByCodeStr(String code){
        Optional<UserJobs> op = Arrays.stream(UserJobs.values()).filter(j ->code.equals(j.roleCode)).findFirst();
        return op.isPresent() ? op.get():null;
    }
}
