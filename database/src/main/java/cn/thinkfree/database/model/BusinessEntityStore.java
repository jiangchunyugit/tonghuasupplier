package cn.thinkfree.database.model;

import cn.thinkfree.core.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Database Table Remarks:
 *   城市分站店面信息表
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table pc_business_entity_store
 */
@ApiModel(value="cn.thinkfree.database.model.BusinessEntityStore")
public class BusinessEntityStore extends BaseModel {
    /**
     * Database Column Remarks:
     *   自增主键
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_business_entity_store.id
     *
     * @mbg.generated
     */
    @ApiModelProperty(value="id自增主键")
    private Integer id;

    /**
     * Database Column Remarks:
     *   城市分站编码
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_business_entity_store.city_branch_code
     *
     * @mbg.generated
     */
    @ApiModelProperty(value="cityBranchCode城市分站编码")
    private String cityBranchCode;

    /**
     * Database Column Remarks:
     *   店面名称
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_business_entity_store.store_nm
     *
     * @mbg.generated
     */
    @ApiModelProperty(value="storeNm店面名称")
    private String storeNm;

    /**
     * Database Column Remarks:
     *   经营主体编码
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_business_entity_store.business_entity_code
     *
     * @mbg.generated
     */
    @ApiModelProperty(value="businessEntityCode经营主体编码")
    private String businessEntityCode;

    /**
     * Database Column Remarks:
     *   门店ID对应埃森哲部门ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_business_entity_store.store_id
     *
     * @mbg.generated
     */
    @ApiModelProperty(value="storeId门店ID对应埃森哲部门ID")
    private String storeId;

    /**
     * Database Column Remarks:
     *   分公司编码
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_business_entity_store.branch_company_code
     *
     * @mbg.generated
     */
    @ApiModelProperty(value="branchCompanyCode分公司编码")
    private String branchCompanyCode;

    /**
     * Database Column Remarks:
     *   经营主体名称
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_business_entity_store.entity_name
     *
     * @mbg.generated
     */
    @ApiModelProperty(value="entityName经营主体名称")
    private String entityName;

    /**
     * Database Column Remarks:
     *   经营主体分站主键
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_business_entity_store.business_entity_relation_id
     *
     * @mbg.generated
     */
    @ApiModelProperty(value="businessEntityRelationId经营主体分站主键")
    private Integer businessEntityRelationId;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_business_entity_store.id
     *
     * @return the value of pc_business_entity_store.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_business_entity_store.id
     *
     * @param id the value for pc_business_entity_store.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_business_entity_store.city_branch_code
     *
     * @return the value of pc_business_entity_store.city_branch_code
     *
     * @mbg.generated
     */
    public String getCityBranchCode() {
        return cityBranchCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_business_entity_store.city_branch_code
     *
     * @param cityBranchCode the value for pc_business_entity_store.city_branch_code
     *
     * @mbg.generated
     */
    public void setCityBranchCode(String cityBranchCode) {
        this.cityBranchCode = cityBranchCode == null ? null : cityBranchCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_business_entity_store.store_nm
     *
     * @return the value of pc_business_entity_store.store_nm
     *
     * @mbg.generated
     */
    public String getStoreNm() {
        return storeNm;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_business_entity_store.store_nm
     *
     * @param storeNm the value for pc_business_entity_store.store_nm
     *
     * @mbg.generated
     */
    public void setStoreNm(String storeNm) {
        this.storeNm = storeNm == null ? null : storeNm.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_business_entity_store.business_entity_code
     *
     * @return the value of pc_business_entity_store.business_entity_code
     *
     * @mbg.generated
     */
    public String getBusinessEntityCode() {
        return businessEntityCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_business_entity_store.business_entity_code
     *
     * @param businessEntityCode the value for pc_business_entity_store.business_entity_code
     *
     * @mbg.generated
     */
    public void setBusinessEntityCode(String businessEntityCode) {
        this.businessEntityCode = businessEntityCode == null ? null : businessEntityCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_business_entity_store.store_id
     *
     * @return the value of pc_business_entity_store.store_id
     *
     * @mbg.generated
     */
    public String getStoreId() {
        return storeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_business_entity_store.store_id
     *
     * @param storeId the value for pc_business_entity_store.store_id
     *
     * @mbg.generated
     */
    public void setStoreId(String storeId) {
        this.storeId = storeId == null ? null : storeId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_business_entity_store.branch_company_code
     *
     * @return the value of pc_business_entity_store.branch_company_code
     *
     * @mbg.generated
     */
    public String getBranchCompanyCode() {
        return branchCompanyCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_business_entity_store.branch_company_code
     *
     * @param branchCompanyCode the value for pc_business_entity_store.branch_company_code
     *
     * @mbg.generated
     */
    public void setBranchCompanyCode(String branchCompanyCode) {
        this.branchCompanyCode = branchCompanyCode == null ? null : branchCompanyCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_business_entity_store.entity_name
     *
     * @return the value of pc_business_entity_store.entity_name
     *
     * @mbg.generated
     */
    public String getEntityName() {
        return entityName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_business_entity_store.entity_name
     *
     * @param entityName the value for pc_business_entity_store.entity_name
     *
     * @mbg.generated
     */
    public void setEntityName(String entityName) {
        this.entityName = entityName == null ? null : entityName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_business_entity_store.business_entity_relation_id
     *
     * @return the value of pc_business_entity_store.business_entity_relation_id
     *
     * @mbg.generated
     */
    public Integer getBusinessEntityRelationId() {
        return businessEntityRelationId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_business_entity_store.business_entity_relation_id
     *
     * @param businessEntityRelationId the value for pc_business_entity_store.business_entity_relation_id
     *
     * @mbg.generated
     */
    public void setBusinessEntityRelationId(Integer businessEntityRelationId) {
        this.businessEntityRelationId = businessEntityRelationId;
    }
}