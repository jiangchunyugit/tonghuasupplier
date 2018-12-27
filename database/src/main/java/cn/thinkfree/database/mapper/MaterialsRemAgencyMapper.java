package cn.thinkfree.database.mapper;

import cn.thinkfree.database.model.MaterialsRemAgency;
import cn.thinkfree.database.model.MaterialsRemAgencyExample;
import java.util.List;

import cn.thinkfree.database.vo.AgencyContractCompanyInfoVo;
import org.apache.ibatis.annotations.Param;

public interface MaterialsRemAgencyMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table materials_rem_agency
     *
     * @mbg.generated
     */
    long countByExample(MaterialsRemAgencyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table materials_rem_agency
     *
     * @mbg.generated
     */
    int deleteByExample(MaterialsRemAgencyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table materials_rem_agency
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table materials_rem_agency
     *
     * @mbg.generated
     */
    int insert(MaterialsRemAgency record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table materials_rem_agency
     *
     * @mbg.generated
     */
    int insertSelective(MaterialsRemAgency record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table materials_rem_agency
     *
     * @mbg.generated
     */
    List<MaterialsRemAgency> selectByExample(MaterialsRemAgencyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table materials_rem_agency
     *
     * @mbg.generated
     */
    MaterialsRemAgency selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table materials_rem_agency
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") MaterialsRemAgency record, @Param("example") MaterialsRemAgencyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table materials_rem_agency
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") MaterialsRemAgency record, @Param("example") MaterialsRemAgencyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table materials_rem_agency
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(MaterialsRemAgency record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table materials_rem_agency
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(MaterialsRemAgency record);

    /**
     * 经销商信息
     * @param companyId
     * @param companyName
     * @return
     */
    List<AgencyContractCompanyInfoVo> getAgencyCompanyInfos(@Param("companyId")String companyId, @Param("companyName")String companyName);
}