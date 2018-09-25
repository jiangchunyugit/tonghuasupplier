package cn.thinkfree.database.model;

import cn.thinkfree.core.model.BaseModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table pc_contract_template
 */
public class PcContractTemplate extends BaseModel {
    /**
     * Database Column Remarks:
     *   主键id
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_contract_template.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     * Database Column Remarks:
     *   合同类型
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_contract_template.contract_tp_type
     *
     * @mbg.generated
     */
    private String contractTpType;

    /**
     * Database Column Remarks:
     *   合同名称
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_contract_template.contract_tp_name
     *
     * @mbg.generated
     */
    private String contractTpName;

    /**
     * Database Column Remarks:
     *   合同备注
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_contract_template.contract_tp_remark
     *
     * @mbg.generated
     */
    private String contractTpRemark;

    /**
     * Database Column Remarks:
     *   上传时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_contract_template.upload_time 
     *
     * @mbg.generated
     */
    private Date uploadTime;

    /**
     * Database Column Remarks:
     *   附件url
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_contract_template.upload_url
     *
     * @mbg.generated
     */
    private String uploadUrl;

    /**
     * Database Column Remarks:
     *   创建时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_contract_template.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     * Database Column Remarks:
     *   修改时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_contract_template.update_time
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_contract_template.id
     *
     * @return the value of pc_contract_template.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_contract_template.id
     *
     * @param id the value for pc_contract_template.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_contract_template.contract_tp_type
     *
     * @return the value of pc_contract_template.contract_tp_type
     *
     * @mbg.generated
     */
    public String getContractTpType() {
        return contractTpType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_contract_template.contract_tp_type
     *
     * @param contractTpType the value for pc_contract_template.contract_tp_type
     *
     * @mbg.generated
     */
    public void setContractTpType(String contractTpType) {
        this.contractTpType = contractTpType == null ? null : contractTpType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_contract_template.contract_tp_name
     *
     * @return the value of pc_contract_template.contract_tp_name
     *
     * @mbg.generated
     */
    public String getContractTpName() {
        return contractTpName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_contract_template.contract_tp_name
     *
     * @param contractTpName the value for pc_contract_template.contract_tp_name
     *
     * @mbg.generated
     */
    public void setContractTpName(String contractTpName) {
        this.contractTpName = contractTpName == null ? null : contractTpName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_contract_template.contract_tp_remark
     *
     * @return the value of pc_contract_template.contract_tp_remark
     *
     * @mbg.generated
     */
    public String getContractTpRemark() {
        return contractTpRemark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_contract_template.contract_tp_remark
     *
     * @param contractTpRemark the value for pc_contract_template.contract_tp_remark
     *
     * @mbg.generated
     */
    public void setContractTpRemark(String contractTpRemark) {
        this.contractTpRemark = contractTpRemark == null ? null : contractTpRemark.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_contract_template.upload_time 
     *
     * @return the value of pc_contract_template.upload_time 
     *
     * @mbg.generated
     */
    public String getUploadTime() {
    	 DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        return format1.format(uploadTime);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_contract_template.upload_time 
     *
     * @param uploadTime the value for pc_contract_template.upload_time 
     *
     * @mbg.generated
     */
    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_contract_template.upload_url
     *
     * @return the value of pc_contract_template.upload_url
     *
     * @mbg.generated
     */
    public String getUploadUrl() {
        return uploadUrl;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_contract_template.upload_url
     *
     * @param uploadUrl the value for pc_contract_template.upload_url
     *
     * @mbg.generated
     */
    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl == null ? null : uploadUrl.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_contract_template.create_time
     *
     * @return the value of pc_contract_template.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_contract_template.create_time
     *
     * @param createTime the value for pc_contract_template.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_contract_template.update_time
     *
     * @return the value of pc_contract_template.update_time
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_contract_template.update_time
     *
     * @param updateTime the value for pc_contract_template.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}