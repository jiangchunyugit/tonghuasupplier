package cn.thinkfree.database.vo.account;

import cn.thinkfree.database.model.SystemPermission;

public class PermissionVO extends SystemPermission {
    /**
     * 创建者姓名
      */
    private String creatorName;

    /**
     * 资源集
     */
    private String resources;

    /**
     * 是否授权
     */
    private String isGrant;

    public String getIsGrant() {
        return isGrant;
    }

    public void setIsGrant(String isGrant) {
        this.isGrant = isGrant;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getResources() {
        return resources;
    }

    public void setResources(String resources) {
        this.resources = resources;
    }
}
