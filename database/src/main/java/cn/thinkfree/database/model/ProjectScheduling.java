package cn.thinkfree.database.model;

import cn.thinkfree.core.model.BaseModel;
import java.util.Date;

/**
 * Database Table Remarks:
 *   项目排期表
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table project_scheduling
 */
public class ProjectScheduling extends BaseModel {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project_scheduling.id
     *
     * @mbg.generated
     */
    private Long id;

    /**
     * Database Column Remarks:
     *   公司编号
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project_scheduling.company_id
     *
     * @mbg.generated
     */
    private Integer companyId;

    /**
     * Database Column Remarks:
     *   项目编号
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project_scheduling.project_no
     *
     * @mbg.generated
     */
    private String projectNo;

    /**
     * Database Column Remarks:
     *   开始时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project_scheduling.start_time
     *
     * @mbg.generated
     */
    private Date startTime;

    /**
     * Database Column Remarks:
     *   结束时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project_scheduling.end_time
     *
     * @mbg.generated
     */
    private Date endTime;

    /**
     * Database Column Remarks:
     *   任务数量
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project_scheduling.task_num
     *
     * @mbg.generated
     */
    private Integer taskNum;

    /**
     * Database Column Remarks:
     *   延期时间(天)
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project_scheduling.delay
     *
     * @mbg.generated
     */
    private Integer delay;

    /**
     * Database Column Remarks:
     *   变更次数
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project_scheduling.change_num
     *
     * @mbg.generated
     */
    private Integer changeNum;

    /**
     * Database Column Remarks:
     *   进度(
     *   1,准备阶段 
     *   2, 拆除工程  
     *   3, 门窗工程 
     *   4, 水电工程 
     *   5, 泥瓦工程 
     *   6, 木工施工工程 
     *   7, 涂料喷涂工程 
     *   8, 安装工程 
     *   9, 竣工阶段)
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project_scheduling.rate
     *
     * @mbg.generated
     */
    private Integer rate;

    /**
     * Database Column Remarks:
     *   状态(1-正常，2-失效，3-冻结)
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project_scheduling.status
     *
     * @mbg.generated
     */
    private Integer status;

    /**
     * Database Column Remarks:
     *   创建时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project_scheduling.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     * Database Column Remarks:
     *   是否确认(0,未确认 1,已确认)
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project_scheduling.is_confirm
     *
     * @mbg.generated
     */
    private Integer isConfirm;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project_scheduling.id
     *
     * @return the value of project_scheduling.id
     *
     * @mbg.generated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project_scheduling.id
     *
     * @param id the value for project_scheduling.id
     *
     * @mbg.generated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project_scheduling.company_id
     *
     * @return the value of project_scheduling.company_id
     *
     * @mbg.generated
     */
    public Integer getCompanyId() {
        return companyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project_scheduling.company_id
     *
     * @param companyId the value for project_scheduling.company_id
     *
     * @mbg.generated
     */
    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project_scheduling.project_no
     *
     * @return the value of project_scheduling.project_no
     *
     * @mbg.generated
     */
    public String getProjectNo() {
        return projectNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project_scheduling.project_no
     *
     * @param projectNo the value for project_scheduling.project_no
     *
     * @mbg.generated
     */
    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo == null ? null : projectNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project_scheduling.start_time
     *
     * @return the value of project_scheduling.start_time
     *
     * @mbg.generated
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project_scheduling.start_time
     *
     * @param startTime the value for project_scheduling.start_time
     *
     * @mbg.generated
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project_scheduling.end_time
     *
     * @return the value of project_scheduling.end_time
     *
     * @mbg.generated
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project_scheduling.end_time
     *
     * @param endTime the value for project_scheduling.end_time
     *
     * @mbg.generated
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project_scheduling.task_num
     *
     * @return the value of project_scheduling.task_num
     *
     * @mbg.generated
     */
    public Integer getTaskNum() {
        return taskNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project_scheduling.task_num
     *
     * @param taskNum the value for project_scheduling.task_num
     *
     * @mbg.generated
     */
    public void setTaskNum(Integer taskNum) {
        this.taskNum = taskNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project_scheduling.delay
     *
     * @return the value of project_scheduling.delay
     *
     * @mbg.generated
     */
    public Integer getDelay() {
        return delay;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project_scheduling.delay
     *
     * @param delay the value for project_scheduling.delay
     *
     * @mbg.generated
     */
    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project_scheduling.change_num
     *
     * @return the value of project_scheduling.change_num
     *
     * @mbg.generated
     */
    public Integer getChangeNum() {
        return changeNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project_scheduling.change_num
     *
     * @param changeNum the value for project_scheduling.change_num
     *
     * @mbg.generated
     */
    public void setChangeNum(Integer changeNum) {
        this.changeNum = changeNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project_scheduling.rate
     *
     * @return the value of project_scheduling.rate
     *
     * @mbg.generated
     */
    public Integer getRate() {
        return rate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project_scheduling.rate
     *
     * @param rate the value for project_scheduling.rate
     *
     * @mbg.generated
     */
    public void setRate(Integer rate) {
        this.rate = rate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project_scheduling.status
     *
     * @return the value of project_scheduling.status
     *
     * @mbg.generated
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project_scheduling.status
     *
     * @param status the value for project_scheduling.status
     *
     * @mbg.generated
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project_scheduling.create_time
     *
     * @return the value of project_scheduling.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project_scheduling.create_time
     *
     * @param createTime the value for project_scheduling.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project_scheduling.is_confirm
     *
     * @return the value of project_scheduling.is_confirm
     *
     * @mbg.generated
     */
    public Integer getIsConfirm() {
        return isConfirm;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project_scheduling.is_confirm
     *
     * @param isConfirm the value for project_scheduling.is_confirm
     *
     * @mbg.generated
     */
    public void setIsConfirm(Integer isConfirm) {
        this.isConfirm = isConfirm;
    }
}