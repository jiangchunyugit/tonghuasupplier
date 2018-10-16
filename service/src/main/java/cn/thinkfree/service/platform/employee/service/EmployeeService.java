package cn.thinkfree.service.platform.employee.service;

/**
 * @author xusonghui
 * 员工管理
 */
public interface EmployeeService {
    /**
     * 员工实名认证审核
     *
     * @param userId    用户ID
     * @param authState 审核状态
     */
    void reviewEmployee(String userId, int authState);

    /**
     * 处理员工申请
     *
     * @param userId             用户ID
     * @param employeeApplyState 员工申请状态，1入驻待审核，2入驻不通过，3已入驻，4解约待审核，5解约不通过，6已解约
     */
    void employeeApply(String userId, int employeeApplyState);

    /**
     * 处理申请
     *
     * @param userId             用户ID
     * @param employeeApplyState 员工申请状态，1入驻待审核，2入驻不通过，3已入驻，4解约待审核，5解约不通过，6已解约
     * @param dealExplain        处理结果
     * @param dealUserId         处理人ID
     */
    void dealApply(String userId, int employeeApplyState, String dealExplain, String dealUserId);
}
