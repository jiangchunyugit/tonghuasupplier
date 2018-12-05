package cn.thinkfree.database.model;

import cn.thinkfree.core.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table pc_agency_contract_terms_history
 */
@ApiModel(value="cn.thinkfree.database.model.AgencyContractTermsHistory")
public class AgencyContractTermsHistory extends BaseModel {
    /**
     * Database Column Remarks:
     *   主鍵id
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_agency_contract_terms_history.id
     *
     * @mbg.generated
     */
    @ApiModelProperty(value="id主鍵id")
    private Integer id;

    /**
     * Database Column Remarks:
     *   合同编号
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_agency_contract_terms_history.contract_number
     *
     * @mbg.generated
     */
    @ApiModelProperty(value="contractNumber合同编号")
    private String contractNumber;

    /**
     * Database Column Remarks:
     *   公司编号
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_agency_contract_terms_history.company_id
     *
     * @mbg.generated
     */
    @ApiModelProperty(value="companyId公司编号")
    private String companyId;

    /**
     * Database Column Remarks:
     *   品牌名称
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_agency_contract_terms_history.brand_name
     *
     * @mbg.generated
     */
    @ApiModelProperty(value="brandName品牌名称")
    private String brandName;

    /**
     * Database Column Remarks:
     *   品牌编码
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_agency_contract_terms_history.brand_no
     *
     * @mbg.generated
     */
    @ApiModelProperty(value="brandNo品牌编码")
    private String brandNo;

    /**
     * Database Column Remarks:
     *   品类名称
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_agency_contract_terms_history.category_name
     *
     * @mbg.generated
     */
    @ApiModelProperty(value="categoryName品类名称")
    private String categoryName;

    /**
     * Database Column Remarks:
     *   品类编码
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_agency_contract_terms_history.category_no
     *
     * @mbg.generated
     */
    @ApiModelProperty(value="categoryNo品类编码")
    private String categoryNo;

    /**
     * Database Column Remarks:
     *   门店名称
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_agency_contract_terms_history.shop_name
     *
     * @mbg.generated
     */
    @ApiModelProperty(value="shopName门店名称")
    private String shopName;

    /**
     * Database Column Remarks:
     *   门店code
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_agency_contract_terms_history.shop_code
     *
     * @mbg.generated
     */
    @ApiModelProperty(value="shopCode门店code")
    private String shopCode;

    /**
     * Database Column Remarks:
     *   摊位名称
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_agency_contract_terms_history.booth_name
     *
     * @mbg.generated
     */
    @ApiModelProperty(value="boothName摊位名称")
    private String boothName;

    /**
     * Database Column Remarks:
     *   摊位code
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_agency_contract_terms_history.booth_code
     *
     * @mbg.generated
     */
    @ApiModelProperty(value="boothCode摊位code")
    private String boothCode;

    /**
     * Database Column Remarks:
     *   结算开始日
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_agency_contract_terms_history.st_start_time
     *
     * @mbg.generated
     */
    @ApiModelProperty(value="stStartTime结算开始日")
    private Date stStartTime;

    /**
     * Database Column Remarks:
     *   结算结束日
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_agency_contract_terms_history.st_end_time
     *
     * @mbg.generated
     */
    @ApiModelProperty(value="stEndTime结算结束日")
    private Date stEndTime;

    /**
     * Database Column Remarks:
     *   承担话费金额单位（万）
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_agency_contract_terms_history.activity_cost
     *
     * @mbg.generated
     */
    @ApiModelProperty(value="activityCost承担话费金额单位（万）")
    private BigDecimal activityCost;

    /**
     * Database Column Remarks:
     *   产品服务费比例
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_agency_contract_terms_history.product_ratio
     *
     * @mbg.generated
     */
    @ApiModelProperty(value="productRatio产品服务费比例")
    private String productRatio;

    /**
     * Database Column Remarks:
     *   推荐服务费比例
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_agency_contract_terms_history.recommend_ratio
     *
     * @mbg.generated
     */
    @ApiModelProperty(value="recommendRatio推荐服务费比例")
    private String recommendRatio;

