package cn.thinkfree.database.model;

import cn.thinkfree.core.model.BaseModel;
import java.util.Date;

/**
 * Database Table Remarks:
 *   审批流配置
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table approval_flow_config
 */
public class ApprovalFlowConfig extends BaseModel {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column approval_flow_config.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column approval_flow_config.name
     *
     * @mbg.generated
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column approval_flow_config.num
     *
     * @mbg.generated
     */
    private String num;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column approval_flow_config.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column approval_flow_config.create_user_id
     *
     * @mbg.generated
     */
    private String createUserId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column approval_flow_config.effective_time
     *
     * @mbg.generated
     */
    private Date effectiveTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column approval_flow_config.update_time
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column approval_flow_config.update_user_id
     *
     * @mbg.generated
     */
    private String updateUserId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column approval_flow_config.version
     *
     * @mbg.generated
     */
    private Integer version;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column approval_flow_config.h5_link
     *
     * @mbg.generated
     */
    private String h5Link;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column approval_flow_config.h5_resume
     *
     * @mbg.generated
     */
    private String h5Resume;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column approval_flow_config.type
     *
     * @mbg.generated
     */
    private Integer type;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column approval_flow_config.sort
     *
     * @mbg.generated
     */
    private Integer sort;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column approval_flow_config.company_num
     *
     * @mbg.generated
     */
    private String companyNum;

    /**
     * Database Column Remarks:
     *   英文简称
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column approval_flow_config.alias
     *
     * @mbg.generated
     */
    private String alias;

    /**
     * Database Column Remarks:
     *   是否是基础配置，0：不是；1：是
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column approval_flow_config.is_base
     *
     * @mbg.generated
     */
    private Short isBase;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column approval_flow_config.id
     *
     * @return the value of approval_flow_config.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column approval_flow_config.id
     *
     * @param id the value for approval_flow_config.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column approval_flow_config.name
     *
     * @return the value of approval_flow_config.name
     *
     * @mbg.generated
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column approval_flow_config.name
     *
     * @param name the value for approval_flow_config.name
     *
     * @mbg.generated
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column approval_flow_config.num
     *
     * @return the value of approval_flow_config.num
     *
     * @mbg.generated
     */
    public String getNum() {
        return num;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column approval_flow_config.num
     *
     * @param num the value for approval_flow_config.num
     *
     * @mbg.generated
     */
    public void setNum(String num) {
        this.num = num == null ? null : num.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column approval_flow_config.create_time
     *
     * @return the value of approval_flow_config.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column approval_flow_config.create_time
     *
     * @param createTime the value for approval_flow_config.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column approval_flow_config.create_user_id
     *
     * @return the value of approval_flow_config.create_user_id
     *
     * @mbg.generated
     */
    public String getCreateUserId() {
        return createUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column approval_flow_config.create_user_id
     *
     * @param createUserId the value for approval_flow_config.create_user_id
     *
     * @mbg.generated
     */
    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId == null ? null : createUserId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column approval_flow_config.effective_time
     *
     * @return the value of approval_flow_config.effective_time
     *
     * @mbg.generated
     */
    public Date getEffectiveTime() {
        return effectiveTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column approval_flow_config.effective_time
     *
     * @param effectiveTime the value for approval_flow_config.effective_time
     *
     * @mbg.generated
     */
    public void setEffectiveTime(Date effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column approval_flow_config.update_time
     *
     * @return the value of approval_flow_config.update_time
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column approval_flow_config.update_time
     *
     * @param updateTime the value for approval_flow_config.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column approval_flow_config.update_user_id
     *
     * @return the value of approval_flow_config.update_user_id
     *
     * @mbg.generated
     */
    public String getUpdateUserId() {
        return updateUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column approval_flow_config.update_user_id
     *
     * @param updateUserId the value for approval_flow_config.update_user_id
     *
     * @mbg.generated
     */
    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId == null ? null : updateUserId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column approval_flow_config.version
     *
     * @return the value of approval_flow_config.version
     *
     * @mbg.generated
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column approval_flow_config.version
     *
     * @param version the value for approval_flow_config.version
     *
     * @mbg.generated
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column approval_flow_config.h5_link
     *
     * @return the value of approval_flow_config.h5_link
     *
     * @mbg.generated
     */
    public String getH5Link() {
        return h5Link;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column approval_flow_config.h5_link
     *
     * @param h5Link the value for approval_flow_config.h5_link
     *
     * @mbg.generated
     */
    public void setH5Link(String h5Link) {
        this.h5Link = h5Link == null ? null : h5Link.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column approval_flow_config.h5_resume
     *
     * @return the value of approval_flow_config.h5_resume
     *
     * @mbg.generated
     */
    public String getH5Resume() {
        return h5Resume;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column approval_flow_config.h5_resume
     *
     * @param h5Resume the value for approval_flow_config.h5_resume
     *
     * @mbg.generated
     */
    public void setH5Resume(String h5Resume) {
        this.h5Resume = h5Resume == null ? null : h5Resume.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column approval_flow_config.type
     *
     * @return the value of approval_flow_config.type
     *
     * @mbg.generated
     */
    public Integer getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column approval_flow_config.type
     *
     * @param type the value for approval_flow_config.type
     *
     * @mbg.generated
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column approval_flow_config.sort
     *
     * @return the value of approval_flow_config.sort
     *
     * @mbg.generated
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column approval_flow_config.sort
     *
     * @param sort the value for approval_flow_config.sort
     *
     * @mbg.generated
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column approval_flow_config.company_num
     *
     * @return the value of approval_flow_config.company_num
     *
     * @mbg.generated
     */
    public String getCompanyNum() {
        return companyNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column approval_flow_config.company_num
     *
     * @param companyNum the value for approval_flow_config.company_num
     *
     * @mbg.generated
     */
    public void setCompanyNum(String companyNum) {
        this.companyNum = companyNum == null ? null : companyNum.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column approval_flow_config.alias
     *
     * @return the value of approval_flow_config.alias
     *
     * @mbg.generated
     */
    public String getAlias() {
        return alias;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column approval_flow_config.alias
     *
     * @param alias the value for approval_flow_config.alias
     *
     * @mbg.generated
     */
    public void setAlias(String alias) {
        this.alias = alias == null ? null : alias.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column approval_flow_config.is_base
     *
     * @return the value of approval_flow_config.is_base
     *
     * @mbg.generated
     */
    public Short getIsBase() {
        return isBase;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column approval_flow_config.is_base
     *
     * @param isBase the value for approval_flow_config.is_base
     *
     * @mbg.generated
     */
    public void setIsBase(Short isBase) {
        this.isBase = isBase;
    }
}