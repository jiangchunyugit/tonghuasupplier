package cn.thinkfree.service.account;

import cn.thinkfree.database.model.UserRoleSet;
import cn.thinkfree.database.vo.account.UserRoleSetSEO;
import com.github.pagehelper.PageInfo;

public interface UserRoleSetService {
    /**
     * 保存企业角色
     * @param userRoleSet
     * @return
     */
    String saveEnterPriseRole(UserRoleSet userRoleSet);

    /**
     * 查询企业角色详情
     * @param id
     * @return
     */
    UserRoleSet findUserRoleSetVO(Integer id);

    /**
     * 分页查询企业角色列表
     * @param userRoleSetSEO
     * @return
     */
    PageInfo<UserRoleSet> pageEnterPriseRole(UserRoleSetSEO userRoleSetSEO);

    /**
     * 编辑企业角色信息
     * @param id
     * @param userRoleSet
     * @return
     */
    String updateEnterPriseRole(Integer id, UserRoleSet userRoleSet);
}
