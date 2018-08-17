package cn.thinkfree.database.model;

import cn.thinkfree.core.model.BaseModel;
import java.util.Date;

/**
 * Database Table Remarks:
 *   后台用户信息表
 *   在user_register 中type为3
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table pc_user_info
 */
public class PcUserInfo extends BaseModel {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_user_info.id
     *
     * @mbg.generated
     */
    private String id;

    /**
     * Database Column Remarks:
     *   用户名
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_user_info.name
     *
     * @mbg.generated
     */
    private String name;

    /**
     * Database Column Remarks:
     *   手机号
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_user_info.phone
     *
     * @mbg.generated
     */
    private String phone;

    /**
     * Database Column Remarks:
     *   备注
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_user_info.memo
     *
     * @mbg.generated
     */
    private String memo;

    /**
     * Database Column Remarks:
     *   创建者
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_user_info.creator
     *
     * @mbg.generated
     */
    private String creator;

    /**
     * Database Column Remarks:
     *   创建日期
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_user_info.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     * Database Column Remarks:
     *   是否启用
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_user_info.enabled
     *
     * @mbg.generated
     */
    private Short enabled;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_user_info.is_delete
     *
     * @mbg.generated
     */
    private Short isDelete;

    /**
     * Database Column Remarks:
     *   公司主键
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_user_info.company_id
     *
     * @mbg.generated
     */
    private String companyId;

    /**
     * Database Column Remarks:
     *   根公司主键
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_user_info.root_company_id
     *
     * @mbg.generated
     */
    private String rootCompanyId;

    /**
     * Database Column Remarks:
     *   父级公司
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_user_info.parent_company_id
     *
     * @mbg.generated
     */
    private String parentCompanyId;

    /**
     * Database Column Remarks:
     *   用户级别 0 创世 1公司根账号 2公司省账号 3 公司市账号
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_user_info.level
     *
     * @mbg.generated
     */
    private Short level;

    /**
     * Database Column Remarks:
     *   省
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_user_info.province
     *
     * @mbg.generated
     */
    private String province;

    /**
     * Database Column Remarks:
     *   市
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_user_info.city
     *
     * @mbg.generated
     */
    private String city;

    /**
     * Database Column Remarks:
     *   区
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_user_info.area
     *
     * @mbg.generated
     */
    private String area;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_user_info.id
     *
     * @return the value of pc_user_info.id
     *
     * @mbg.generated
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_user_info.id
     *
     * @param id the value for pc_user_info.id
     *
     * @mbg.generated
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_user_info.name
     *
     * @return the value of pc_user_info.name
     *
     * @mbg.generated
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_user_info.name
     *
     * @param name the value for pc_user_info.name
     *
     * @mbg.generated
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_user_info.phone
     *
     * @return the value of pc_user_info.phone
     *
     * @mbg.generated
     */
    public String getPhone() {
        return phone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_user_info.phone
     *
     * @param phone the value for pc_user_info.phone
     *
     * @mbg.generated
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_user_info.memo
     *
     * @return the value of pc_user_info.memo
     *
     * @mbg.generated
     */
    public String getMemo() {
        return memo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_user_info.memo
     *
     * @param memo the value for pc_user_info.memo
     *
     * @mbg.generated
     */
    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_user_info.creator
     *
     * @return the value of pc_user_info.creator
     *
     * @mbg.generated
     */
    public String getCreator() {
        return creator;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_user_info.creator
     *
     * @param creator the value for pc_user_info.creator
     *
     * @mbg.generated
     */
    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_user_info.create_time
     *
     * @return the value of pc_user_info.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_user_info.create_time
     *
     * @param createTime the value for pc_user_info.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_user_info.enabled
     *
     * @return the value of pc_user_info.enabled
     *
     * @mbg.generated
     */
    public Short getEnabled() {
        return enabled;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_user_info.enabled
     *
     * @param enabled the value for pc_user_info.enabled
     *
     * @mbg.generated
     */
    public void setEnabled(Short enabled) {
        this.enabled = enabled;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_user_info.is_delete
     *
     * @return the value of pc_user_info.is_delete
     *
     * @mbg.generated
     */
    public Short getIsDelete() {
        return isDelete;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_user_info.is_delete
     *
     * @param isDelete the value for pc_user_info.is_delete
     *
     * @mbg.generated
     */
    public void setIsDelete(Short isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_user_info.company_id
     *
     * @return the value of pc_user_info.company_id
     *
     * @mbg.generated
     */
    public String getCompanyId() {
        return companyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_user_info.company_id
     *
     * @param companyId the value for pc_user_info.company_id
     *
     * @mbg.generated
     */
    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_user_info.root_company_id
     *
     * @return the value of pc_user_info.root_company_id
     *
     * @mbg.generated
     */
    public String getRootCompanyId() {
        return rootCompanyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_user_info.root_company_id
     *
     * @param rootCompanyId the value for pc_user_info.root_company_id
     *
     * @mbg.generated
     */
    public void setRootCompanyId(String rootCompanyId) {
        this.rootCompanyId = rootCompanyId == null ? null : rootCompanyId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_user_info.parent_company_id
     *
     * @return the value of pc_user_info.parent_company_id
     *
     * @mbg.generated
     */
    public String getParentCompanyId() {
        return parentCompanyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_user_info.parent_company_id
     *
     * @param parentCompanyId the value for pc_user_info.parent_company_id
     *
     * @mbg.generated
     */
    public void setParentCompanyId(String parentCompanyId) {
        this.parentCompanyId = parentCompanyId == null ? null : parentCompanyId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_user_info.level
     *
     * @return the value of pc_user_info.level
     *
     * @mbg.generated
     */
    public Short getLevel() {
        return level;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_user_info.level
     *
     * @param level the value for pc_user_info.level
     *
     * @mbg.generated
     */
    public void setLevel(Short level) {
        this.level = level;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_user_info.province
     *
     * @return the value of pc_user_info.province
     *
     * @mbg.generated
     */
    public String getProvince() {
        return province;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_user_info.province
     *
     * @param province the value for pc_user_info.province
     *
     * @mbg.generated
     */
    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_user_info.city
     *
     * @return the value of pc_user_info.city
     *
     * @mbg.generated
     */
    public String getCity() {
        return city;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_user_info.city
     *
     * @param city the value for pc_user_info.city
     *
     * @mbg.generated
     */
    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_user_info.area
     *
     * @return the value of pc_user_info.area
     *
     * @mbg.generated
     */
    public String getArea() {
        return area;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_user_info.area
     *
     * @param area the value for pc_user_info.area
     *
     * @mbg.generated
     */
    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }
}