package cn.thinkfree.service.platform.employee;

import cn.thinkfree.core.constants.RoleFunctionEnum;
import cn.thinkfree.database.model.OrderUser;

import java.util.List;

/**
 * @author xusonghui
 * 项目人员操作服务
 */
public interface ProjectUserService {
    /**
     * 根据项目编号和权限查询该项目对应权限的人的ID
     *
     * @param projectNo    项目编号
     * @param functionEnum 权限枚举
     * @return
     */
    List<String> queryUserId(String projectNo, RoleFunctionEnum functionEnum);

    /**
     * 根据项目编号和权限查询该项目对应权限的人的ID
     *
     * @param projectNo    项目编号
     * @param functionEnum 权限枚举
     * @return
     */
    List<OrderUser> queryOrderUser(String projectNo, RoleFunctionEnum functionEnum);

    /**
     * 根据项目编号和权限查询该项目对应权限的人的ID
     *
     * @param projectNo    项目编号
     * @param functionEnum 权限枚举
     * @return
     */
    String queryUserIdOne(String projectNo, RoleFunctionEnum functionEnum);

    /**
     * 根据项目编号和权限查询该项目对应权限的人的ID
     *
     * @param projectNo    项目编号
     * @param functionEnum 权限枚举
     * @return
     */
    OrderUser queryOrderUserOne(String projectNo, RoleFunctionEnum functionEnum);

    /**
     * 添加项目人员
     *
     * @param orderNo      订单编号
     * @param projectNo    项目编号
     * @param userId       用户ID
     * @param functionEnum 权限枚举
     */
    void addUserId(String orderNo, String projectNo, String userId, RoleFunctionEnum functionEnum);

    /**
     * 删除员工和项目的关联关系
     *
     * @param orderNo      订单编号
     * @param projectNo    项目编号
     * @param userId       员工ID
     * @param functionEnum 权限枚举
     */
    void delUserRel(String orderNo, String projectNo, String userId, RoleFunctionEnum functionEnum);

    /**
     * 项目移交接口
     *
     * @param transferUserId   移交人ID
     * @param beTransferUserId 被移交人ID
     * @param projectNo        项目编号
     * @param roleCode         角色编码
     */
    void transferEmployee(String transferUserId, String beTransferUserId, String projectNo, String roleCode);
}
