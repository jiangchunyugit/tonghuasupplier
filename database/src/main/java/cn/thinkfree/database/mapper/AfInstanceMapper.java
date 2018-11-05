package cn.thinkfree.database.mapper;

import cn.thinkfree.database.model.AfInstance;
import cn.thinkfree.database.model.AfInstanceExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AfInstanceMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table a_f_instance
     *
     * @mbg.generated
     */
    long countByExample(AfInstanceExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table a_f_instance
     *
     * @mbg.generated
     */
    int deleteByExample(AfInstanceExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table a_f_instance
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table a_f_instance
     *
     * @mbg.generated
     */
    int insert(AfInstance record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table a_f_instance
     *
     * @mbg.generated
     */
    int insertSelective(AfInstance record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table a_f_instance
     *
     * @mbg.generated
     */
    List<AfInstance> selectByExample(AfInstanceExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table a_f_instance
     *
     * @mbg.generated
     */
    AfInstance selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table a_f_instance
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") AfInstance record, @Param("example") AfInstanceExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table a_f_instance
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") AfInstance record, @Param("example") AfInstanceExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table a_f_instance
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(AfInstance record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table a_f_instance
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(AfInstance record);

    Integer getProjectCheckResult(@Param("projectNo") String projectNo, @Param("configNo") String configNo, @Param("statuses") List<Integer> statuses);

    List<AfInstance> getProjectCheckResults(@Param("projectNos") List<String> projectNos, @Param("configNo") String configNo, @Param("statuses") List<Integer> statuses);
}