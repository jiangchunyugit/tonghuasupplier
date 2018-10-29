package cn.thinkfree.service.account;

public interface PermissionResourceService {

    /**
     * 为权限分配资源
     * @param id
     * @param resources
     * @return
     */
    String updateSystemPermissionResource(Integer id, Integer[] resources);


}
