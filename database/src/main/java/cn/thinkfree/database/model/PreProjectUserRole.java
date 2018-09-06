package cn.thinkfree.database.model;

import cn.thinkfree.core.model.BaseModel;
import java.util.Date;

/**
 * Database Table Remarks:
 *   项目角色设置表
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table pre_project_user_role
 */
public class PreProjectUserRole extends BaseModel {
    /**
     * Database Column Remarks:
     *   自增id
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pre_project_user_role.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     * Database Column Remarks:
     *   项目编号
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pre_project_user_role.project_no
     *
     * @mbg.generated
     */
    private String projectNo;

    /**
     * Database Column Remarks:
     *   角色ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pre_project_user_role.role_id
     *
     * @mbg.generated
     */
    private String roleId;

    /**
     * Database Column Remarks:
     *   用户ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pre_project_user_role.user_id
     *
     * @mbg.generated
     */
    private String userId;

    /**
     * Database Column Remarks:
     *   创建时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pre_project_user_role.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     * Database Column Remarks:
     *   是否在职 1是 0否
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pre_project_user_role.is_job
     *
     * @mbg.generated
     */
    private Short isJob;

    /**
     * Database Column Remarks:
     *   是否移交给他人 1是 0否
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pre_project_user_role.is_transfer
     *
     * @mbg.generated
     */
    private Short isTransfer;

    /**
     * Database Column Remarks:
     *   移交员工id
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pre_project_user_role.transfer_user_id
     *
     * @mbg.generated
     */
    private String transferUserId;

    /**
     * Database Column Remarks:
     *   移交时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pre_project_user_role.transfer_time
     *
     * @mbg.generated
     */
    private Date transferTime;

    /**
     * Database Column Remarks:
     *   更新时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pre_project_user_role.update_time
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     * Database Column Remarks:
     *   离职时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pre_project_user_role.leave_time
     *
     * @mbg.generated
     */
    private Date leaveTime;

    /**
     * Database Column Remarks:
     *   姓名
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pre_project_user_role.user_name
     *
     * @mbg.generated
     */
    private String userName;

    /**
     * Database Column Remarks:
     *   角色名称
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pre_project_user_role.role_name
     *
     * @mbg.generated
     */
    private String roleName;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pre_project_user_role.id
     *
     * @return the value of pre_project_user_role.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pre_project_user_role.id
     *
     * @param id the value for pre_project_user_role.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pre_project_user_role.project_no
     *
     * @return the value of pre_project_user_role.project_no
     *
     * @mbg.generated
     */
    public String getProjectNo() {
        return projectNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pre_project_user_role.project_no
     *
     * @param projectNo the value for pre_project_user_role.project_no
     *
     * @mbg.generated
     */
    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo == null ? null : projectNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pre_project_user_role.role_id
     *
     * @return the value of pre_project_user_role.role_id
     *
     * @mbg.generated
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pre_project_user_role.role_id
     *
     * @param roleId the value for pre_project_user_role.role_id
     *
     * @mbg.generated
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId == null ? null : roleId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pre_project_user_role.user_id
     *
     * @return the value of pre_project_user_role.user_id
     *
     * @mbg.generated
     */
    public String getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pre_project_user_role.user_id
     *
     * @param userId the value for pre_project_user_role.user_id
     *
     * @mbg.generated
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pre_project_user_role.create_time
     *
     * @return the value of pre_project_user_role.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pre_project_user_role.create_time
     *
     * @param createTime the value for pre_project_user_role.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pre_project_user_role.is_job
     *
     * @return the value of pre_project_user_role.is_job
     *
     * @mbg.generated
     */
    public Short getIsJob() {
        return isJob;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pre_project_user_role.is_job
     *
     * @param isJob the value for pre_project_user_role.is_job
     *
     * @mbg.generated
     */
    public void setIsJob(Short isJob) {
        this.isJob = isJob;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pre_project_user_role.is_transfer
     *
     * @return the value of pre_project_user_role.is_transfer
     *
     * @mbg.generated
     */
    public Short getIsTransfer() {
        return isTransfer;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pre_project_user_role.is_transfer
     *
     * @param isTransfer the value for pre_project_user_role.is_transfer
     *
     * @mbg.generated
     */
    public void setIsTransfer(Short isTransfer) {
        this.isTransfer = isTransfer;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pre_project_user_role.transfer_user_id
     *
     * @return the value of pre_project_user_role.transfer_user_id
     *
     * @mbg.generated
     */
    public String getTransferUserId() {
        return transferUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pre_project_user_role.transfer_user_id
     *
     * @param transferUserId the value for pre_project_user_role.transfer_user_id
     *
     * @mbg.generated
     */
    public void setTransferUserId(String transferUserId) {
        this.transferUserId = transferUserId == null ? null : transferUserId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pre_project_user_role.transfer_time
     *
     * @return the value of pre_project_user_role.transfer_time
     *
     * @mbg.generated
     */
    public Date getTransferTime() {
        return transferTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pre_project_user_role.transfer_time
     *
     * @param transferTime the value for pre_project_user_role.transfer_time
     *
     * @mbg.generated
     */
    public void setTransferTime(Date transferTime) {
        this.transferTime = transferTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pre_project_user_role.update_time
     *
     * @return the value of pre_project_user_role.update_time
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pre_project_user_role.update_time
     *
     * @param updateTime the value for pre_project_user_role.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pre_project_user_role.leave_time
     *
     * @return the value of pre_project_user_role.leave_time
     *
     * @mbg.generated
     */
    public Date getLeaveTime() {
        return leaveTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pre_project_user_role.leave_time
     *
     * @param leaveTime the value for pre_project_user_role.leave_time
     *
     * @mbg.generated
     */
    public void setLeaveTime(Date leaveTime) {
        this.leaveTime = leaveTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pre_project_user_role.user_name
     *
     * @return the value of pre_project_user_role.user_name
     *
     * @mbg.generated
     */
    public String getUserName() {
        return userName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pre_project_user_role.user_name
     *
     * @param userName the value for pre_project_user_role.user_name
     *
     * @mbg.generated
     */
    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pre_project_user_role.role_name
     *
     * @return the value of pre_project_user_role.role_name
     *
     * @mbg.generated
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pre_project_user_role.role_name
     *
     * @param roleName the value for pre_project_user_role.role_name
     *
     * @mbg.generated
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }
}