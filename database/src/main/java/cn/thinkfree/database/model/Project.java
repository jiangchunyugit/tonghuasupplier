package cn.thinkfree.database.model;

import cn.thinkfree.core.model.BaseModel;
import java.util.Date;

/**
 * Database Table Remarks:
 *   项目表
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table project
 */
public class Project extends BaseModel {
    /**
     * Database Column Remarks:
     *   主键id
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project.id
     *
     * @mbg.generated
     */
    private Long id;

    /**
     * Database Column Remarks:
     *   project_no
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project.project_no
     *
     * @mbg.generated
     */
    private String projectNo;

    /**
     * Database Column Remarks:
     *   状态(1,正常  2,失效)
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project.status
     *
     * @mbg.generated
     */
    private Integer status;

    /**
     * Database Column Remarks:
     *   项目阶段(预约/量房/设计/施工)
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project.stage
     *
     * @mbg.generated
     */
    private Integer stage;

    /**
     * Database Column Remarks:
     *   创建时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     * Database Column Remarks:
     *   发布时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project.release_time
     *
     * @mbg.generated
     */
    private Date releaseTime;

    /**
     * Database Column Remarks:
     *   订单来源(1,天猫 2,线下)
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project.order_source
     *
     * @mbg.generated
     */
    private Integer orderSource;

    /**
     * Database Column Remarks:
     *   房屋户型(别墅)
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project.house_huxing
     *
     * @mbg.generated
     */
    private Integer houseHuxing;

    /**
     * Database Column Remarks:
     *   居室
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project.house_room
     *
     * @mbg.generated
     */
    private Integer houseRoom;

    /**
     * Database Column Remarks:
     *   客厅
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project.house_office
     *
     * @mbg.generated
     */
    private Integer houseOffice;

    /**
     * Database Column Remarks:
     *   卫生间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project.house_toilet
     *
     * @mbg.generated
     */
    private Integer houseToilet;

    /**
     * Database Column Remarks:
     *   省(地址)
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project.province
     *
     * @mbg.generated
     */
    private String province;

    /**
     * Database Column Remarks:
     *   详细地址
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project.address_detail
     *
     * @mbg.generated
     */
    private String addressDetail;

    /**
     * Database Column Remarks:
     *   装修风格
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project.style
     *
     * @mbg.generated
     */
    private String style;

    /**
     * Database Column Remarks:
     *   建筑面积
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project.area
     *
     * @mbg.generated
     */
    private Integer area;

    /**
     * Database Column Remarks:
     *   房屋类型(新房/老房)
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project.house_type
     *
     * @mbg.generated
     */
    private Integer houseType;

    /**
     * Database Column Remarks:
     *   常住人口
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project.people_no
     *
     * @mbg.generated
     */
    private Integer peopleNo;

    /**
     * Database Column Remarks:
     *   计划装修开始时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project.plan_start_time
     *
     * @mbg.generated
     */
    private Date planStartTime;

    /**
     * Database Column Remarks:
     *   计划装修结束时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project.plan_end_time
     *
     * @mbg.generated
     */
    private Date planEndTime;

    /**
     * Database Column Remarks:
     *   装修预算
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project.decoration_budget
     *
     * @mbg.generated
     */
    private Integer decorationBudget;

    /**
     * Database Column Remarks:
     *   业主id
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project.owner_id
     *
     * @mbg.generated
     */
    private String ownerId;

    /**
     * Database Column Remarks:
     *   阳台
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project.balcony
     *
     * @mbg.generated
     */
    private Integer balcony;

    /**
     * Database Column Remarks:
     *   参考方案
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project.reference_scheme
     *
     * @mbg.generated
     */
    private String referenceScheme;

    /**
     * Database Column Remarks:
     *   项目图片地址
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project.img_url
     *
     * @mbg.generated
     */
    private String imgUrl;

    /**
     * Database Column Remarks:
     *   是否是3D
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project.third_view
     *
     * @mbg.generated
     */
    private Boolean thirdView;

    /**
     * Database Column Remarks:
     *   承包类型(1,小包  2,大包)
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project.contract_type
     *
     * @mbg.generated
     */
    private Integer contractType;

    /**
     * Database Column Remarks:
     *   城市(地址)
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project.city
     *
     * @mbg.generated
     */
    private String city;

