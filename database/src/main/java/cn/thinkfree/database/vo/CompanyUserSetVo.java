package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.CompanyUserSet;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("员工信息表")
public class CompanyUserSetVo extends CompanyUserSet {
    @ApiModelProperty("岗位名称")
    private String roleName;

    @ApiModelProperty("公司名称")
    private String companyName;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
