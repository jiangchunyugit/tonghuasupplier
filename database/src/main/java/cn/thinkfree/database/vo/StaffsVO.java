package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.CompanyUserSet;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "员工信息")
public class StaffsVO extends CompanyUserSet {

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
            statusName = "移除";
        }
        return statusName;
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
