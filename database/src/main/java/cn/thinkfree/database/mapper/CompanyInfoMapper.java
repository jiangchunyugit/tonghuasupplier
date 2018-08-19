package cn.thinkfree.database.mapper;

import cn.thinkfree.database.model.CompanyInfo;
import cn.thinkfree.database.model.CompanyInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CompanyInfoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table company_info
     *
     * @mbg.generated
     */
    long countByExample(CompanyInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table company_info
     *
     * @mbg.generated
     */
    int deleteByExample(CompanyInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table company_info
     *
     * @mbg.generated
     */
    int insert(CompanyInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table company_info
     *
     * @mbg.generated
     */
    int insertSelective(CompanyInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table company_info
     *
     * @mbg.generated
     */
    List<CompanyInfo> selectByExample(CompanyInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table company_info
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") CompanyInfo record, @Param("example") CompanyInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table company_info
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") CompanyInfo record, @Param("example") CompanyInfoExample example);

    /**
     * 根据根公司查询公司关系图
     * @param rootCompanyId
     * @return
     */
    List<String> selectRelationMapByRootCompany(String rootCompanyId);

    /**
     * 根据id获取公司信息
     * @param companyId
     * @return
     */
    List<CompanyInfo> selectByCompany(List<String> companyId);

    /**
     * 根据id获取公司信息
     * @param companyId
     * @return
     */
    CompanyInfo findByCompanyId(String companyId);
}