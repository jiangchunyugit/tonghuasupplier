package cn.thinkfree.database.vo.account;

import cn.thinkfree.database.model.SystemPermission;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("运营后台-系统权限")
public class PermissionVO extends SystemPermission {
    /**
     * 创建者姓名
      */
    @ApiModelProperty("创建人姓名")
    private String creatorName;

    /**
     * 资源集
     */
    @ApiModelProperty("资源集合")
    private String resources;

    /**
     * 是否授权
     */
    @ApiModelProperty("是否授权")
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
