package cn.thinkfree.service.platform.employee.service;

import cn.thinkfree.database.model.UserRoleSet;

import java.util.List;

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
     * @param companyId 公司ID
     */
    void reviewEmployee(String userId, int authState, String companyId);

    /**
     * 处理员工申请
     *
     * @param userId             用户ID
     * @param employeeApplyState 员工申请状态，1入驻待审核，2入驻不通过，3已入驻，4解约待审核，5解约不通过，6已解约
     * @param companyId          公司ID
     */
    void employeeApply(String userId, int employeeApplyState, String companyId);

    /**
     * 处理申请
     *
     * @param userId             用户ID
     * @param employeeApplyState 员工申请状态，1入驻待审核，2入驻不通过，3已入驻，4解约待审核，5解约不通过，6已解约
     * @param dealExplain        处理结果
     * @param dealUserId         处理人ID
     * @param companyId          公司ID
     */
    void dealApply(String userId, int employeeApplyState, String dealExplain, String dealUserId, String companyId);

    /**
     * 提交证件信息
     *
     * @param userId   用户ID
     * @param cardType 证件类型
     * @param cardNo   证件号码
     * @param realName 真实姓名
     */
    void submitCardMsg(String userId, int cardType, String cardNo, String realName);

    /**
     * 查询角色信息
     *
     * @return
     */
    List<UserRoleSet> queryRoles();

    /**
     * 设置用户角色
     *
     * @param userId    用户ID
     * @param roleCode  角色编码
     * @param companyId 公司ID
     */
    void setUserRole(String userId, String roleCode, String companyId);
}
