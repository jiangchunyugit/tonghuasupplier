package cn.thinkfree.database.model;

import cn.thinkfree.core.model.BaseModel;

/**
 * Database Table Remarks:
 *   权限-用户门店中间表
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table pc_system_user_store
 */
public class SystemUserStore extends BaseModel {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_system_user_store.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     * Database Column Remarks:
     *   用户主键
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_system_user_store.user_id
     *
     * @mbg.generated
     */
    private String userId;

    /**
     * Database Column Remarks:
     *   分站ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_system_user_store.branch_code
     *
     * @mbg.generated
     */
    private String branchCode;

    /**
     * Database Column Remarks:
     *   城市站ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_system_user_store.city_branch_code
     *
     * @mbg.generated
     */
    private String cityBranchCode;

    /**
     * Database Column Remarks:
     *   门店ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pc_system_user_store.store_code
     *
     * @mbg.generated
     */
    private String storeCode;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_system_user_store.id
     *
     * @return the value of pc_system_user_store.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_system_user_store.id
     *
     * @param id the value for pc_system_user_store.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_system_user_store.user_id
     *
     * @return the value of pc_system_user_store.user_id
     *
     * @mbg.generated
     */
    public String getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_system_user_store.user_id
     *
     * @param userId the value for pc_system_user_store.user_id
     *
     * @mbg.generated
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_system_user_store.branch_code
     *
     * @return the value of pc_system_user_store.branch_code
     *
     * @mbg.generated
     */
    public String getBranchCode() {
        return branchCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_system_user_store.branch_code
     *
     * @param branchCode the value for pc_system_user_store.branch_code
     *
     * @mbg.generated
     */
    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode == null ? null : branchCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_system_user_store.city_branch_code
     *
     * @return the value of pc_system_user_store.city_branch_code
     *
     * @mbg.generated
     */
    public String getCityBranchCode() {
        return cityBranchCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_system_user_store.city_branch_code
     *
     * @param cityBranchCode the value for pc_system_user_store.city_branch_code
     *
     * @mbg.generated
     */
    public void setCityBranchCode(String cityBranchCode) {
        this.cityBranchCode = cityBranchCode == null ? null : cityBranchCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pc_system_user_store.store_code
     *
     * @return the value of pc_system_user_store.store_code
     *
     * @mbg.generated
     */
    public String getStoreCode() {
        return storeCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pc_system_user_store.store_code
     *
     * @param storeCode the value for pc_system_user_store.store_code
     *
     * @mbg.generated
     */
    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode == null ? null : storeCode.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SystemUserStore that = (SystemUserStore) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (branchCode != null ? !branchCode.equals(that.branchCode) : that.branchCode != null) return false;
        if (cityBranchCode != null ? !cityBranchCode.equals(that.cityBranchCode) : that.cityBranchCode != null)
            return false;
        return storeCode != null ? storeCode.equals(that.storeCode) : that.storeCode == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (branchCode != null ? branchCode.hashCode() : 0);
        result = 31 * result + (cityBranchCode != null ? cityBranchCode.hashCode() : 0);
        result = 31 * result + (storeCode != null ? storeCode.hashCode() : 0);
        return result;
    }
}