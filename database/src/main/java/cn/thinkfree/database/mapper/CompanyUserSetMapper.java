package cn.thinkfree.database.mapper;

import cn.thinkfree.database.model.CompanyUserSet;
import cn.thinkfree.database.model.CompanyUserSetExample;
import java.util.List;
import java.util.Map;

import cn.thinkfree.database.vo.CompanyUserSetVo;
import cn.thinkfree.database.vo.IndexUserReportVO;
import cn.thinkfree.database.vo.StaffSEO;
import cn.thinkfree.database.vo.StaffsVO;
import org.apache.ibatis.annotations.Param;

public interface CompanyUserSetMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table company_user_set
     *
     * @mbg.generated
     */
    long countByExample(CompanyUserSetExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table company_user_set
     *
     * @mbg.generated
     */
    int deleteByExample(CompanyUserSetExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table company_user_set
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table company_user_set
     *
     * @mbg.generated
     */
    int insert(CompanyUserSet record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table company_user_set
     *
     * @mbg.generated
     */
    int insertSelective(CompanyUserSet record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table company_user_set
     *
     * @mbg.generated
     */
    List<CompanyUserSet> selectByExample(CompanyUserSetExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table company_user_set
     *
     * @mbg.generated
     */
    CompanyUserSet selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table company_user_set
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") CompanyUserSet record, @Param("example") CompanyUserSetExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table company_user_set
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") CompanyUserSet record, @Param("example") CompanyUserSetExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table company_user_set
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(CompanyUserSet record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table company_user_set
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(CompanyUserSet record);


    /**
     * 汇总公司员工信息
     * @param companyRelationMap
     */
    IndexUserReportVO countCompanyUser(List<String> companyRelationMap);

    /**
     * 查询公司所有员工
     * @param companyUserSetExample
     * @return
     */
    List<StaffsVO> selectStaffsVOByExample(CompanyUserSetExample companyUserSetExample);
    /**
     * 查询员工列表信息
     */
    List<StaffsVO> findStaffByParam(StaffSEO staffSEO);

    /**
     * 员工移除
     * @param userId
     * @return
     */
    int updateIsJob(String userId);

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    StaffsVO findByUserId(String userId);

    /**
     * 子公司：员工信息  根据公司id查询员工
     * @param companyId
     * @return
     */
    List<StaffsVO> staffByCompanyID(String companyId);
}