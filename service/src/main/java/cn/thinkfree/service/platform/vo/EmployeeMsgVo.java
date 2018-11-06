package cn.thinkfree.service.platform.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author xusonghui
 * 员工信息
 */
public class EmployeeMsgVo {

    @ApiModelProperty("所属公司名称")
    private String companyName;

    @ApiModelProperty("用户真实姓名")
    private String realName;

    @ApiModelProperty("用户所属角色名称")
    private String roleName;

    @ApiModelProperty("用户所属角色编码")
    private String roleCode;

    @ApiModelProperty("绑定公司状态，1未绑定，2已绑定，3实名认证审核中，4审核不通过")
    private int bindCompanyState;

    @ApiModelProperty("实名认证状态，1未认证，2已认证，3实名认证审核中，4审核不通过")
    private int authState;

    @ApiModelProperty("用户手机号")
    private String phone;

    @ApiModelProperty("用户头像地址")
    private String iconUrl;

    @ApiModelProperty("用户ID")
    private String userId;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public int getBindCompanyState() {
        return bindCompanyState;
    }

    public void setBindCompanyState(int bindCompanyState) {
        this.bindCompanyState = bindCompanyState;
    }

    public int getAuthState() {
        return authState;
    }

    public void setAuthState(int authState) {
        this.authState = authState;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
