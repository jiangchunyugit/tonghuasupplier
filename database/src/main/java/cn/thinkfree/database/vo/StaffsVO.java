package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.CompanyUserSet;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "员工信息")
public class StaffsVO extends CompanyUserSet {
    @ApiModelProperty("岗位名称")
    private String roleName;

    @ApiModelProperty("状态")
    private String statusName;

    @ApiModelProperty("公司名称")
    private String companyName;

    public String getStatusName() {
        if(getIsBind() == 1){
            statusName = "正常";
        }
        if(getIsBind() == 2){
            statusName = "待激活";
        }
        if(getIsJob() == 2){
            statusName = "被移除";
        }
        return statusName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
