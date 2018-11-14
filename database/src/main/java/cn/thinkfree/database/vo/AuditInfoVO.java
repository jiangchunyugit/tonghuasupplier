package cn.thinkfree.database.vo;

import cn.thinkfree.database.constants.CompanyAuditStatus;
import cn.thinkfree.database.model.PcAuditInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "审批信息")
public class AuditInfoVO extends PcAuditInfo {

    @ApiModelProperty(value = "公司入驻状态code")
    private String companyAuditType;

    @ApiModelProperty(value = "公司入驻状态name")
    private String companyAuditName;

    public String getCompanyAuditName() {
        return companyAuditName;
    }

    public void setCompanyAuditName(String companyAuditName) {
        this.companyAuditName = companyAuditName;
    }

    public String getCompanyAuditType() {
        return companyAuditType;
    }

    public void setCompanyAuditType(String companyAuditType) {
        this.companyAuditType = companyAuditType;
    }
}
