package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Auther: jiang
 * @Date: 2019/1/16 16:09
 * @Description:
 */
@ApiModel(description = "实名认证信息")
public class DesignerCertificationVO {
    @ApiModelProperty("公司id")
    private String companyId;
    @ApiModelProperty("实名认证状态，1未认证，2已认证，3实名认证审核中，4审核不通过")
    private Integer authState;
    @ApiModelProperty("是否绑定公司 1:绑定 0:未绑定")
    private Integer isBindCompany;
    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Integer getAuthState() {
        return authState;
    }

    public void setAuthState(Integer authState) {
        this.authState = authState;
    }

    public Integer getIsBindCompany() {
        return isBindCompany;
    }

    public void setIsBindCompany(Integer isBindCompany) {
        this.isBindCompany = isBindCompany;
    }
}
