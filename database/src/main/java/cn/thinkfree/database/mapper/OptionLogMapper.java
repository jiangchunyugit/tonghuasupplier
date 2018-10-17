package cn.thinkfree.database.mapper;

import cn.thinkfree.database.model.OptionLog;
import cn.thinkfree.database.model.OptionLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OptionLogMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table option_log
     *
     * @mbg.generated
     */
    long countByExample(OptionLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table option_log
     *
     * @mbg.generated
     */
    int deleteByExample(OptionLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table option_log
     *
     * @mbg.generated
     */
    int insert(OptionLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table option_log
     *
     * @mbg.generated
     */
    int insertSelective(OptionLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table option_log
     *
     * @mbg.generated
     */
    List<OptionLog> selectByExample(OptionLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table option_log
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") OptionLog record, @Param("example") OptionLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table option_log
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") OptionLog record, @Param("example") OptionLogExample example);
}