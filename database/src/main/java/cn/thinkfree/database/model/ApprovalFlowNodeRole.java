package cn.thinkfree.database.model;

import cn.thinkfree.core.model.BaseModel;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table af_node_role
 */
public class ApprovalFlowNodeRole extends BaseModel {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column af_node_role.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     * Database Column Remarks:
     *   关联approval_flow_node表中的数据唯一编码
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column af_node_role.node_num
     *
     * @mbg.generated
     */
    private String nodeNum;

    /**
     * Database Column Remarks:
     *   角色编码
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column af_node_role.role_id
     *
     * @mbg.generated
     */
    private String roleId;

    /**
     * Database Column Remarks:
     *   类型，0是审批角色，1接收通知的角色
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column af_node_role.type
     *
     * @mbg.generated
     */
    private Integer type;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column af_node_role.id
     *
     * @return the value of af_node_role.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column af_node_role.id
     *
     * @param id the value for af_node_role.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column af_node_role.node_num
     *
     * @return the value of af_node_role.node_num
     *
     * @mbg.generated
     */
    public String getNodeNum() {
        return nodeNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column af_node_role.node_num
     *
     * @param nodeNum the value for af_node_role.node_num
     *
     * @mbg.generated
     */
    public void setNodeNum(String nodeNum) {
        this.nodeNum = nodeNum == null ? null : nodeNum.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column af_node_role.role_id
     *
     * @return the value of af_node_role.role_id
     *
     * @mbg.generated
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column af_node_role.role_id
     *
     * @param roleId the value for af_node_role.role_id
     *
     * @mbg.generated
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId == null ? null : roleId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column af_node_role.type
     *
     * @return the value of af_node_role.type
     *
     * @mbg.generated
     */
    public Integer getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column af_node_role.type
     *
     * @param type the value for af_node_role.type
     *
     * @mbg.generated
     */
    public void setType(Integer type) {
        this.type = type;
    }
}