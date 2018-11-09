package cn.thinkfree.database.vo.account;

import cn.thinkfree.database.vo.AbsPageSearchCriteria;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("账号搜索条件")
public class AccountSEO extends AbsPageSearchCriteria {

    /**
     * 分公司
     */
    @ApiModelProperty("分公司")
    private String branchCompany;
    /**
     * 分站
     */
    @ApiModelProperty("分站")
    private String cityBranch;

    /**
     * 部门
     */
    @ApiModelProperty("部门")
    private String dept;

    /**
     * 名称或手机号
     */
    @ApiModelProperty("名称或手机号")
    private String name;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private String state;

    public String getBranchCompany() {
        return branchCompany;
    }

    public void setBranchCompany(String branchCompany) {
        this.branchCompany = branchCompany;
    }

    public String getCityBranch() {
        return cityBranch;
    }

    public void setCityBranch(String cityBranch) {
        this.cityBranch = cityBranch;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
