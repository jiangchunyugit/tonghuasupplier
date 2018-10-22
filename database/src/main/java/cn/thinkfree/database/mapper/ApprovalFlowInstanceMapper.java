package cn.thinkfree.database.mapper;

import cn.thinkfree.database.model.ApprovalFlowInstance;
import cn.thinkfree.database.model.ApprovalFlowInstanceExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ApprovalFlowInstanceMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table af_instance
     *
     * @mbg.generated
     */
    long countByExample(ApprovalFlowInstanceExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table af_instance
     *
     * @mbg.generated
     */
    int deleteByExample(ApprovalFlowInstanceExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table af_instance
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table af_instance
     *
     * @mbg.generated
     */
    int insert(ApprovalFlowInstance record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table af_instance
     *
     * @mbg.generated
     */
    int insertSelective(ApprovalFlowInstance record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table af_instance
     *
     * @mbg.generated
     */
    List<ApprovalFlowInstance> selectByExample(ApprovalFlowInstanceExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table af_instance
     *
     * @mbg.generated
     */
    ApprovalFlowInstance selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table af_instance
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") ApprovalFlowInstance record, @Param("example") ApprovalFlowInstanceExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table af_instance
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") ApprovalFlowInstance record, @Param("example") ApprovalFlowInstanceExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table af_instance
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(ApprovalFlowInstance record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table af_instance
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(ApprovalFlowInstance record);
}