    /**
     * Database Column Remarks:
     *   地区
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project.region
     *
     * @mbg.generated
     */
    private String region;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project.id
     *
     * @return the value of project.id
     *
     * @mbg.generated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project.id
     *
     * @param id the value for project.id
     *
     * @mbg.generated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project.project_no
     *
     * @return the value of project.project_no
     *
     * @mbg.generated
     */
    public String getProjectNo() {
        return projectNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project.project_no
     *
     * @param projectNo the value for project.project_no
     *
     * @mbg.generated
     */
    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo == null ? null : projectNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project.status
     *
     * @return the value of project.status
     *
     * @mbg.generated
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project.status
     *
     * @param status the value for project.status
     *
     * @mbg.generated
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project.stage
     *
     * @return the value of project.stage
     *
     * @mbg.generated
     */
    public Integer getStage() {
        return stage;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project.stage
     *
     * @param stage the value for project.stage
     *
     * @mbg.generated
     */
    public void setStage(Integer stage) {
        this.stage = stage;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project.create_time
     *
     * @return the value of project.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project.create_time
     *
     * @param createTime the value for project.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project.release_time
     *
     * @return the value of project.release_time
     *
     * @mbg.generated
     */
    public Date getReleaseTime() {
        return releaseTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project.release_time
     *
     * @param releaseTime the value for project.release_time
     *
     * @mbg.generated
     */
    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project.order_source
     *
     * @return the value of project.order_source
     *
     * @mbg.generated
     */
    public Integer getOrderSource() {
        return orderSource;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project.order_source
     *
     * @param orderSource the value for project.order_source
     *
     * @mbg.generated
     */
    public void setOrderSource(Integer orderSource) {
        this.orderSource = orderSource;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project.house_huxing
     *
     * @return the value of project.house_huxing
     *
     * @mbg.generated
     */
    public Integer getHouseHuxing() {
        return houseHuxing;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project.house_huxing
     *
     * @param houseHuxing the value for project.house_huxing
     *
     * @mbg.generated
     */
    public void setHouseHuxing(Integer houseHuxing) {
        this.houseHuxing = houseHuxing;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project.house_room
     *
     * @return the value of project.house_room
     *
     * @mbg.generated
     */
    public Integer getHouseRoom() {
        return houseRoom;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project.house_room
     *
     * @param houseRoom the value for project.house_room
     *
     * @mbg.generated
     */
    public void setHouseRoom(Integer houseRoom) {
        this.houseRoom = houseRoom;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project.house_office
     *
     * @return the value of project.house_office
     *
     * @mbg.generated
     */
    public Integer getHouseOffice() {
        return houseOffice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project.house_office
     *
     * @param houseOffice the value for project.house_office
     *
     * @mbg.generated
     */
    public void setHouseOffice(Integer houseOffice) {
        this.houseOffice = houseOffice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project.house_toilet
     *
     * @return the value of project.house_toilet
     *
     * @mbg.generated
     */
    public Integer getHouseToilet() {
        return houseToilet;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project.house_toilet
     *
     * @param houseToilet the value for project.house_toilet
     *
     * @mbg.generated
     */
    public void setHouseToilet(Integer houseToilet) {
        this.houseToilet = houseToilet;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project.province
     *
     * @return the value of project.province
     *
     * @mbg.generated
     */
    public String getProvince() {
        return province;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project.province
     *
     * @param province the value for project.province
     *
     * @mbg.generated
     */
    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project.address_detail
     *
     * @return the value of project.address_detail
     *
     * @mbg.generated
     */
    public String getAddressDetail() {
        return addressDetail;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project.address_detail
     *
     * @param addressDetail the value for project.address_detail
     *
     * @mbg.generated
     */
    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail == null ? null : addressDetail.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project.style
     *
     * @return the value of project.style
     *
     * @mbg.generated
     */
    public String getStyle() {
        return style;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project.style
     *
     * @param style the value for project.style
     *
     * @mbg.generated
     */
    public void setStyle(String style) {
        this.style = style == null ? null : style.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project.area
     *
     * @return the value of project.area
     *
     * @mbg.generated
     */
    public Integer getArea() {
        return area;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project.area
     *
     * @param area the value for project.area
     *
     * @mbg.generated
     */
    public void setArea(Integer area) {
        this.area = area;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project.house_type
     *
     * @return the value of project.house_type
     *
     * @mbg.generated
     */
    public Integer getHouseType() {
        return houseType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project.house_type
     *
     * @param houseType the value for project.house_type
     *
     * @mbg.generated
     */
    public void setHouseType(Integer houseType) {
        this.houseType = houseType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project.people_no
     *
     * @return the value of project.people_no
     *
     * @mbg.generated
     */
    public Integer getPeopleNo() {
        return peopleNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project.people_no
     *
     * @param peopleNo the value for project.people_no
     *
     * @mbg.generated
     */
    public void setPeopleNo(Integer peopleNo) {
        this.peopleNo = peopleNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project.plan_start_time
     *
     * @return the value of project.plan_start_time
     *
     * @mbg.generated
     */
    public Date getPlanStartTime() {
        return planStartTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project.plan_start_time
     *
     * @param planStartTime the value for project.plan_start_time
     *
     * @mbg.generated
     */
    public void setPlanStartTime(Date planStartTime) {
        this.planStartTime = planStartTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project.plan_end_time
     *
     * @return the value of project.plan_end_time
     *
     * @mbg.generated
     */
    public Date getPlanEndTime() {
        return planEndTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project.plan_end_time
     *
     * @param planEndTime the value for project.plan_end_time
     *
     * @mbg.generated
     */
    public void setPlanEndTime(Date planEndTime) {
        this.planEndTime = planEndTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project.decoration_budget
     *
     * @return the value of project.decoration_budget
     *
     * @mbg.generated
     */
    public Integer getDecorationBudget() {
        return decorationBudget;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project.decoration_budget
     *
     * @param decorationBudget the value for project.decoration_budget
     *
     * @mbg.generated
     */
    public void setDecorationBudget(Integer decorationBudget) {
        this.decorationBudget = decorationBudget;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project.owner_id
     *
     * @return the value of project.owner_id
     *
     * @mbg.generated
     */
    public String getOwnerId() {
        return ownerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project.owner_id
     *
     * @param ownerId the value for project.owner_id
     *
     * @mbg.generated
     */
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId == null ? null : ownerId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project.balcony
     *
     * @return the value of project.balcony
     *
     * @mbg.generated
     */
    public Integer getBalcony() {
        return balcony;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project.balcony
     *
     * @param balcony the value for project.balcony
     *
     * @mbg.generated
     */
    public void setBalcony(Integer balcony) {
        this.balcony = balcony;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project.reference_scheme
     *
     * @return the value of project.reference_scheme
     *
     * @mbg.generated
     */
    public String getReferenceScheme() {
        return referenceScheme;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project.reference_scheme
     *
     * @param referenceScheme the value for project.reference_scheme
     *
     * @mbg.generated
     */
    public void setReferenceScheme(String referenceScheme) {
        this.referenceScheme = referenceScheme == null ? null : referenceScheme.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project.img_url
     *
     * @return the value of project.img_url
     *
     * @mbg.generated
     */
    public String getImgUrl() {
        return imgUrl;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project.img_url
     *
     * @param imgUrl the value for project.img_url
     *
     * @mbg.generated
     */
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl == null ? null : imgUrl.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project.third_view
     *
     * @return the value of project.third_view
     *
     * @mbg.generated
     */
    public Boolean getThirdView() {
        return thirdView;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project.third_view
     *
     * @param thirdView the value for project.third_view
     *
     * @mbg.generated
     */
    public void setThirdView(Boolean thirdView) {
        this.thirdView = thirdView;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project.contract_type
     *
     * @return the value of project.contract_type
     *
     * @mbg.generated
     */
    public Integer getContractType() {
        return contractType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project.contract_type
     *
     * @param contractType the value for project.contract_type
     *
     * @mbg.generated
     */
    public void setContractType(Integer contractType) {
        this.contractType = contractType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project.city
     *
     * @return the value of project.city
     *
     * @mbg.generated
     */
    public String getCity() {
        return city;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project.city
     *
     * @param city the value for project.city
     *
     * @mbg.generated
     */
    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project.region
     *
     * @return the value of project.region
     *
     * @mbg.generated
     */
    public String getRegion() {
        return region;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project.region
     *
     * @param region the value for project.region
     *
     * @mbg.generated
     */
    public void setRegion(String region) {
        this.region = region == null ? null : region.trim();
    }
}