package cn.thinkfree.service.platform.vo;

import cn.thinkfree.database.model.Project;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author xusonghui
 * 员工信息和项目信息对象
 */
public class EmployeeAndProjectMsgVo {
    @ApiModelProperty("员工真实姓名")
    private String realName;
    @ApiModelProperty("角色名称")
    private String roleName;
    @ApiModelProperty("角色编码")
    private String roleCode;
    @ApiModelProperty("项目总个数")
    private int sumCount;
    @ApiModelProperty("用户ID")
    private String userId;
    @ApiModelProperty("项目列表")
    private List<ProjectSummaryMsgVo> projects;

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

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public int getSumCount() {
        return sumCount;
    }

    public void setSumCount(int sumCount) {
        this.sumCount = sumCount;
    }

    public void setProjects(List<ProjectSummaryMsgVo> projects) {
        this.projects = projects;
    }

    public List<ProjectSummaryMsgVo> getProjects() {
        return projects;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleCode() {
        return roleCode;
    }
}
