package cn.thinkfree.database.mapper;

import cn.thinkfree.database.appvo.OrderTaskSortVo;
import cn.thinkfree.database.model.ProjectStageLog;
import cn.thinkfree.database.model.ProjectStageLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProjectStageLogMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table project_stage_log
     *
     * @mbg.generated
     */
    long countByExample(ProjectStageLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table project_stage_log
     *
     * @mbg.generated
     */
    int deleteByExample(ProjectStageLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table project_stage_log
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table project_stage_log
     *
     * @mbg.generated
     */
    int insert(ProjectStageLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table project_stage_log
     *
     * @mbg.generated
     */
    int insertSelective(ProjectStageLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table project_stage_log
     *
     * @mbg.generated
     */
    List<ProjectStageLog> selectByExample(ProjectStageLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table project_stage_log
     *
     * @mbg.generated
     */
    ProjectStageLog selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table project_stage_log
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") ProjectStageLog record, @Param("example") ProjectStageLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table project_stage_log
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") ProjectStageLog record, @Param("example") ProjectStageLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table project_stage_log
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(ProjectStageLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table project_stage_log
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(ProjectStageLog record);

    List<OrderTaskSortVo> selectByProjectNo(@Param("projectNo") String projectNo);
}