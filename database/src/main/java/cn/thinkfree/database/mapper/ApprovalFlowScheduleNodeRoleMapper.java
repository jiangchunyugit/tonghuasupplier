package cn.thinkfree.database.mapper;

import cn.thinkfree.database.model.ApprovalFlowScheduleNodeRole;
import cn.thinkfree.database.model.ApprovalFlowScheduleNodeRoleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ApprovalFlowScheduleNodeRoleMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table af_schedule_node_role
     *
     * @mbg.generated
     */
    long countByExample(ApprovalFlowScheduleNodeRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table af_schedule_node_role
     *
     * @mbg.generated
     */
    int deleteByExample(ApprovalFlowScheduleNodeRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table af_schedule_node_role
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table af_schedule_node_role
     *
     * @mbg.generated
     */
    int insert(ApprovalFlowScheduleNodeRole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table af_schedule_node_role
     *
     * @mbg.generated
     */
    int insertSelective(ApprovalFlowScheduleNodeRole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table af_schedule_node_role
     *
     * @mbg.generated
     */
    List<ApprovalFlowScheduleNodeRole> selectByExample(ApprovalFlowScheduleNodeRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table af_schedule_node_role
     *
     * @mbg.generated
     */
    ApprovalFlowScheduleNodeRole selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table af_schedule_node_role
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") ApprovalFlowScheduleNodeRole record, @Param("example") ApprovalFlowScheduleNodeRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table af_schedule_node_role
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") ApprovalFlowScheduleNodeRole record, @Param("example") ApprovalFlowScheduleNodeRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table af_schedule_node_role
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(ApprovalFlowScheduleNodeRole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table af_schedule_node_role
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(ApprovalFlowScheduleNodeRole record);

    List<ApprovalFlowScheduleNodeRole> findLastVersionByNodeNumAndScheduleSort(@Param("nodeNum") String nodeNum, @Param("scheduleSort") Integer scheduleSort);
}