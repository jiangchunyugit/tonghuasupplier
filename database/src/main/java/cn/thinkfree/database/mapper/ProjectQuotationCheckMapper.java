package cn.thinkfree.database.mapper;

import cn.thinkfree.database.model.ProjectQuotationCheck;
import cn.thinkfree.database.model.ProjectQuotationCheckExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProjectQuotationCheckMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table project_quotation_check
     *
     * @mbg.generated
     */
    long countByExample(ProjectQuotationCheckExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table project_quotation_check
     *
     * @mbg.generated
     */
    int deleteByExample(ProjectQuotationCheckExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table project_quotation_check
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table project_quotation_check
     *
     * @mbg.generated
     */
    int insert(ProjectQuotationCheck record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table project_quotation_check
     *
     * @mbg.generated
     */
    int insertSelective(ProjectQuotationCheck record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table project_quotation_check
     *
     * @mbg.generated
     */
    List<ProjectQuotationCheck> selectByExample(ProjectQuotationCheckExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table project_quotation_check
     *
     * @mbg.generated
     */
    ProjectQuotationCheck selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table project_quotation_check
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") ProjectQuotationCheck record, @Param("example") ProjectQuotationCheckExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table project_quotation_check
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") ProjectQuotationCheck record, @Param("example") ProjectQuotationCheckExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table project_quotation_check
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(ProjectQuotationCheck record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table project_quotation_check
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(ProjectQuotationCheck record);
}