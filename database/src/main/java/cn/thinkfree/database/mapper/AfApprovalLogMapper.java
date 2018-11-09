package cn.thinkfree.database.mapper;

import cn.thinkfree.database.model.AfApprovalLog;
import cn.thinkfree.database.model.AfApprovalLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AfApprovalLogMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table a_f_approval_log
     *
     * @mbg.generated
     */
    long countByExample(AfApprovalLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table a_f_approval_log
     *
     * @mbg.generated
     */
    int deleteByExample(AfApprovalLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table a_f_approval_log
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table a_f_approval_log
     *
     * @mbg.generated
     */
    int insert(AfApprovalLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table a_f_approval_log
     *
     * @mbg.generated
     */
    int insertSelective(AfApprovalLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table a_f_approval_log
     *
     * @mbg.generated
     */
    List<AfApprovalLog> selectByExample(AfApprovalLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table a_f_approval_log
     *
     * @mbg.generated
     */
    AfApprovalLog selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table a_f_approval_log
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") AfApprovalLog record, @Param("example") AfApprovalLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table a_f_approval_log
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") AfApprovalLog record, @Param("example") AfApprovalLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table a_f_approval_log
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(AfApprovalLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table a_f_approval_log
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(AfApprovalLog record);
}