    /**
     * Database Column Remarks:
     *   平台服务费比例
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_agency_contract_terms_history.service_ratio
     *
     * @mbg.generated
     */
    @ApiModelProperty(value="serviceRatio平台服务费比例")
    private String serviceRatio;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_agency_contract_terms_history.id
     *
     * @return the value of pc_agency_contract_terms_history.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_agency_contract_terms_history.id
     *
     * @param id the value for pc_agency_contract_terms_history.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_agency_contract_terms_history.contract_number
     *
     * @return the value of pc_agency_contract_terms_history.contract_number
     *
     * @mbg.generated
     */
    public String getContractNumber() {
        return contractNumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_agency_contract_terms_history.contract_number
     *
     * @param contractNumber the value for pc_agency_contract_terms_history.contract_number
     *
     * @mbg.generated
     */
    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber == null ? null : contractNumber.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_agency_contract_terms_history.company_id
     *
     * @return the value of pc_agency_contract_terms_history.company_id
     *
     * @mbg.generated
     */
    public String getCompanyId() {
        return companyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_agency_contract_terms_history.company_id
     *
     * @param companyId the value for pc_agency_contract_terms_history.company_id
     *
     * @mbg.generated
     */
    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_agency_contract_terms_history.brand_name
     *
     * @return the value of pc_agency_contract_terms_history.brand_name
     *
     * @mbg.generated
     */
    public String getBrandName() {
        return brandName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_agency_contract_terms_history.brand_name
     *
     * @param brandName the value for pc_agency_contract_terms_history.brand_name
     *
     * @mbg.generated
     */
    public void setBrandName(String brandName) {
        this.brandName = brandName == null ? null : brandName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_agency_contract_terms_history.brand_no
     *
     * @return the value of pc_agency_contract_terms_history.brand_no
     *
     * @mbg.generated
     */
    public String getBrandNo() {
        return brandNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_agency_contract_terms_history.brand_no
     *
     * @param brandNo the value for pc_agency_contract_terms_history.brand_no
     *
     * @mbg.generated
     */
    public void setBrandNo(String brandNo) {
        this.brandNo = brandNo == null ? null : brandNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_agency_contract_terms_history.category_name
     *
     * @return the value of pc_agency_contract_terms_history.category_name
     *
     * @mbg.generated
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_agency_contract_terms_history.category_name
     *
     * @param categoryName the value for pc_agency_contract_terms_history.category_name
     *
     * @mbg.generated
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName == null ? null : categoryName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_agency_contract_terms_history.category_no
     *
     * @return the value of pc_agency_contract_terms_history.category_no
     *
     * @mbg.generated
     */
    public String getCategoryNo() {
        return categoryNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_agency_contract_terms_history.category_no
     *
     * @param categoryNo the value for pc_agency_contract_terms_history.category_no
     *
     * @mbg.generated
     */
    public void setCategoryNo(String categoryNo) {
        this.categoryNo = categoryNo == null ? null : categoryNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_agency_contract_terms_history.shop_name
     *
     * @return the value of pc_agency_contract_terms_history.shop_name
     *
     * @mbg.generated
     */
    public String getShopName() {
        return shopName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_agency_contract_terms_history.shop_name
     *
     * @param shopName the value for pc_agency_contract_terms_history.shop_name
     *
     * @mbg.generated
     */
    public void setShopName(String shopName) {
        this.shopName = shopName == null ? null : shopName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_agency_contract_terms_history.shop_code
     *
     * @return the value of pc_agency_contract_terms_history.shop_code
     *
     * @mbg.generated
     */
    public String getShopCode() {
        return shopCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_agency_contract_terms_history.shop_code
     *
     * @param shopCode the value for pc_agency_contract_terms_history.shop_code
     *
     * @mbg.generated
     */
    public void setShopCode(String shopCode) {
        this.shopCode = shopCode == null ? null : shopCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_agency_contract_terms_history.booth_name
     *
     * @return the value of pc_agency_contract_terms_history.booth_name
     *
     * @mbg.generated
     */
    public String getBoothName() {
        return boothName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_agency_contract_terms_history.booth_name
     *
     * @param boothName the value for pc_agency_contract_terms_history.booth_name
     *
     * @mbg.generated
     */
    public void setBoothName(String boothName) {
        this.boothName = boothName == null ? null : boothName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_agency_contract_terms_history.booth_code
     *
     * @return the value of pc_agency_contract_terms_history.booth_code
     *
     * @mbg.generated
     */
    public String getBoothCode() {
        return boothCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_agency_contract_terms_history.booth_code
     *
     * @param boothCode the value for pc_agency_contract_terms_history.booth_code
     *
     * @mbg.generated
     */
    public void setBoothCode(String boothCode) {
        this.boothCode = boothCode == null ? null : boothCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_agency_contract_terms_history.st_start_time
     *
     * @return the value of pc_agency_contract_terms_history.st_start_time
     *
     * @mbg.generated
     */
    public Date getStStartTime() {
        return stStartTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_agency_contract_terms_history.st_start_time
     *
     * @param stStartTime the value for pc_agency_contract_terms_history.st_start_time
     *
     * @mbg.generated
     */
    public void setStStartTime(Date stStartTime) {
        this.stStartTime = stStartTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_agency_contract_terms_history.st_end_time
     *
     * @return the value of pc_agency_contract_terms_history.st_end_time
     *
     * @mbg.generated
     */
    public Date getStEndTime() {
        return stEndTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_agency_contract_terms_history.st_end_time
     *
     * @param stEndTime the value for pc_agency_contract_terms_history.st_end_time
     *
     * @mbg.generated
     */
    public void setStEndTime(Date stEndTime) {
        this.stEndTime = stEndTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_agency_contract_terms_history.activity_cost
     *
     * @return the value of pc_agency_contract_terms_history.activity_cost
     *
     * @mbg.generated
     */
    public BigDecimal getActivityCost() {
        return activityCost;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_agency_contract_terms_history.activity_cost
     *
     * @param activityCost the value for pc_agency_contract_terms_history.activity_cost
     *
     * @mbg.generated
     */
    public void setActivityCost(BigDecimal activityCost) {
        this.activityCost = activityCost;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_agency_contract_terms_history.product_ratio
     *
     * @return the value of pc_agency_contract_terms_history.product_ratio
     *
     * @mbg.generated
     */
    public String getProductRatio() {
        return productRatio;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_agency_contract_terms_history.product_ratio
     *
     * @param productRatio the value for pc_agency_contract_terms_history.product_ratio
     *
     * @mbg.generated
     */
    public void setProductRatio(String productRatio) {
        this.productRatio = productRatio == null ? null : productRatio.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_agency_contract_terms_history.recommend_ratio
     *
     * @return the value of pc_agency_contract_terms_history.recommend_ratio
     *
     * @mbg.generated
     */
    public String getRecommendRatio() {
        return recommendRatio;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_agency_contract_terms_history.recommend_ratio
     *
     * @param recommendRatio the value for pc_agency_contract_terms_history.recommend_ratio
     *
     * @mbg.generated
     */
    public void setRecommendRatio(String recommendRatio) {
        this.recommendRatio = recommendRatio == null ? null : recommendRatio.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_agency_contract_terms_history.service_ratio
     *
     * @return the value of pc_agency_contract_terms_history.service_ratio
     *
     * @mbg.generated
     */
    public String getServiceRatio() {
        return serviceRatio;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_agency_contract_terms_history.service_ratio
     *
     * @param serviceRatio the value for pc_agency_contract_terms_history.service_ratio
     *
     * @mbg.generated
     */
    public void setServiceRatio(String serviceRatio) {
        this.serviceRatio = serviceRatio == null ? null : serviceRatio.trim();
    }
}