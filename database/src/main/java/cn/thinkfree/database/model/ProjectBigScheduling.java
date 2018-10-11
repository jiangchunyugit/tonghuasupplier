package cn.thinkfree.database.model;

import cn.thinkfree.core.model.BaseModel;
import java.util.Date;

/**
 * Database Table Remarks:
 *   项目施工顺序表(大排期)
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table project_construction_sort
 */
public class ProjectBigScheduling extends BaseModel {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project_construction_sort.id
     *
     * @mbg.generated
     */
    private Long id;

    /**
     * Database Column Remarks:
     *   公司编号
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project_construction_sort.company_id
     *
     * @mbg.generated
     */
    private String companyId;

    /**
     * Database Column Remarks:
     *   序号
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project_construction_sort.sort
     *
     * @mbg.generated
     */
    private Integer sort;

    /**
     * Database Column Remarks:
     *   工作模块名称
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project_construction_sort.name
     *
     * @mbg.generated
     */
    private String name;

    /**
     * Database Column Remarks:
     *   自定义大排期名字
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project_construction_sort.rename
     *
     * @mbg.generated
     */
    private String rename;

    /**
     * Database Column Remarks:
     *   描述
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project_construction_sort.description
     *
     * @mbg.generated
     */
    private String description;

    /**
     * Database Column Remarks:
     *   下限平米
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project_construction_sort.square_metre_start
     *
     * @mbg.generated
     */
    private Integer squareMetreStart;

    /**
     * Database Column Remarks:
     *   上限平米
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project_construction_sort.square_metre_end
     *
     * @mbg.generated
     */
    private Integer squareMetreEnd;

    /**
     * Database Column Remarks:
     *   施工工期，单位（天）
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project_construction_sort.workload
     *
     * @mbg.generated
     */
    private Integer workload;

    /**
     * Database Column Remarks:
     *   状态(1,正常  2,失效)
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project_construction_sort.status
     *
     * @mbg.generated
     */
    private Integer status;

    /**
     * Database Column Remarks:
     *   创建时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project_construction_sort.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     * Database Column Remarks:
     *   版本号
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project_construction_sort.version
     *
     * @mbg.generated
     */
    private Integer version;

    /**
     * Database Column Remarks:
     *   是否需要验收(0-不需要验收，1-需要验收)
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project_construction_sort.is_need_check
     *
     * @mbg.generated
     */
    private Short isNeedCheck;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project_construction_sort.id
     *
     * @return the value of project_construction_sort.id
     *
     * @mbg.generated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project_construction_sort.id
     *
     * @param id the value for project_construction_sort.id
     *
     * @mbg.generated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project_construction_sort.company_id
     *
     * @return the value of project_construction_sort.company_id
     *
     * @mbg.generated
     */
    public String getCompanyId() {
        return companyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project_construction_sort.company_id
     *
     * @param companyId the value for project_construction_sort.company_id
     *
     * @mbg.generated
     */
    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project_construction_sort.sort
     *
     * @return the value of project_construction_sort.sort
     *
     * @mbg.generated
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project_construction_sort.sort
     *
     * @param sort the value for project_construction_sort.sort
     *
     * @mbg.generated
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project_construction_sort.name
     *
     * @return the value of project_construction_sort.name
     *
     * @mbg.generated
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project_construction_sort.name
     *
     * @param name the value for project_construction_sort.name
     *
     * @mbg.generated
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project_construction_sort.rename
     *
     * @return the value of project_construction_sort.rename
     *
     * @mbg.generated
     */
    public String getRename() {
        return rename;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project_construction_sort.rename
     *
     * @param rename the value for project_construction_sort.rename
     *
     * @mbg.generated
     */
    public void setRename(String rename) {
        this.rename = rename == null ? null : rename.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project_construction_sort.description
     *
     * @return the value of project_construction_sort.description
     *
     * @mbg.generated
     */
    public String getDescription() {
        return description;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project_construction_sort.description
     *
     * @param description the value for project_construction_sort.description
     *
     * @mbg.generated
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project_construction_sort.square_metre_start
     *
     * @return the value of project_construction_sort.square_metre_start
     *
     * @mbg.generated
     */
    public Integer getSquareMetreStart() {
        return squareMetreStart;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project_construction_sort.square_metre_start
     *
     * @param squareMetreStart the value for project_construction_sort.square_metre_start
     *
     * @mbg.generated
     */
    public void setSquareMetreStart(Integer squareMetreStart) {
        this.squareMetreStart = squareMetreStart;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project_construction_sort.square_metre_end
     *
     * @return the value of project_construction_sort.square_metre_end
     *
     * @mbg.generated
     */
    public Integer getSquareMetreEnd() {
        return squareMetreEnd;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project_construction_sort.square_metre_end
     *
     * @param squareMetreEnd the value for project_construction_sort.square_metre_end
     *
     * @mbg.generated
     */
    public void setSquareMetreEnd(Integer squareMetreEnd) {
        this.squareMetreEnd = squareMetreEnd;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project_construction_sort.workload
     *
     * @return the value of project_construction_sort.workload
     *
     * @mbg.generated
     */
    public Integer getWorkload() {
        return workload;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project_construction_sort.workload
     *
     * @param workload the value for project_construction_sort.workload
     *
     * @mbg.generated
     */
    public void setWorkload(Integer workload) {
        this.workload = workload;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project_construction_sort.status
     *
     * @return the value of project_construction_sort.status
     *
     * @mbg.generated
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project_construction_sort.status
     *
     * @param status the value for project_construction_sort.status
     *
     * @mbg.generated
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project_construction_sort.create_time
     *
     * @return the value of project_construction_sort.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project_construction_sort.create_time
     *
     * @param createTime the value for project_construction_sort.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project_construction_sort.version
     *
     * @return the value of project_construction_sort.version
     *
     * @mbg.generated
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project_construction_sort.version
     *
     * @param version the value for project_construction_sort.version
     *
     * @mbg.generated
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project_construction_sort.is_need_check
     *
     * @return the value of project_construction_sort.is_need_check
     *
     * @mbg.generated
     */
    public Short getIsNeedCheck() {
        return isNeedCheck;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project_construction_sort.is_need_check
     *
     * @param isNeedCheck the value for project_construction_sort.is_need_check
     *
     * @mbg.generated
     */
    public void setIsNeedCheck(Short isNeedCheck) {
        this.isNeedCheck = isNeedCheck;
    }
}