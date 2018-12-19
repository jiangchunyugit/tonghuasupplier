package cn.thinkfree.service.platform.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author xusonghui
 * 查询该公司的员工
 */
public class CompanyStaffVo {
    @ApiModelProperty("员工真实姓名")
    private String realName;
    @ApiModelProperty("员工ID")
    private String userId;
    @ApiModelProperty("员工手机号")
    private String phone;
    @ApiModelProperty("角色编码")
    private String roleCode;
    @ApiModelProperty("项目总个数")
    private int projectCount;

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public int getProjectCount() {
        return projectCount;
    }

    public void setProjectCount(int projectCount) {
        this.projectCount = projectCount;
    }
}
