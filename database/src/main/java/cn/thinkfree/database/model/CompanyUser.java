package cn.thinkfree.database.model;

import cn.thinkfree.core.model.BaseModel;
import java.util.Date;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table pc_company_user
 */
public class CompanyUser extends BaseModel {
    /**
     * Database Column Remarks:
     *   主键
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_company_user.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     * Database Column Remarks:
     *   员工姓名
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_company_user.emp_name
     *
     * @mbg.generated
     */
    private String empName;

    /**
     * Database Column Remarks:
     *   员工编号
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_company_user.emp_number
     *
     * @mbg.generated
     */
    private String empNumber;

    /**
     * Database Column Remarks:
     *   是否在职 0 离职 1在职
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_company_user.is_job
     *
     * @mbg.generated
     */
    private String isJob;

    /**
     * Database Column Remarks:
     *   手机号
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_company_user.mobile
     *
     * @mbg.generated
     */
    private String mobile;

    /**
     * Database Column Remarks:
     *   身份证号
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_company_user.card_no
     *
     * @mbg.generated
     */
    private String cardNo;

    /**
     * Database Column Remarks:
     *   邮箱
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_company_user.email
     *
     * @mbg.generated
     */
    private String email;

    /**
     * Database Column Remarks:
     *   创建时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_company_user.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     * Database Column Remarks:
     *   最近登陆时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_company_user.login_time
     *
     * @mbg.generated
     */
    private Date loginTime;

    /**
     * Database Column Remarks:
     *   修改时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_company_user.update_time
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     * Database Column Remarks:
     *   备注
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_company_user.remark
     *
     * @mbg.generated
     */
    private String remark;

    /**
     * Database Column Remarks:
     *   所属公司
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_company_user.company_id
     *
     * @mbg.generated
     */
    private String companyId;

    /**
     * Database Column Remarks:
     *   员工状态 1在职0离职
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_company_user.status
     *
     * @mbg.generated
     */
    private String status;

    /**
     * Database Column Remarks:
     *   头像
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_company_user.photo_url
     *
     * @mbg.generated
     */
    private String photoUrl;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_company_user.id
     *
     * @return the value of pc_company_user.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_company_user.id
     *
     * @param id the value for pc_company_user.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_company_user.emp_name
     *
     * @return the value of pc_company_user.emp_name
     *
     * @mbg.generated
     */
    public String getEmpName() {
        return empName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_company_user.emp_name
     *
     * @param empName the value for pc_company_user.emp_name
     *
     * @mbg.generated
     */
    public void setEmpName(String empName) {
        this.empName = empName == null ? null : empName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_company_user.emp_number
     *
     * @return the value of pc_company_user.emp_number
     *
     * @mbg.generated
     */
    public String getEmpNumber() {
        return empNumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_company_user.emp_number
     *
     * @param empNumber the value for pc_company_user.emp_number
     *
     * @mbg.generated
     */
    public void setEmpNumber(String empNumber) {
        this.empNumber = empNumber == null ? null : empNumber.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_company_user.is_job
     *
     * @return the value of pc_company_user.is_job
     *
     * @mbg.generated
     */
    public String getIsJob() {
        return isJob;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_company_user.is_job
     *
     * @param isJob the value for pc_company_user.is_job
     *
     * @mbg.generated
     */
    public void setIsJob(String isJob) {
        this.isJob = isJob == null ? null : isJob.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_company_user.mobile
     *
     * @return the value of pc_company_user.mobile
     *
     * @mbg.generated
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_company_user.mobile
     *
     * @param mobile the value for pc_company_user.mobile
     *
     * @mbg.generated
     */
    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_company_user.card_no
     *
     * @return the value of pc_company_user.card_no
     *
     * @mbg.generated
     */
    public String getCardNo() {
        return cardNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_company_user.card_no
     *
     * @param cardNo the value for pc_company_user.card_no
     *
     * @mbg.generated
     */
    public void setCardNo(String cardNo) {
        this.cardNo = cardNo == null ? null : cardNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_company_user.email
     *
     * @return the value of pc_company_user.email
     *
     * @mbg.generated
     */
    public String getEmail() {
        return email;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_company_user.email
     *
     * @param email the value for pc_company_user.email
     *
     * @mbg.generated
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_company_user.create_time
     *
     * @return the value of pc_company_user.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_company_user.create_time
     *
     * @param createTime the value for pc_company_user.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_company_user.login_time
     *
     * @return the value of pc_company_user.login_time
     *
     * @mbg.generated
     */
    public Date getLoginTime() {
        return loginTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_company_user.login_time
     *
     * @param loginTime the value for pc_company_user.login_time
     *
     * @mbg.generated
     */
    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_company_user.update_time
     *
     * @return the value of pc_company_user.update_time
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_company_user.update_time
     *
     * @param updateTime the value for pc_company_user.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_company_user.remark
     *
     * @return the value of pc_company_user.remark
     *
     * @mbg.generated
     */
    public String getRemark() {
        return remark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_company_user.remark
     *
     * @param remark the value for pc_company_user.remark
     *
     * @mbg.generated
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_company_user.company_id
     *
     * @return the value of pc_company_user.company_id
     *
     * @mbg.generated
     */
    public String getCompanyId() {
        return companyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_company_user.company_id
     *
     * @param companyId the value for pc_company_user.company_id
     *
     * @mbg.generated
     */
    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_company_user.status
     *
     * @return the value of pc_company_user.status
     *
     * @mbg.generated
     */
    public String getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_company_user.status
     *
     * @param status the value for pc_company_user.status
     *
     * @mbg.generated
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_company_user.photo_url
     *
     * @return the value of pc_company_user.photo_url
     *
     * @mbg.generated
     */
    public String getPhotoUrl() {
        return photoUrl;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_company_user.photo_url
     *
     * @param photoUrl the value for pc_company_user.photo_url
     *
     * @mbg.generated
     */
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl == null ? null : photoUrl.trim();
    }
}