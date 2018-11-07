package cn.thinkfree.service.platform.basics;

import cn.thinkfree.core.constants.RoleFunctionEnum;
import cn.thinkfree.database.model.UserRoleSet;

import java.util.List;

/**
 * @author xusonghui
 * 判断，控制角色是否拥有某项操作能力
 */
public interface RoleFunctionService {
    /**
     * 检查改角色是否拥有某项权限的能力
     *
     * @param roleCode     角色编码
     * @param functionEnum 权限枚举
     * @return true拥有，false没有
     */
    boolean checkFun(String roleCode, RoleFunctionEnum functionEnum);

    /**
     * 根据权限枚举查询拥有该权限的角色信息
     *
     * @param functionEnum
     * @return
     */
    List<UserRoleSet> queryUserRoleByFun(RoleFunctionEnum functionEnum);

    /**
     * 根据权限枚举查询拥有该权限的角色信息
     *
     * @param functionEnum
     * @return
     */
    List<String> queryRoleCode(RoleFunctionEnum functionEnum);

    /**
     * 给某个角色赋予某种权限
     *
     * @param roleCode     角色编码
     * @param functionCode 权限编码
     */
    void addRel(String roleCode, String functionCode);

    /**
     * 删除某个角色和权限的关联关系
     *
     * @param roleCode     角色编码
     * @param functionCode 权限编码
     */
    void delRel(String roleCode, String functionCode);

}
