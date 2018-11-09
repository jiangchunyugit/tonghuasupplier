package cn.thinkfree.database.mapper;

import cn.thinkfree.database.model.ProjectSmallScheduling;
import cn.thinkfree.database.model.ProjectSmallSchedulingExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProjectSmallSchedulingMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table project_base_small_scheduling
     *
     * @mbg.generated
     */
    long countByExample(ProjectSmallSchedulingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table project_base_small_scheduling
     *
     * @mbg.generated
     */
    int deleteByExample(ProjectSmallSchedulingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table project_base_small_scheduling
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table project_base_small_scheduling
     *
     * @mbg.generated
     */
    int insert(ProjectSmallScheduling record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table project_base_small_scheduling
     *
     * @mbg.generated
     */
    int insertSelective(ProjectSmallScheduling record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table project_base_small_scheduling
     *
     * @mbg.generated
     */
    List<ProjectSmallScheduling> selectByExample(ProjectSmallSchedulingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table project_base_small_scheduling
     *
     * @mbg.generated
     */
    ProjectSmallScheduling selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table project_base_small_scheduling
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") ProjectSmallScheduling record, @Param("example") ProjectSmallSchedulingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table project_base_small_scheduling
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") ProjectSmallScheduling record, @Param("example") ProjectSmallSchedulingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table project_base_small_scheduling
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(ProjectSmallScheduling record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table project_base_small_scheduling
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(ProjectSmallScheduling record);

    /**
     * 根据状态查询本地小排期信息
     * @param value
     * @return
     */
    List<ProjectSmallScheduling> selectByStatus(Integer value);
}