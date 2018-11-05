package cn.thinkfree.service.constants;

import lombok.Getter;

@Getter
public enum UserRoleStatus {
    /**
     * 货运公司
     */
    STATUS_1("BG ", "货运公司"),
    /**
     *管家
     */
    STATUS_2("CS", "管家"),
    /**
     *项目经理
     */
    STATUS_3("CP", "项目经理"),
    /**
     *设计师
     */
    STATUS_4("CD", "设计师"),
    /**
     *质检员
     */
    STATUS_5("CQ", "质检员"),
    /**
     *工长
     */
    STATUS_6("CM", "工长"),
    /**
     *工人
     */
    STATUS_8("CW ", "工人"),
    /**
     *客服
     */
    STATUS_9("CCS", "客服"),
    /**
     *装饰公司
     */
    STATUS_10("BD", "装饰公司"),
    /**
     *质检管理员
     */
    STATUS_11("CQM", "质检管理员"),
    /**
     *业主
     */
    STATUS_12("CC", "业主");

    private String roleCode;
    private String roleName;

    UserRoleStatus(String roleCode, String roleName) {
        this.roleCode = roleCode;
        this.roleName = roleName;
    }

    /**
     * 根据枚举的状态值查询枚举
     * @param roleCode
     * @return
     */
    public static UserRoleStatus queryByRoleCode(String roleCode){
        UserRoleStatus[] values = UserRoleStatus.values();
        for (UserRoleStatus userRoleStatus:values){
            if(userRoleStatus.roleCode.equals(roleCode)){
                return userRoleStatus;
            }
        }
        throw new RuntimeException("无效的状态值");
    }

    /**
     * 根据枚举的状态值查询枚举name值
     * @param roleCode
     * @return
     */
    public static String queryNameByRoleCode(String roleCode){
        UserRoleStatus[] values = UserRoleStatus.values();
        for (UserRoleStatus userRoleStatus:values){
            if(userRoleStatus.roleCode.equals(roleCode)){
                return userRoleStatus.roleName;
            }
        }
        throw new RuntimeException("无效的状态值");
    }
}
