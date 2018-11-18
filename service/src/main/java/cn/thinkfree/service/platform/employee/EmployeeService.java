package cn.thinkfree.service.platform.employee;

import cn.thinkfree.database.model.*;
import cn.thinkfree.service.platform.vo.EmployeeApplyVo;
import cn.thinkfree.service.platform.vo.EmployeeMsgVo;
import cn.thinkfree.service.platform.vo.PageVo;
import cn.thinkfree.service.platform.vo.RoleVo;

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
     * @param authState 审核状态1未认证，2已认证，3实名认证审核中，4审核不通过
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
    void dealApply(String userId, int employeeApplyState, String dealExplain, String dealUserId, String roleCode, String companyId);

    /**
     * 提交证件信息
     *
     * @param userId   用户ID
     * @param cardType 证件类型
     * @param cardNo   证件号码
     * @param realName 真实姓名
     * @param country  所属国家
     * @param photo1   证件-正面
     * @param photo2   证件-反面
     * @param photo3   手持证件
     */
    void submitCardMsg(String userId, int cardType, String cardNo, String realName, String country, String photo1, String photo2, String photo3);

    /**
     * 查询角色信息
     *
     * @param searchKey 搜索关键字
     * @param state     角色状态，-1全部，1启用，2未启用
     * @param pageSize  每页多少条
     * @param pageIndex 第几页
     * @return
     */
    PageVo<List<RoleVo>> queryRoles(String searchKey, int state, int pageSize, int pageIndex);

    /**
     * 设置用户角色
     *
     * @param userId    用户ID
     * @param roleCode  角色编码
     * @param companyId 公司ID
     */
    void setUserRole(String userId, String roleCode, String companyId);

    /**
     * 通过userId获取用户信息
     *
     * @param userId userId
     * @return
     */
    EmployeeMsgVo employeeMsgById(String userId);

    /**
     * 创建角色
     *
     * @param roleName 角色名称
     * @param remark   备注
     */
    void createRole(String roleName, String remark);

    /**
     * 删除角色
     *
     * @param roleCode
     */
    void delRole(String roleCode);

    /**
     * 启用角色
     *
     * @param roleCode 角色编码
     * @param state    1启用，2未启用，3禁用
     */
    void enableRole(String roleCode, int state);

    /**
     * 根据公司ID和角色编码，搜索条件查询员工信息
     *
     * @param companyId 公司ID
     * @param roleCode  角色编码
     * @param searchKey 搜索条件
     * @return
     */
    PageVo<List<EmployeeMsgVo>> queryEmployee(String companyId, String roleCode, String searchKey, int pageSize, int pageIndex);

    /**
     * 查询所有员工信息
     *
     * @param roleCode  角色编码
     * @param searchKey 搜索关键字
     * @param city      城市编码
     * @param pageSize
     * @param pageIndex
     * @return
     */
    PageVo<List<EmployeeMsgVo>> queryStaffByPlatform(String roleCode, String searchKey, String city, int pageSize, int pageIndex);

    /**
     * 编辑角色编码
     *
     * @param roleCode
     * @param roleName
     * @param remark
     */
    void editRole(String roleCode, String roleName, String remark);

    /**
     * 查询设计师列表
     *
     * @param companyId
     * @param roleCode
     * @return
     */
    List<EmployeeMsg> queryDesignerByCompanyId(String companyId, String roleCode);

    /**
     * 查询设计师公司的员工
     *
     * @param companyId 公司ID
     * @param roleCode  角色编码
     * @param searchKey 搜索关键字
     * @param pageSize  每页多少条
     * @param pageIndex 第几页
     * @return
     */
    PageVo<List<EmployeeMsgVo>> queryStaffByDesignCompanyId(String companyId, String roleCode, String searchKey, int pageSize, int pageIndex);

    /**
     * 查询员工申请记录表
     *
     * @param companyId   公司ID
     * @param companyType 公司类型，1装饰，2设计
     * @return
     */
    PageVo<List<EmployeeApplyVo>> waitDealList(String companyId, int companyType, int pageSize, int pageIndex);
}
