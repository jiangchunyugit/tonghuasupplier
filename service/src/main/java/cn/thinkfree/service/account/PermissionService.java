package cn.thinkfree.service.account;

import cn.thinkfree.database.model.SystemPermission;
import cn.thinkfree.database.vo.account.PermissionSEO;
import cn.thinkfree.database.vo.account.PermissionVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 权限相关服务
 */
public interface PermissionService {
    /**
     * 新增权限
     * @param permissionVO
     * @return
     */
    SystemPermission save(PermissionVO permissionVO);

    /**
     * 分页查询
     * @param permissionSEO
     * @return
     */
    PageInfo<SystemPermission> page(PermissionSEO permissionSEO);

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    SystemPermission detail(Integer id);

    /**
     * 编辑权限信息
     * @param permissionVO
     * @return
     */
    SystemPermission edit(PermissionVO permissionVO);

    /**
     * 更新权限状态
     * @param id
     * @param state
     * @return
     */
    String updatePermissionState(Integer id, Short state);

    /**
     * 查询权限状况根据角色ID
     * @param id
     * @return
     */
    List<SystemPermission> listPermissionByRoleID(Integer id);
}
