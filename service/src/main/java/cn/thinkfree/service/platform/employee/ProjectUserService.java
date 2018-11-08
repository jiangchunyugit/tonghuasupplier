package cn.thinkfree.service.platform.employee;

import cn.thinkfree.core.constants.RoleFunctionEnum;

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
    String queryUserIdOne(String projectNo, RoleFunctionEnum functionEnum);

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
}
