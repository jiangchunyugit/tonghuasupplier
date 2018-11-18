package cn.thinkfree.database.mapper;

import cn.thinkfree.database.model.CityBranch;
import cn.thinkfree.database.model.CityBranchExample;
import java.util.List;

import cn.thinkfree.database.vo.CityBranchSEO;
import cn.thinkfree.database.vo.CityBranchVO;
import cn.thinkfree.database.vo.CityBranchWtihProCitVO;
import org.apache.ibatis.annotations.Param;

public interface CityBranchMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_city_branch

     */
    long countByExample(CityBranchExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_city_branch

     */
    int deleteByExample(CityBranchExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_city_branch

     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_city_branch

     */
    int insert(CityBranch record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_city_branch

     */
    int insertSelective(CityBranch record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_city_branch

     */
    List<CityBranch> selectByExample(CityBranchExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_city_branch

     */
    CityBranch selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_city_branch

     */
    int updateByExampleSelective(@Param("record") CityBranch record, @Param("example") CityBranchExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_city_branch

     */
    int updateByExample(@Param("record") CityBranch record, @Param("example") CityBranchExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_city_branch

     */
    int updateByPrimaryKeySelective(CityBranch record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_city_branch

     */
    int updateByPrimaryKey(CityBranch record);

    CityBranchVO selectBranchDetails(Integer id);

    List<CityBranchVO> selectBranchCompanyByParam(CityBranchSEO cityBranchSEO);

    List<CityBranchWtihProCitVO> selectCityBranchWithProCit(String branchCompanyCode);

}