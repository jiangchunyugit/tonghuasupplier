package cn.thinkfree.service.account;

import cn.thinkfree.database.model.UserRoleSet;

public interface UserRoleSetService {
    /**
     * 保存企业角色
     * @param userRoleSet
     * @return
     */
    String saveEnterPriseRole(UserRoleSet userRoleSet);
}
