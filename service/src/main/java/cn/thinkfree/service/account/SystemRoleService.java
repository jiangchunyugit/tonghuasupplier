package cn.thinkfree.service.account;

import cn.thinkfree.database.model.SystemRole;
import cn.thinkfree.database.vo.account.RegionsRoleVO;
import cn.thinkfree.database.vo.account.SystemRoleSEO;
import cn.thinkfree.database.vo.account.SystemRoleVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface SystemRoleService {
    /**
     * 新增角色
     * @param systemRoleVO
     * @return
     */
    SystemRole save(SystemRoleVO systemRoleVO);

    /**
     * 分页查询角色
     * @param systemRoleSEO
     * @return
     */
    PageInfo<SystemRole> page(SystemRoleSEO systemRoleSEO);

    /**
     * 角色详情
     * @param id
     * @return
     */
    SystemRole detail(Integer id);

    /**
     * 编辑角色
     * @param systemRoleVO
     * @return
     */
    SystemRole edit( SystemRoleVO systemRoleVO);

    /**
     * 更新角色权限
     * @param id
     * @param permissions
     * @return
     */
    String updateRoleByGrant(Integer id, Integer[] permissions);

    /**
     * 修改角色状态
     * @param id
     * @param state
     * @return
     */
    String updateRoleState(Integer id, Short state);

    /**
     * 删除角色
     * @param id
     * @return
     */
    String updateRoleForDel(Integer id);

    /**
     * 根据适用范围查询角色名称
     * @param scope
     * @return
     */
    List<SystemRole> listRoleByScope(Integer scope);

    /**
     * 查询区域角色
     *
     * @param id  账号主键
     * @param rid 区域主键
     * @param level  区域级别
     * @return
     */
    List<SystemRole> listRoleByRegions(String id, String rid, String level);

    /**
     * 授权区域角色
     * @param regionsRoleVO  区域角色信息
     * @return
     */
    String authRegionsRole(RegionsRoleVO regionsRoleVO);
}
