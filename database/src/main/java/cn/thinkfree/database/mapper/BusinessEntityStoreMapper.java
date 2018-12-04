package cn.thinkfree.database.mapper;

import cn.thinkfree.database.model.BusinessEntityStore;
import cn.thinkfree.database.model.BusinessEntityStoreExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BusinessEntityStoreMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_business_entity_store
     *
     * @mbg.generated
     */
    long countByExample(BusinessEntityStoreExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_business_entity_store
     *
     * @mbg.generated
     */
    int deleteByExample(BusinessEntityStoreExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_business_entity_store
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_business_entity_store
     *
     * @mbg.generated
     */
    int insert(BusinessEntityStore record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_business_entity_store
     *
     * @mbg.generated
     */
    int insertSelective(BusinessEntityStore record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_business_entity_store
     *
     * @mbg.generated
     */
    List<BusinessEntityStore> selectByExample(BusinessEntityStoreExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_business_entity_store
     *
     * @mbg.generated
     */
    BusinessEntityStore selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_business_entity_store
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") BusinessEntityStore record, @Param("example") BusinessEntityStoreExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_business_entity_store
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") BusinessEntityStore record, @Param("example") BusinessEntityStoreExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_business_entity_store
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(BusinessEntityStore record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_business_entity_store
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(BusinessEntityStore record);
}