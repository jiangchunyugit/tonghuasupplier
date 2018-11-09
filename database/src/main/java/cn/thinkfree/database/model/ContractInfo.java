package cn.thinkfree.database.model;

import cn.thinkfree.core.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table pc_contract_info
 */
@ApiModel(value="cn.thinkfree.database.model.ContractInfo")
public class ContractInfo extends BaseModel {
    /**
     * Database Column Remarks:
     *   主键id
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_contract_info.id
     *
     * @mbg.generated
     */
    @ApiModelProperty(value="id主键id")
    private Integer id;

    /**
     * Database Column Remarks:
     *   合同编号
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_contract_info.contract_number
     *
     * @mbg.generated
     */
    @ApiModelProperty(value="contractNumber合同编号")
    private String contractNumber;

    /**
     * Database Column Remarks:
     *   0草稿 1待审批 2 审批通过 3 审批拒绝4待确认保证金5已确认保证金
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_contract_info.contract_status
     *
     * @mbg.generated
     */
    @ApiModelProperty(value="contractStatus0草稿 1待审批 2 审批通过 3 审批拒绝4待确认保证金5已确认保证金")
    private String contractStatus;

    /**
     * Database Column Remarks:
     *   签约时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_contract_info.signed_time
     *
     * @mbg.generated
     */
    @ApiModelProperty(value="signedTime签约时间")
    private Date signedTime;

    /**
     * Database Column Remarks:
     *   合同开始时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_contract_info.start_time
     *
     * @mbg.generated
     */
    @ApiModelProperty(value="startTime合同开始时间")
    private Date startTime;

    /**
     * Database Column Remarks:
     *   合同结束时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_contract_info.end_time
     *
     * @mbg.generated
     */
    @ApiModelProperty(value="endTime合同结束时间")
    private Date endTime;

    /**
     * Database Column Remarks:
     *   创建时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_contract_info.create_time
     *
     * @mbg.generated
     */
    @ApiModelProperty(value="createTime创建时间")
    private Date createTime;

    /**
     * Database Column Remarks:
     *   公司编号
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_contract_info.company_id
     *
     * @mbg.generated
     */
    @ApiModelProperty(value="companyId公司编号")
    private String companyId;

    /**
     * Database Column Remarks:
     *   合同附件地址url
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_contract_info.contract_url
     *
     * @mbg.generated
     */
    @ApiModelProperty(value="contractUrl合同附件地址url")
    private String contractUrl;

    /**
     * Database Column Remarks:
     *   合同备注
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_contract_info.contract_remark
     *
     * @mbg.generated
     */
    @ApiModelProperty(value="contractRemark合同备注")
    private String contractRemark;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_contract_info.id
     *
     * @return the value of pc_contract_info.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_contract_info.id
     *
     * @param id the value for pc_contract_info.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_contract_info.contract_number
     *
     * @return the value of pc_contract_info.contract_number
     *
     * @mbg.generated
     */
    public String getContractNumber() {
        return contractNumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_contract_info.contract_number
     *
     * @param contractNumber the value for pc_contract_info.contract_number
     *
     * @mbg.generated
     */
    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber == null ? null : contractNumber.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_contract_info.contract_status
     *
     * @return the value of pc_contract_info.contract_status
     *
     * @mbg.generated
     */
    public String getContractStatus() {
        return contractStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_contract_info.contract_status
     *
     * @param contractStatus the value for pc_contract_info.contract_status
     *
     * @mbg.generated
     */
    public void setContractStatus(String contractStatus) {
        this.contractStatus = contractStatus == null ? null : contractStatus.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_contract_info.signed_time
     *
     * @return the value of pc_contract_info.signed_time
     *
     * @mbg.generated
     */
    public Date getSignedTime() {
        return signedTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_contract_info.signed_time
     *
     * @param signedTime the value for pc_contract_info.signed_time
     *
     * @mbg.generated
     */
    public void setSignedTime(Date signedTime) {
        this.signedTime = signedTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_contract_info.start_time
     *
     * @return the value of pc_contract_info.start_time
     *
     * @mbg.generated
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_contract_info.start_time
     *
     * @param startTime the value for pc_contract_info.start_time
     *
     * @mbg.generated
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_contract_info.end_time
     *
     * @return the value of pc_contract_info.end_time
     *
     * @mbg.generated
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_contract_info.end_time
     *
     * @param endTime the value for pc_contract_info.end_time
     *
     * @mbg.generated
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_contract_info.create_time
     *
     * @return the value of pc_contract_info.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_contract_info.create_time
     *
     * @param createTime the value for pc_contract_info.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_contract_info.company_id
     *
     * @return the value of pc_contract_info.company_id
     *
     * @mbg.generated
     */
    public String getCompanyId() {
        return companyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_contract_info.company_id
     *
     * @param companyId the value for pc_contract_info.company_id
     *
     * @mbg.generated
     */
    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_contract_info.contract_url
     *
     * @return the value of pc_contract_info.contract_url
     *
     * @mbg.generated
     */
    public String getContractUrl() {
        return contractUrl;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_contract_info.contract_url
     *
     * @param contractUrl the value for pc_contract_info.contract_url
     *
     * @mbg.generated
     */
    public void setContractUrl(String contractUrl) {
        this.contractUrl = contractUrl == null ? null : contractUrl.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_contract_info.contract_remark
     *
     * @return the value of pc_contract_info.contract_remark
     *
     * @mbg.generated
     */
    public String getContractRemark() {
        return contractRemark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_contract_info.contract_remark
     *
     * @param contractRemark the value for pc_contract_info.contract_remark
     *
     * @mbg.generated
     */
    public void setContractRemark(String contractRemark) {
        this.contractRemark = contractRemark == null ? null : contractRemark.trim();
    }
}