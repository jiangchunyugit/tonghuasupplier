package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.PcAuditInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("审批")
public class PcAuditInfoVO extends PcAuditInfo {
    @ApiModelProperty(value = "运营入驻审批时候的必填字段：角色id BD：装饰， SJ：设计")
    private String roleId;

    @ApiModelProperty(value = "运营资质变更审批的必填字段：申请id")
    private String applyId;

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
