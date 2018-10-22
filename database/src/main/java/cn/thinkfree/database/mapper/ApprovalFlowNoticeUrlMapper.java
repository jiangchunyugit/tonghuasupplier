package cn.thinkfree.database.mapper;

import cn.thinkfree.database.model.ApprovalFlowNoticeUrl;
import cn.thinkfree.database.model.ApprovalFlowNoticeUrlExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ApprovalFlowNoticeUrlMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table af_notice_url
     *
     * @mbg.generated
     */
    long countByExample(ApprovalFlowNoticeUrlExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table af_notice_url
     *
     * @mbg.generated
     */
    int deleteByExample(ApprovalFlowNoticeUrlExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table af_notice_url
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table af_notice_url
     *
     * @mbg.generated
     */
    int insert(ApprovalFlowNoticeUrl record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table af_notice_url
     *
     * @mbg.generated
     */
    int insertSelective(ApprovalFlowNoticeUrl record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table af_notice_url
     *
     * @mbg.generated
     */
    List<ApprovalFlowNoticeUrl> selectByExample(ApprovalFlowNoticeUrlExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table af_notice_url
     *
     * @mbg.generated
     */
    ApprovalFlowNoticeUrl selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table af_notice_url
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") ApprovalFlowNoticeUrl record, @Param("example") ApprovalFlowNoticeUrlExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table af_notice_url
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") ApprovalFlowNoticeUrl record, @Param("example") ApprovalFlowNoticeUrlExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table af_notice_url
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(ApprovalFlowNoticeUrl record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table af_notice_url
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(ApprovalFlowNoticeUrl record);

    List<ApprovalFlowNoticeUrl> findByNodeNum(String nodeNum);
}