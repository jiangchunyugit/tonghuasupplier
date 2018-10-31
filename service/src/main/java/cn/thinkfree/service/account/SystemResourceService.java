package cn.thinkfree.service.account;

import cn.thinkfree.database.model.SystemResource;

import java.util.List;

public interface SystemResourceService {
    /**
     * 查询当前权限的资源情况
     * @param id
     * @return
     */
    List<SystemResource> listResourceByPermissionID(Integer id);


    /**
     * 查询当前企业角色资源状况
     * @param id
     * @return
     */
    List<SystemResource> listResourceByEnterPriseRoleID(Integer id);
}
