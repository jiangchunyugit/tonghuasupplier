package cn.thinkfree.database.model;

import cn.thinkfree.core.model.BaseModel;
import java.util.Date;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table af_message_log
 */
public class ApprovalFlowMessageLog extends BaseModel {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column af_message_log.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     * Database Column Remarks:
     *   审批流实例编号
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column af_message_log.instance_num
     *
     * @mbg.generated
     */
    private String instanceNum;

    /**
     * Database Column Remarks:
     *   创建时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column af_message_log.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     * Database Column Remarks:
     *   项目编号
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column af_message_log.project_no
     *
     * @mbg.generated
     */
    private String projectNo;

    /**
     * Database Column Remarks:
     *   读取状态：0：未读；1：已读
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column af_message_log.read_state
     *
     * @mbg.generated
     */
    private Integer readState;

    /**
     * Database Column Remarks:
     *   读取时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column af_message_log.read_time
     *
     * @mbg.generated
     */
    private Date readTime;

    /**
     * Database Column Remarks:
     *   接受用户id
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column af_message_log.user_id
     *
     * @mbg.generated
     */
    private String userId;

    /**
     * Database Column Remarks:
     *   接受用户角色
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column af_message_log.role_id
     *
     * @mbg.generated
     */
    private String roleId;

    /**
     * Database Column Remarks:
     *   发送用户id
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column af_message_log.send_user_id
     *
     * @mbg.generated
     */
    private String sendUserId;

    /**
     * Database Column Remarks:
     *   发送用户角色id
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column af_message_log.send_role_id
     *
     * @mbg.generated
     */
    private String sendRoleId;

    /**
     * Database Column Remarks:
     *   排期编号
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column af_message_log.schedule_sort
     *
     * @mbg.generated
     */
    private Integer scheduleSort;

    /**
     * Database Column Remarks:
     *   发送消息
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column af_message_log.message
     *
     * @mbg.generated
     */
    private String message;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column af_message_log.id
     *
     * @return the value of af_message_log.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column af_message_log.id
     *
     * @param id the value for af_message_log.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column af_message_log.instance_num
     *
     * @return the value of af_message_log.instance_num
     *
     * @mbg.generated
     */
    public String getInstanceNum() {
        return instanceNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column af_message_log.instance_num
     *
     * @param instanceNum the value for af_message_log.instance_num
     *
     * @mbg.generated
     */
    public void setInstanceNum(String instanceNum) {
        this.instanceNum = instanceNum == null ? null : instanceNum.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column af_message_log.create_time
     *
     * @return the value of af_message_log.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column af_message_log.create_time
     *
     * @param createTime the value for af_message_log.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column af_message_log.project_no
     *
     * @return the value of af_message_log.project_no
     *
     * @mbg.generated
     */
    public String getProjectNo() {
        return projectNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column af_message_log.project_no
     *
     * @param projectNo the value for af_message_log.project_no
     *
     * @mbg.generated
     */
    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo == null ? null : projectNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column af_message_log.read_state
     *
     * @return the value of af_message_log.read_state
     *
     * @mbg.generated
     */
    public Integer getReadState() {
        return readState;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column af_message_log.read_state
     *
     * @param readState the value for af_message_log.read_state
     *
     * @mbg.generated
     */
    public void setReadState(Integer readState) {
        this.readState = readState;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column af_message_log.read_time
     *
     * @return the value of af_message_log.read_time
     *
     * @mbg.generated
     */
    public Date getReadTime() {
        return readTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column af_message_log.read_time
     *
     * @param readTime the value for af_message_log.read_time
     *
     * @mbg.generated
     */
    public void setReadTime(Date readTime) {
        this.readTime = readTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column af_message_log.user_id
     *
     * @return the value of af_message_log.user_id
     *
     * @mbg.generated
     */
    public String getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column af_message_log.user_id
     *
     * @param userId the value for af_message_log.user_id
     *
     * @mbg.generated
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column af_message_log.role_id
     *
     * @return the value of af_message_log.role_id
     *
     * @mbg.generated
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column af_message_log.role_id
     *
     * @param roleId the value for af_message_log.role_id
     *
     * @mbg.generated
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId == null ? null : roleId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column af_message_log.send_user_id
     *
     * @return the value of af_message_log.send_user_id
     *
     * @mbg.generated
     */
    public String getSendUserId() {
        return sendUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column af_message_log.send_user_id
     *
     * @param sendUserId the value for af_message_log.send_user_id
     *
     * @mbg.generated
     */
    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId == null ? null : sendUserId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column af_message_log.send_role_id
     *
     * @return the value of af_message_log.send_role_id
     *
     * @mbg.generated
     */
    public String getSendRoleId() {
        return sendRoleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column af_message_log.send_role_id
     *
     * @param sendRoleId the value for af_message_log.send_role_id
     *
     * @mbg.generated
     */
    public void setSendRoleId(String sendRoleId) {
        this.sendRoleId = sendRoleId == null ? null : sendRoleId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column af_message_log.schedule_sort
     *
     * @return the value of af_message_log.schedule_sort
     *
     * @mbg.generated
     */
    public Integer getScheduleSort() {
        return scheduleSort;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column af_message_log.schedule_sort
     *
     * @param scheduleSort the value for af_message_log.schedule_sort
     *
     * @mbg.generated
     */
    public void setScheduleSort(Integer scheduleSort) {
        this.scheduleSort = scheduleSort;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column af_message_log.message
     *
     * @return the value of af_message_log.message
     *
     * @mbg.generated
     */
    public String getMessage() {
        return message;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column af_message_log.message
     *
     * @param message the value for af_message_log.message
     *
     * @mbg.generated
     */
    public void setMessage(String message) {
        this.message = message == null ? null : message.trim();
    }
}