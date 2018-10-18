package cn.thinkfree.database.model;

import cn.thinkfree.core.model.BaseModel;
import java.util.Date;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table approval_flow_approval_log
 */
public class ApprovalFlowApprovalLog extends BaseModel {
    /**
     * Database Column Remarks:
     *   id
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column approval_flow_approval_log.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     * Database Column Remarks:
     *   编号
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column approval_flow_approval_log.num
     *
     * @mbg.generated
     */
    private String num;

    /**
     * Database Column Remarks:
     *   审批流实例编号
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column approval_flow_approval_log.instance_num
     *
     * @mbg.generated
     */
    private String instanceNum;

    /**
     * Database Column Remarks:
     *   审批流节点编号
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column approval_flow_approval_log.node_num
     *
     * @mbg.generated
     */
    private String nodeNum;

    /**
     * Database Column Remarks:
     *   角色Id
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column approval_flow_approval_log.role_id
     *
     * @mbg.generated
     */
    private String roleId;

    /**
     * Database Column Remarks:
     *   操作项编号
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column approval_flow_approval_log.option_num
     *
     * @mbg.generated
     */
    private String optionNum;

    /**
     * Database Column Remarks:
     *   操作项描述
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column approval_flow_approval_log.option_msg
     *
     * @mbg.generated
     */
    private String optionMsg;

    /**
     * Database Column Remarks:
     *   是否有效，1是，2否；审批被打回时，当前步骤之后的数据失效
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column approval_flow_approval_log.is_invalid
     *
     * @mbg.generated
     */
    private Integer isInvalid;

    /**
     * Database Column Remarks:
     *   审批时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column approval_flow_approval_log.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     * Database Column Remarks:
     *   审批备注
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column approval_flow_approval_log.remark
     *
     * @mbg.generated
     */
    private String remark;

    /**
     * Database Column Remarks:
     *   用户Id
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column approval_flow_approval_log.user_id
     *
     * @mbg.generated
     */
    private String userId;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column approval_flow_approval_log.id
     *
     * @return the value of approval_flow_approval_log.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column approval_flow_approval_log.id
     *
     * @param id the value for approval_flow_approval_log.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column approval_flow_approval_log.num
     *
     * @return the value of approval_flow_approval_log.num
     *
     * @mbg.generated
     */
    public String getNum() {
        return num;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column approval_flow_approval_log.num
     *
     * @param num the value for approval_flow_approval_log.num
     *
     * @mbg.generated
     */
    public void setNum(String num) {
        this.num = num == null ? null : num.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column approval_flow_approval_log.instance_num
     *
     * @return the value of approval_flow_approval_log.instance_num
     *
     * @mbg.generated
     */
    public String getInstanceNum() {
        return instanceNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column approval_flow_approval_log.instance_num
     *
     * @param instanceNum the value for approval_flow_approval_log.instance_num
     *
     * @mbg.generated
     */
    public void setInstanceNum(String instanceNum) {
        this.instanceNum = instanceNum == null ? null : instanceNum.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column approval_flow_approval_log.node_num
     *
     * @return the value of approval_flow_approval_log.node_num
     *
     * @mbg.generated
     */
    public String getNodeNum() {
        return nodeNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column approval_flow_approval_log.node_num
     *
     * @param nodeNum the value for approval_flow_approval_log.node_num
     *
     * @mbg.generated
     */
    public void setNodeNum(String nodeNum) {
        this.nodeNum = nodeNum == null ? null : nodeNum.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column approval_flow_approval_log.role_id
     *
     * @return the value of approval_flow_approval_log.role_id
     *
     * @mbg.generated
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column approval_flow_approval_log.role_id
     *
     * @param roleId the value for approval_flow_approval_log.role_id
     *
     * @mbg.generated
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId == null ? null : roleId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column approval_flow_approval_log.option_num
     *
     * @return the value of approval_flow_approval_log.option_num
     *
     * @mbg.generated
     */
    public String getOptionNum() {
        return optionNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column approval_flow_approval_log.option_num
     *
     * @param optionNum the value for approval_flow_approval_log.option_num
     *
     * @mbg.generated
     */
    public void setOptionNum(String optionNum) {
        this.optionNum = optionNum == null ? null : optionNum.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column approval_flow_approval_log.option_msg
     *
     * @return the value of approval_flow_approval_log.option_msg
     *
     * @mbg.generated
     */
    public String getOptionMsg() {
        return optionMsg;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column approval_flow_approval_log.option_msg
     *
     * @param optionMsg the value for approval_flow_approval_log.option_msg
     *
     * @mbg.generated
     */
    public void setOptionMsg(String optionMsg) {
        this.optionMsg = optionMsg == null ? null : optionMsg.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column approval_flow_approval_log.is_invalid
     *
     * @return the value of approval_flow_approval_log.is_invalid
     *
     * @mbg.generated
     */
    public Integer getIsInvalid() {
        return isInvalid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column approval_flow_approval_log.is_invalid
     *
     * @param isInvalid the value for approval_flow_approval_log.is_invalid
     *
     * @mbg.generated
     */
    public void setIsInvalid(Integer isInvalid) {
        this.isInvalid = isInvalid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column approval_flow_approval_log.create_time
     *
     * @return the value of approval_flow_approval_log.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column approval_flow_approval_log.create_time
     *
     * @param createTime the value for approval_flow_approval_log.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column approval_flow_approval_log.remark
     *
     * @return the value of approval_flow_approval_log.remark
     *
     * @mbg.generated
     */
    public String getRemark() {
        return remark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column approval_flow_approval_log.remark
     *
     * @param remark the value for approval_flow_approval_log.remark
     *
     * @mbg.generated
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column approval_flow_approval_log.user_id
     *
     * @return the value of approval_flow_approval_log.user_id
     *
     * @mbg.generated
     */
    public String getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column approval_flow_approval_log.user_id
     *
     * @param userId the value for approval_flow_approval_log.user_id
     *
     * @mbg.generated
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }
}