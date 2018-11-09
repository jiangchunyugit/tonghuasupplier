package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.PcAuditInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("审批")
public class PcAuditInfoVO extends PcAuditInfo {
    @ApiModelProperty(value = "必填字段：角色id BD：装饰， SJ：设计")
    private String roleId;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
